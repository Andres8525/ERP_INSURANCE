import { TestBed } from '@angular/core/testing';
import { App } from './app';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render the operations dashboard', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Resumen operativo');
    expect(compiled.querySelector('nav')).toBeTruthy();
  });

  it('should filter and resolve anomalies', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;

    app.setFilter('critical');
    expect(app.visibleAnomalies().length).toBe(2);

    app.openConflict(app.visibleAnomalies()[0]);
    app.applySuggestion();

    expect(app.selectedAnomaly()).toBeNull();
    expect(app.anomalies().length).toBe(4);
    expect(app.visibleAnomalies().length).toBe(1);
  });

  it('should render the patient profile from structured data', async () => {
    const fixture = TestBed.createComponent(App);
    fixture.componentInstance.setView('patient');
    fixture.detectChanges();
    await fixture.whenStable();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('AMB-2847-11903');
    expect(compiled.textContent).toContain('Clear Silver 73');
    expect(compiled.querySelectorAll('.timeline-item')).toHaveLength(4);
    expect(compiled.querySelectorAll('tbody tr')).toHaveLength(3);
  });

  it('should render the claims Kanban from typed pipeline data', async () => {
    const fixture = TestBed.createComponent(App);
    fixture.componentInstance.setView('claims');
    fixture.detectChanges();
    await fixture.whenStable();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('CL-48291');
    expect(compiled.textContent).toContain('CL-48260');
    expect(compiled.textContent).toContain('CL-48234');
    expect(compiled.textContent).toContain('CL-48197');
    expect(compiled.textContent).toContain('En revisión logística');
  });

  it('should expose typed operational seed data', () => {
    const app = TestBed.createComponent(App).componentInstance;

    expect(app.patient.memberId).toBe('NX-0048219');
    expect(app.claims.map((claim) => claim.status)).toContain('logistics-review');
    expect(app.archiveCandidates[0].eligibility).toBe('archivable');
  });
});
