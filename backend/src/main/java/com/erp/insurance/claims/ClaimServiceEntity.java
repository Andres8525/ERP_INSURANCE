package com.erp.insurance.claims;

import com.erp.insurance.security.SensitiveDataConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "claim_services")
public class ClaimServiceEntity {
    @Id @GeneratedValue @UuidGenerator
    private UUID id;
    @Column(nullable = false, length = 160)
    private String name;
    @Convert(converter = SensitiveDataConverter.class)
    @Column(columnDefinition = "text")
    private String details;
    @Column(name = "scheduled_at")
    private Instant scheduledAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "claim_id", nullable = false)
    private ClaimEntity claim;

    protected ClaimServiceEntity() {}
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDetails() { return details; }
    public Instant getScheduledAt() { return scheduledAt; }
}