package com.erp.insurance.patients;

import com.erp.insurance.domain.DomainEnums.AccountStatus;
import com.erp.insurance.domain.DomainEnums.AcaEligibilityStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class PatientDtos {
    private PatientDtos() {}

    public record CreatePatientRequest(
            @NotBlank @Size(max = 100) String firstName,
            @NotBlank @Size(max = 100) String lastName,
            @NotBlank @Email @Size(max = 320) String email,
            @Size(max = 32) String phone,
            @NotBlank @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String dateOfBirth,
            @DecimalMin("0.00") @Digits(integer = 8, fraction = 2) BigDecimal householdIncome,
            AcaEligibilityStatus acaEligibilityStatus,
            AccountStatus accountStatus) {}

    public record UpdatePatientRequest(
            @Size(max = 100) String firstName,
            @Size(max = 100) String lastName,
            @Email @Size(max = 320) String email,
            @Size(max = 32) String phone,
            @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String dateOfBirth,
            @DecimalMin("0.00") @Digits(integer = 8, fraction = 2) BigDecimal householdIncome,
            AcaEligibilityStatus acaEligibilityStatus,
            AccountStatus accountStatus) {}

    public record PatientResponse(UUID id, String firstName, String lastName, String email, String phone,
                                 String dateOfBirth, BigDecimal householdIncome,
                                 AcaEligibilityStatus acaEligibilityStatus, AccountStatus accountStatus,
                                 Instant createdAt) {
        static PatientResponse from(PatientEntity patient) {
            return new PatientResponse(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getEmail(),
                    patient.getPhone(), patient.getDateOfBirth(), patient.getHouseholdIncome() == null ? null : new BigDecimal(patient.getHouseholdIncome()),
                    patient.getAcaEligibilityStatus(), patient.getAccountStatus(), patient.getCreatedAt());
        }
    }

    public record PolicySummary(UUID id, String insurer, String coverageStart, String coverageEnd,
                                BigDecimal premiumAmount, String status, boolean hasAnomalies) {}
}