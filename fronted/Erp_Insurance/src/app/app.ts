import { Component, computed, signal } from '@angular/core';

type ViewKey = 'overview' | 'audit' | 'claims' | 'enrollment' | 'patient' | 'cloud';
type Severity = 'critical' | 'review';

interface Anomaly {
  id: string;
  member: string;
  initials: string;
  issue: string;
  detail: string;
  severity: Severity;
  source: string;
  date: string;
}

@Component({
  selector: 'app-root',
  imports: [],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  readonly activeView = signal<ViewKey>('overview');
  readonly sidebarOpen = signal(false);
  readonly filterSeverity = signal<'all' | Severity>('all');
  readonly selectedAnomaly = signal<Anomaly | null>(null);
  readonly enrollmentStep = signal(1);

  readonly anomalies = signal<Anomaly[]>([
    { id: 'AN-2481', member: 'María González', initials: 'MG', issue: 'Pólizas solapadas', detail: 'Ambetter · Silver 73', severity: 'critical', source: 'Marketplace', date: 'Hoy, 09:42' },
    { id: 'AN-2479', member: 'James Rodriguez', initials: 'JR', issue: 'Discrepancia de facturación', detail: 'Prima mensual · $428.16', severity: 'critical', source: 'Facturación', date: 'Hoy, 09:18' },
    { id: 'AN-2476', member: 'Lucía Martínez', initials: 'LM', issue: 'Posible cliente duplicado', detail: 'Coincidencia del 94%', severity: 'review', source: 'CRM', date: 'Hoy, 08:56' },
    { id: 'AN-2472', member: 'Robert Chen', initials: 'RC', issue: 'Elegibilidad por verificar', detail: 'Ingreso anual · $38,400', severity: 'review', source: 'ACA', date: 'Ayer, 16:34' },
    { id: 'AN-2468', member: 'Ana Pérez', initials: 'AP', issue: 'Póliza sin asignación', detail: 'Oscar Health · EPO', severity: 'review', source: 'Marketplace', date: 'Ayer, 15:12' }
  ]);

  readonly visibleAnomalies = computed(() => {
    const filter = this.filterSeverity();
    return filter === 'all' ? this.anomalies() : this.anomalies().filter((item) => item.severity === filter);
  });

  readonly navItems: { key: ViewKey; label: string; icon: string; section: string; count?: string }[] = [
    { key: 'overview', label: 'Resumen', icon: 'grid', section: 'OPERACIONES' },
    { key: 'audit', label: 'Auditoría IA', icon: 'scan', section: 'OPERACIONES', count: '12' },
    { key: 'claims', label: 'Siniestros', icon: 'file', section: 'OPERACIONES', count: '8' },
    { key: 'enrollment', label: 'Afiliaciones', icon: 'users', section: 'GESTIÓN' },
    { key: 'patient', label: 'Pacientes', icon: 'user', section: 'GESTIÓN' },
    { key: 'cloud', label: 'Optimización nube', icon: 'cloud', section: 'SISTEMA' }
  ];

  setView(view: ViewKey): void {
    this.activeView.set(view);
    this.sidebarOpen.set(false);
  }

  setFilter(filter: 'all' | Severity): void {
    this.filterSeverity.set(filter);
  }

  openConflict(anomaly: Anomaly): void {
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
