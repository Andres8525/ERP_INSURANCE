package com.erp.insurance.domain;

public final class DomainEnums {
    private DomainEnums() {}

    public enum UserRole { ADMIN, AGENT, AUDITOR }
    public enum AccountStatus { ACTIVE, PENDING, INACTIVE }
    public enum AcaEligibilityStatus { UNKNOWN, PENDING, ELIGIBLE, INELIGIBLE }
    public enum PolicyStatus { PENDING, ACTIVE, TERMINATED }
    public enum ClaimStatus { OPEN, IN_REVIEW, APPROVED, REJECTED }
    public enum AnomalyType { POLICY_OVERLAP, DUPLICATE, OBSOLETE_FILE, OTHER }
    public enum AnomalySeverity { RED, YELLOW }
    public enum AnomalyResolutionStatus { OPEN, RESOLVED, ARCHIVED }
    public enum AffectedRecordType { PATIENT, POLICY, CLAIM }
    public enum BatchResolutionAction { ARCHIVAR_DEFINITIVO, AUTOCORREGIR }
}