package com.erp.insurance.claims;

import com.erp.insurance.domain.DomainEnums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.UUID;

public interface ClaimRepository extends JpaRepository<ClaimEntity, UUID> {
    @EntityGraph(attributePaths = {"patient", "services"})
    List<ClaimEntity> findAllByOrderByCreatedAtDesc();
    @EntityGraph(attributePaths = {"patient", "services"})
    List<ClaimEntity> findByStatusOrderByCreatedAtDesc(ClaimStatus status);
}