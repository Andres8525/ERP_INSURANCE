package com.erp.insurance.patients;

import com.erp.insurance.domain.DomainEnums.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<PatientEntity, UUID> {
    Optional<PatientEntity> findByEmailLookupHash(String emailLookupHash);
    Page<PatientEntity> findByAccountStatus(AccountStatus accountStatus, Pageable pageable);
}