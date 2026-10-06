package com.erp.insurance.claims;

import com.erp.insurance.domain.DomainEnums.ClaimStatus;
import com.erp.insurance.patients.PatientEntity;
import com.erp.insurance.security.SensitiveDataConverter;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "claims")
public class ClaimEntity {
    @Id @GeneratedValue @UuidGenerator
    private UUID id;
    @Column(name = "claim_number", nullable = false, unique = true, length = 80)
    private String claimNumber;
    @Convert(converter = SensitiveDataConverter.class)
    @Column(nullable = false, columnDefinition = "text")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private ClaimStatus status = ClaimStatus.OPEN;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;
    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<ClaimServiceEntity> services = new ArrayList<>();
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected ClaimEntity() {}
    public UUID getId() { return id; }
    public String getClaimNumber() { return claimNumber; }
    public String getDescription() { return description; }
    public ClaimStatus getStatus() { return status; }
    public PatientEntity getPatient() { return patient; }
    public List<ClaimServiceEntity> getServices() { return services; }
    public Instant getCreatedAt() { return createdAt; }
}