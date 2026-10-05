export type ViewKey = 'overview' | 'audit' | 'claims' | 'enrollment' | 'patient' | 'cloud';
export type AnomalySeverity = 'critical' | 'review';
export type ClaimStatus = 'open' | 'logistics-review' | 'approved' | 'rejected';
export type ArchiveEligibility = 'archivable' | 'legal-review' | 'retention-hold';

export interface AuditAnomaly {
  id: string;
  memberId: string;
  member: string;
  initials: string;
  issue: string;
  detail: string;
  severity: AnomalySeverity;
  source: string;
  date: string;
  confidence: number;
  comparison: {
    field: string;
    recordA: string;
    recordB: string;
    recommendedValue: string;
    evidence: string;
  };
}

export interface InsurancePolicy {
  insurer: string;
  planName: string;
  policyId: string;
  effectiveDate: string;
  renewalDate: string;
  monthlyPremium: number;
  individualDeductible: number;
  deductibleMet: number;
}

export interface BillingRecord {
  id: string;
  date: string;
  description: string;
  amount: number;
  status: 'paid' | 'pending' | 'overdue';
}

export interface PatientTimelineEvent {
  id: string;
  date: string;
  title: string;
  description: string;
  category: 'claim' | 'service' | 'billing' | 'coordination';
  status?: string;
}

export interface PatientProfile {
  memberId: string;
  fullName: string;
  initials: string;
  status: 'active' | 'inactive' | 'pending';
  enrolledAt: string;
  city: string;
  state: string;
  zipCode: string;
  phone: string;
  email: string;
  assignedBroker: string;
  lastUpdated: string;
  policy: InsurancePolicy;
  billingRecords: BillingRecord[];
  timeline: PatientTimelineEvent[];
}

export interface ClaimCase {
  id: string;
  memberId: string;
  memberName: string;
  service: string;
  location: string;
  status: ClaimStatus;
  priority: 'high' | 'normal';
  updatedAt: string;
  amount?: number;
}

export interface CloudArchiveCandidate {
  id: string;
  description: string;
  documentType: string;
  lastActivity: string;
  sizeMb: number;
  eligibility: ArchiveEligibility;
}

export interface ErpDemoData {
  anomalies: AuditAnomaly[];
  claims: ClaimCase[];
  patient: PatientProfile;
  archiveCandidates: CloudArchiveCandidate[];
}

export interface NavigationItem {
  key: ViewKey;
  label: string;
  icon: 'grid' | 'scan' | 'file' | 'users' | 'user' | 'cloud';
  section: 'OPERACIONES' | 'GESTIÓN' | 'SISTEMA';
  count?: string;
}