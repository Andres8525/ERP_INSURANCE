package com.erp.insurance.patients;

import com.erp.insurance.domain.DomainEnums.AccountStatus;
import com.erp.insurance.domain.DomainEnums.AcaEligibilityStatus;
import com.erp.insurance.policies.PolicyEntity;
import com.erp.insurance.security.SensitiveDataConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "patients")
public class PatientEntity {
    @Id @GeneratedValue @UuidGenerator
    private UUID id;
    @Convert(converter = SensitiveDataConverter.class)
    @Column(name = "first_name", nullable = false, columnDefinition = "text")
    private String firstName;
    @Convert(converter = SensitiveDataConverter.class)
    @Column(name = "last_name", nullable = false, columnDefinition = "text")
    private String lastName;
    @Convert(converter = SensitiveDataConverter.class)
    @Column(nullable = false, columnDefinition = "text")
    private String email;
    @Column(name = "email_lookup_hash", nullable = false, unique = true, length = 64)
    private String emailLookupHash;
    @Convert(converter = SensitiveDataConverter.class)
    @Column(columnDefinition = "text")
    private String phone;
    @Convert(converter = SensitiveDataConverter.class)
    @Column(name = "date_of_birth", nullable = false, columnDefinition = "text")
    private String dateOfBirth;
    @Convert(converter = SensitiveDataConverter.class)
    @Column(name = "household_income", columnDefinition = "text")
    private String householdIncome;
    @Enumerated(EnumType.STRING)
    @Column(name = "aca_eligibility_status", nullable = false, length = 24)
    private AcaEligibilityStatus acaEligibilityStatus = AcaEligibilityStatus.PENDING;
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 24)
    private AccountStatus accountStatus = AccountStatus.PENDING;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PolicyEntity> policies = new ArrayList<>();
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    protected PatientEntity() {}
    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String value) { firstName = value; }
    public String getLastName() { return lastName; }
    public void setLastName(String value) { lastName = value; }
    public String getEmail() { return email; }
    public void setEmail(String value) { email = value; }
    public String getEmailLookupHash() { return emailLookupHash; }
    public void setEmailLookupHash(String value) { emailLookupHash = value; }
    public String getPhone() { return phone; }
    public void setPhone(String value) { phone = value; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String value) { dateOfBirth = value; }
    public String getHouseholdIncome() { return householdIncome; }
    public void setHouseholdIncome(String value) { householdIncome = value; }
    public AcaEligibilityStatus getAcaEligibilityStatus() { return acaEligibilityStatus; }
    public void setAcaEligibilityStatus(AcaEligibilityStatus value) { acaEligibilityStatus = value; }
    public AccountStatus getAccountStatus() { return accountStatus; }
    public void setAccountStatus(AccountStatus value) { accountStatus = value; }
    public List<PolicyEntity> getPolicies() { return policies; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    @jakarta.persistence.PreUpdate
    void updateTimestamp() { updatedAt = Instant.now(); }
}