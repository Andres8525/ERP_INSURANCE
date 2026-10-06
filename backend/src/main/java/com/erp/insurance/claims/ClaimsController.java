package com.erp.insurance.claims;

import com.erp.insurance.domain.DomainEnums.ClaimStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/claims")
public class ClaimsController {
    private final ClaimRepository claims;

    public ClaimsController(ClaimRepository claims) {
        this.claims = claims;
    }

    @GetMapping("/pipeline")
    public Map<ClaimStatus, List<ClaimResponse>> pipeline() {
        Map<ClaimStatus, List<ClaimResponse>> pipeline = new EnumMap<>(ClaimStatus.class);
        for (ClaimStatus status : ClaimStatus.values()) {
            pipeline.put(status, claims.findByStatusOrderByCreatedAtDesc(status).stream().map(ClaimResponse::from).toList());
        }
        return pipeline;
    }

    public record ClaimResponse(UUID id, String claimNumber, String description, ClaimStatus status,
                                PatientBrief patient, List<ClaimServiceBrief> services, Instant createdAt) {
        static ClaimResponse from(ClaimEntity claim) {
            var patient = claim.getPatient();
            return new ClaimResponse(claim.getId(), claim.getClaimNumber(), claim.getDescription(), claim.getStatus(),
                    new PatientBrief(patient.getId(), patient.getFirstName(), patient.getLastName()),
                    claim.getServices().stream().map(service -> new ClaimServiceBrief(service.getId(), service.getName(), service.getDetails(), service.getScheduledAt())).toList(),
                    claim.getCreatedAt());
        }
    }
    public record PatientBrief(UUID id, String firstName, String lastName) {}
    public record ClaimServiceBrief(UUID id, String name, String details, Instant scheduledAt) {}
}