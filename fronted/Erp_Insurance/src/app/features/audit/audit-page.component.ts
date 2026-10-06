import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AnomalySeverity, AuditAnomaly } from '../../models/erp.models';

@Component({
  selector: 'app-audit-page',
  standalone: true,
  templateUrl: './audit-page.component.html',
})
export class AuditPageComponent {
  @Input({ required: true }) visibleAnomalies: AuditAnomaly[] = [];
  @Input({ required: true }) filterSeverity: 'all' | AnomalySeverity = 'all';

  @Output() filterChange = new EventEmitter<'all' | AnomalySeverity>();
  @Output() anomalySelected = new EventEmitter<AuditAnomaly>();
}