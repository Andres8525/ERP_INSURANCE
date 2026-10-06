import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AnomalySeverity, AuditAnomaly, ViewKey } from '../../models/erp.models';

@Component({
  selector: 'app-overview-page',
  standalone: true,
  templateUrl: './overview-page.component.html',
})
export class OverviewPageComponent {
  @Input({ required: true }) visibleAnomalies: AuditAnomaly[] = [];
  @Input({ required: true }) filterSeverity: 'all' | AnomalySeverity = 'all';

  @Output() filterChange = new EventEmitter<'all' | AnomalySeverity>();
  @Output() viewChange = new EventEmitter<ViewKey>();
  @Output() anomalySelected = new EventEmitter<AuditAnomaly>();
}