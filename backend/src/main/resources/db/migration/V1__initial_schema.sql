CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(320) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE patients (
    id UUID PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL,
    email_lookup_hash CHAR(64) NOT NULL UNIQUE,
    phone TEXT,
    date_of_birth TEXT NOT NULL,
    household_income TEXT,
    aca_eligibility_status VARCHAR(24) NOT NULL,
    account_status VARCHAR(24) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE policies (
    id UUID PRIMARY KEY,
    policy_number TEXT NOT NULL,
    insurer VARCHAR(200) NOT NULL,
    coverage_start DATE NOT NULL,
    coverage_end DATE NOT NULL,
    premium_amount NUMERIC(12, 2) NOT NULL CHECK (premium_amount >= 0),
    status VARCHAR(24) NOT NULL,
    has_anomalies BOOLEAN NOT NULL DEFAULT FALSE,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE RESTRICT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (coverage_end >= coverage_start)
);
CREATE INDEX idx_policies_patient_id ON policies(patient_id);
CREATE INDEX idx_policies_coverage ON policies(coverage_start, coverage_end);

CREATE TABLE claims (
    id UUID PRIMARY KEY,
    claim_number VARCHAR(80) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    status VARCHAR(24) NOT NULL,
    patient_id UUID NOT NULL REFERENCES patients(id) ON DELETE RESTRICT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_claims_status_created ON claims(status, created_at DESC);

CREATE TABLE claim_services (
    id UUID PRIMARY KEY,
    name VARCHAR(160) NOT NULL,
    details TEXT,
    scheduled_at TIMESTAMPTZ,
    claim_id UUID NOT NULL REFERENCES claims(id) ON DELETE CASCADE
);
CREATE INDEX idx_claim_services_claim_id ON claim_services(claim_id);

CREATE TABLE data_anomalies (
    id UUID PRIMARY KEY,
    type VARCHAR(32) NOT NULL,
    severity VARCHAR(16) NOT NULL,
    description TEXT NOT NULL,
    resolution_status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    affected_record_type VARCHAR(20) NOT NULL,
    affected_record_id UUID NOT NULL,
    suggested_correction_field VARCHAR(80),
    suggested_correction_value TEXT,
    resolution_action VARCHAR(32),
    resolved_by_id UUID REFERENCES users(id) ON DELETE SET NULL,
    resolved_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_anomalies_inbox ON data_anomalies(resolution_status, created_at DESC);
CREATE INDEX idx_anomalies_filters ON data_anomalies(severity, type, resolution_status);
CREATE INDEX idx_anomalies_affected_record ON data_anomalies(affected_record_type, affected_record_id);