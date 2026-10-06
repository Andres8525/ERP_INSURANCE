package com.erp.insurance.policies;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PolicyRepository extends JpaRepository<PolicyEntity, UUID> {}