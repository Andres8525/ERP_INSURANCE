package com.erp.insurance.audit;

import com.erp.insurance.domain.DomainEnums.AnomalyResolutionStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DataAnomalyRepository extends JpaRepository<DataAnomalyEntity, UUID> {
    Page<DataAnomalyEntity> findBySeverityAndResolutionStatusAndType(
            com.erp.insurance.domain.DomainEnums.AnomalySeverity severity,
            AnomalyResolutionStatus resolutionStatus,
            com.erp.insurance.domain.DomainEnums.AnomalyType type,
            Pageable pageable);
    Page<DataAnomalyEntity> findBySeverityAndResolutionStatus(
            com.erp.insurance.domain.DomainEnums.AnomalySeverity severity,
            AnomalyResolutionStatus resolutionStatus,
            Pageable pageable);
    Page<DataAnomalyEntity> findBySeverityAndType(
            com.erp.insurance.domain.DomainEnums.AnomalySeverity severity,
            com.erp.insurance.domain.DomainEnums.AnomalyType type,
            Pageable pageable);
    Page<DataAnomalyEntity> findByResolutionStatusAndType(
            AnomalyResolutionStatus resolutionStatus,
            com.erp.insurance.domain.DomainEnums.AnomalyType type,
            Pageable pageable);
    Page<DataAnomalyEntity> findBySeverity(
            com.erp.insurance.domain.DomainEnums.AnomalySeverity severity, Pageable pageable);
    Page<DataAnomalyEntity> findByResolutionStatus(AnomalyResolutionStatus status, Pageable pageable);
    Page<DataAnomalyEntity> findByType(
            com.erp.insurance.domain.DomainEnums.AnomalyType type, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from DataAnomalyEntity a where a.id in :ids")
    List<DataAnomalyEntity> findAllByIdForUpdate(@Param("ids") List<UUID> ids);
}