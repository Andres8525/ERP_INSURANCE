package com.erp.insurance.policies;

import com.erp.insurance.domain.DomainEnums.PolicyStatus;
import com.erp.insurance.patients.PatientEntity;
import com.erp.insurance.security.SensitiveDataConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "policies")
public class PolicyEntity {
    @Id @GeneratedValue @UuidGenerator
    private UUID id;
    @Convert(converter = SensitiveDataConverter.class)
    @Column(name = "policy_number", nullable = false, columnDefinition = "text")
    private String policyNumber;
    @Column(nullable = false, length = 200)
    private String insurer;
    @Column(name = "coverage_start", nullable = false)
    private LocalDate coverageStart;
    @Column(name = "coverage_end", nullable = false)
    private LocalDate coverageEnd;
    @Column(name = "premium_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal premiumAmount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private PolicyStatus status = PolicyStatus.PENDING;
    @Column(name = "has_anomalies", nullable = false)
    private boolean hasAnomalies;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected PolicyEntity() {}
    public UUID getId() { return id; }
    public String getPolicyNumber() { return policyNumber; }
    public String getInsurer() { return insurer; }
    public LocalDate getCoverageStart() { return coverageStart; }
    public LocalDate getCoverageEnd() { return coverageEnd; }
    public BigDecimal getPremiumAmount() { return premiumAmount; }
    public void setPremiumAmount(BigDecimal value) { premiumAmount = value; }
    public PolicyStatus getStatus() { return status; }
    public boolean isHasAnomalies() { return hasAnomalies; }
    public PatientEntity getPatient() { return patient; }
}