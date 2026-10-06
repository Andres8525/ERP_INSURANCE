package com.erp.insurance.claims;

import com.erp.insurance.domain.DomainEnums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClaimRepository extends JpaRepository<ClaimEntity, UUID> {
    List<ClaimEntity> findAllByOrderByCreatedAtDesc();
    List<ClaimEntity> findByStatusOrderByCreatedAtDesc(ClaimStatus status);
}