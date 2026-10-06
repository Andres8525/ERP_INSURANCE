package com.erp.insurance.audit;

import com.erp.insurance.auth.UserEntity;
import com.erp.insurance.auth.UserRepository;
import com.erp.insurance.domain.DomainEnums.AffectedRecordType;
import com.erp.insurance.domain.DomainEnums.AnomalyResolutionStatus;
import com.erp.insurance.domain.DomainEnums.BatchResolutionAction;
import com.erp.insurance.patients.PatientEntity;
import com.erp.insurance.patients.PatientRepository;
import com.erp.insurance.policies.PolicyEntity;
import com.erp.insurance.policies.PolicyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.erp.insurance.audit.AuditDtos.*;

@Service
public class AuditService {
    private final DataAnomalyRepository anomalies;
    private final PatientRepository patients;
    private final PolicyRepository policies;
    private final UserRepository users;

    public AuditService(DataAnomalyRepository anomalies, PatientRepository patients, PolicyRepository policies,
                       UserRepository users) {
        this.anomalies = anomalies;
        this.patients = patients;
        this.policies = policies;
        this.users = users;
    }

    @Transactional(readOnly = true)
    public Page<AnomalyResponse> list(int page, int size,
                                      com.erp.insurance.domain.DomainEnums.AnomalySeverity severity,
                                      AnomalyResolutionStatus resolutionStatus,
                                      com.erp.insurance.domain.DomainEnums.AnomalyType type) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<DataAnomalyEntity> result;
        if (severity != null && resolutionStatus != null && type != null) result = anomalies.findBySeverityAndResolutionStatusAndType(severity, resolutionStatus, type, pageable);
        else if (severity != null && resolutionStatus != null) result = anomalies.findBySeverityAndResolutionStatus(severity, resolutionStatus, pageable);
        else if (severity != null && type != null) result = anomalies.findBySeverityAndType(severity, type, pageable);
        else if (resolutionStatus != null && type != null) result = anomalies.findByResolutionStatusAndType(resolutionStatus, type, pageable);
        else if (severity != null) result = anomalies.findBySeverity(severity, pageable);
        else if (resolutionStatus != null) result = anomalies.findByResolutionStatus(resolutionStatus, pageable);
        else if (type != null) result = anomalies.findByType(type, pageable);
        else result = anomalies.findAll(pageable);
        return result.map(AnomalyResponse::from);
    }

    @Transactional
    public ResolveBatchResponse resolveBatch(ResolveBatchRequest request, UUID actorId) {
        List<UUID> ids = request.ids().stream().distinct().toList();
        List<DataAnomalyEntity> records = anomalies.findAllByIdForUpdate(ids);
        if (records.size() != ids.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more anomalies were not found");
        }
        if (records.stream().anyMatch(record -> record.getResolutionStatus() != AnomalyResolutionStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "All anomalies in a batch must be open");
        }
        UserEntity actor = users.findById(actorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));

        for (DataAnomalyEntity anomaly : records) {
            if (request.action() == BatchResolutionAction.AUTOCORREGIR) applyCorrection(anomaly);
            anomaly.setResolutionStatus(request.action() == BatchResolutionAction.AUTOCORREGIR
                    ? AnomalyResolutionStatus.RESOLVED : AnomalyResolutionStatus.ARCHIVED);
            anomaly.setResolutionAction(request.action());
            anomaly.setResolvedBy(actor);
            anomaly.setResolvedAt(Instant.now());
        }
        anomalies.saveAll(records);
        return new ResolveBatchResponse(records.size(), request.action(), records.stream().map(DataAnomalyEntity::getId).toList());
    }

    private void applyCorrection(DataAnomalyEntity anomaly) {
        String field = anomaly.getSuggestedCorrectionField();
        String value = anomaly.getSuggestedCorrectionValue();
        if (field == null || field.isBlank() || value == null || value.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Anomaly has no complete correction proposal");
        }
        if (anomaly.getAffectedRecordType() == AffectedRecordType.PATIENT && (field.equals("email") || field.equals("phone"))) {
            PatientEntity patient = patients.findById(anomaly.getAffectedRecordId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Affected patient not found"));
            if (field.equals("email")) {
                if (!value.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid suggested email");
                }
                String normalized = value.trim().toLowerCase();
                patient.setEmail(normalized);
                patient.setEmailLookupHash(com.erp.insurance.security.EmailLookup.hash(normalized));
            } else {
                patient.setPhone(value);
            }
            patients.save(patient);
            return;
        }
        if (anomaly.getAffectedRecordType() == AffectedRecordType.POLICY && field.equals("premiumAmount")) {
            BigDecimal amount;
            try {
                amount = new BigDecimal(value);
            } catch (NumberFormatException exception) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid suggested premium");
            }
            if (amount.signum() < 0 || amount.precision() > 12 || amount.scale() > 2) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Suggested premium is out of bounds");
            }
            PolicyEntity policy = policies.findById(anomaly.getAffectedRecordId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Affected policy not found"));
            policy.setPremiumAmount(amount);
            policies.save(policy);
            return;
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                "Correction " + anomaly.getAffectedRecordType() + "." + field + " is not allowlisted");
    }
}