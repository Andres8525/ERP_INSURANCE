package com.erp.insurance.audit;

import com.erp.insurance.domain.DomainEnums.AffectedRecordType;
import com.erp.insurance.domain.DomainEnums.AnomalyResolutionStatus;
import com.erp.insurance.domain.DomainEnums.AnomalySeverity;
import com.erp.insurance.domain.DomainEnums.AnomalyType;
import com.erp.insurance.domain.DomainEnums.BatchResolutionAction;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class AuditDtos {
    private AuditDtos() {}

    public record ResolveBatchRequest(@NotEmpty @Size(max = 100) List<@NotNull UUID> ids,
                                      @NotNull BatchResolutionAction action) {}
    public record AnomalyResponse(UUID id, AnomalyType type, AnomalySeverity severity, String description,
                                  AnomalyResolutionStatus resolutionStatus, AffectedRecordType affectedRecordType,
                                  UUID affectedRecordId, String suggestedCorrectionField,
                                  String suggestedCorrectionValue, Instant createdAt) {
        static AnomalyResponse from(DataAnomalyEntity entity) {
            return new AnomalyResponse(entity.getId(), entity.getType(), entity.getSeverity(), entity.getDescription(),
                    entity.getResolutionStatus(), entity.getAffectedRecordType(), entity.getAffectedRecordId(),
                    entity.getSuggestedCorrectionField(), entity.getSuggestedCorrectionValue(), entity.getCreatedAt());
        }
    }
    public record ResolveBatchResponse(int processed, BatchResolutionAction action, List<UUID> ids) {}
}