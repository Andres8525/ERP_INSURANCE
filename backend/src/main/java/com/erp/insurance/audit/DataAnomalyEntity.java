package com.erp.insurance.audit;

import com.erp.insurance.auth.UserEntity;
import com.erp.insurance.domain.DomainEnums.AffectedRecordType;
import com.erp.insurance.domain.DomainEnums.AnomalyResolutionStatus;
import com.erp.insurance.domain.DomainEnums.AnomalySeverity;
import com.erp.insurance.domain.DomainEnums.AnomalyType;
import com.erp.insurance.domain.DomainEnums.BatchResolutionAction;
import com.erp.insurance.security.SensitiveDataConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "data_anomalies")
public class DataAnomalyEntity {
    @Id @GeneratedValue @UuidGenerator
    private UUID id;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 32)
    private AnomalyType type;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 16)
    private AnomalySeverity severity;
    @Convert(converter = SensitiveDataConverter.class) @Column(nullable = false, columnDefinition = "text")
    private String description;
    @Enumerated(EnumType.STRING) @Column(name = "resolution_status", nullable = false, length = 20)
    private AnomalyResolutionStatus resolutionStatus = AnomalyResolutionStatus.OPEN;
    @Enumerated(EnumType.STRING) @Column(name = "affected_record_type", nullable = false, length = 20)
    private AffectedRecordType affectedRecordType;
    @Column(name = "affected_record_id", nullable = false)
    private UUID affectedRecordId;
    @Column(name = "suggested_correction_field", length = 80)
    private String suggestedCorrectionField;
    @Convert(converter = SensitiveDataConverter.class) @Column(name = "suggested_correction_value", columnDefinition = "text")
    private String suggestedCorrectionValue;
    @Enumerated(EnumType.STRING) @Column(name = "resolution_action", length = 32)
    private BatchResolutionAction resolutionAction;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "resolved_by_id")
    private UserEntity resolvedBy;
    @Column(name = "resolved_at")
    private Instant resolvedAt;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected DataAnomalyEntity() {}
    public UUID getId() { return id; }
    public AnomalyType getType() { return type; }
    public AnomalySeverity getSeverity() { return severity; }
    public String getDescription() { return description; }
    public AnomalyResolutionStatus getResolutionStatus() { return resolutionStatus; }
    public void setResolutionStatus(AnomalyResolutionStatus value) { resolutionStatus = value; }
    public AffectedRecordType getAffectedRecordType() { return affectedRecordType; }
    public UUID getAffectedRecordId() { return affectedRecordId; }
    public String getSuggestedCorrectionField() { return suggestedCorrectionField; }
    public String getSuggestedCorrectionValue() { return suggestedCorrectionValue; }
    public void setResolutionAction(BatchResolutionAction value) { resolutionAction = value; }
    public void setResolvedBy(UserEntity value) { resolvedBy = value; }
    public void setResolvedAt(Instant value) { resolvedAt = value; }
    public Instant getCreatedAt() { return createdAt; }
}