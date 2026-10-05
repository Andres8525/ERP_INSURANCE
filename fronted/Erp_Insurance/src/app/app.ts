import { Component, computed, signal } from '@angular/core';
import { ERP_DEMO_DATA, NAVIGATION_ITEMS } from './data/erp.demo-data';
import { AnomalySeverity, AuditAnomaly, NavigationItem, ViewKey } from './models/erp.models';

@Component({
  selector: 'app-root',
  imports: [],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  readonly activeView = signal<ViewKey>('overview');
  readonly sidebarOpen = signal(false);
  readonly filterSeverity = signal<'all' | AnomalySeverity>('all');
  readonly selectedAnomaly = signal<AuditAnomaly | null>(null);
  readonly enrollmentStep = signal(1);
  readonly anomalies = signal<AuditAnomaly[]>([...ERP_DEMO_DATA.anomalies]);
  readonly navItems: NavigationItem[] = NAVIGATION_ITEMS;
  readonly patient = ERP_DEMO_DATA.patient;
  readonly claims = ERP_DEMO_DATA.claims;
  readonly archiveCandidates = ERP_DEMO_DATA.archiveCandidates;

  readonly visibleAnomalies = computed(() => {
    const filter = this.filterSeverity();
    return filter === 'all' ? this.anomalies() : this.anomalies().filter((item) => item.severity === filter);
  });

  setView(view: ViewKey): void {
    this.activeView.set(view);
    this.sidebarOpen.set(false);
  }

  setFilter(filter: 'all' | AnomalySeverity): void {
    this.filterSeverity.set(filter);
  }

  openConflict(anomaly: AuditAnomaly): void {
    this.selectedAnomaly.set(anomaly);
  }

  closeConflict(): void {
    this.selectedAnomaly.set(null);
  }

  applySuggestion(): void {
    const selected = this.selectedAnomaly();
    if (!selected) return;
    this.anomalies.update((items) => items.filter((item) => item.id !== selected.id));
    this.closeConflict();
  }

  moveEnrollmentStep(direction: 1 | -1): void {
    this.enrollmentStep.update((step) => Math.min(3, Math.max(1, step + direction)));
  }
}
