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
    expect(compiled.querySelector('[aria-label="Navegación principal"]')).toBeTruthy();
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
});
