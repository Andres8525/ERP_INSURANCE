package com.erp.insurance.patients;

import com.erp.insurance.domain.DomainEnums.AccountStatus;
import com.erp.insurance.domain.DomainEnums.AcaEligibilityStatus;
import com.erp.insurance.policies.PolicyEntity;
import com.erp.insurance.security.EmailLookup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

import static com.erp.insurance.patients.PatientDtos.*;

@RestController
@RequestMapping("/api/patients")
@Validated
public class PatientController {
    private final PatientRepository patients;

    public PatientController(PatientRepository patients) {
        this.patients = patients;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse create(@Valid @RequestBody CreatePatientRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        if (patients.findByEmailLookupHash(EmailLookup.hash(normalizedEmail)).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A patient with this email already exists");
        }
        PatientEntity patient = new PatientEntity();
        patient.setFirstName(request.firstName().trim());
        patient.setLastName(request.lastName().trim());
        patient.setEmail(normalizedEmail);
        patient.setEmailLookupHash(EmailLookup.hash(normalizedEmail));
        patient.setPhone(request.phone());
        patient.setDateOfBirth(request.dateOfBirth());
        patient.setHouseholdIncome(request.householdIncome() == null ? null : request.householdIncome().toPlainString());
        patient.setAcaEligibilityStatus(request.acaEligibilityStatus() == null ? AcaEligibilityStatus.PENDING : request.acaEligibilityStatus());
        patient.setAccountStatus(request.accountStatus() == null ? AccountStatus.PENDING : request.accountStatus());
        try {
            return PatientResponse.from(patients.save(patient));
        } catch (org.springframework.dao.DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A patient with this email already exists");
        }
    }

    @GetMapping
    public Page<PatientResponse> list(@RequestParam(defaultValue = "0") @Min(0) int page,
                                      @RequestParam(defaultValue = "25") @Min(1) @Max(100) int size,
                                      @RequestParam(required = false) AccountStatus accountStatus) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PatientEntity> result = accountStatus == null
                ? patients.findAll(pageable) : patients.findByAccountStatus(accountStatus, pageable);
        return result.map(PatientResponse::from);
    }

    @GetMapping("/{id}")
    public PatientResponse get(@PathVariable UUID id) {
        return patients.findById(id).map(PatientResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
    }

    @PatchMapping("/{id}")
    public PatientResponse update(@PathVariable UUID id, @Valid @RequestBody UpdatePatientRequest request) {
        PatientEntity patient = patients.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
        if (request.firstName() != null) patient.setFirstName(request.firstName().trim());
        if (request.lastName() != null) patient.setLastName(request.lastName().trim());
        if (request.email() != null) {
            String normalized = request.email().trim().toLowerCase();
            String hash = EmailLookup.hash(normalized);
            patients.findByEmailLookupHash(hash).filter(existing -> !existing.getId().equals(id)).ifPresent(existing -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A patient with this email already exists");
            });
            patient.setEmail(normalized);
            patient.setEmailLookupHash(hash);
        }
        if (request.phone() != null) patient.setPhone(request.phone());
        if (request.dateOfBirth() != null) patient.setDateOfBirth(request.dateOfBirth());
        if (request.householdIncome() != null) patient.setHouseholdIncome(request.householdIncome().toPlainString());
        if (request.acaEligibilityStatus() != null) patient.setAcaEligibilityStatus(request.acaEligibilityStatus());
        if (request.accountStatus() != null) patient.setAccountStatus(request.accountStatus());
        return PatientResponse.from(patients.save(patient));
    }
}