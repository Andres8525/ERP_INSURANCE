import { Component, Input } from '@angular/core';
import { ClaimCase, ClaimStatus } from '../../models/erp.models';

interface ClaimColumn {
  status: ClaimStatus;
  title: string;
}

@Component({
  selector: 'app-claims-page',
  standalone: true,
  templateUrl: './claims-page.component.html',
})
export class ClaimsPageComponent {
  @Input({ required: true }) claims: ClaimCase[] = [];

  readonly columns: ClaimColumn[] = [
    { status: 'open', title: 'Abierto' },
    { status: 'logistics-review', title: 'En revisión logística' },
    { status: 'approved', title: 'Aprobado' },
    { status: 'rejected', title: 'Rechazado' },
  ];

  claimsFor(status: ClaimStatus): ClaimCase[] {
    return this.claims.filter((claim) => claim.status === status);
  }
}