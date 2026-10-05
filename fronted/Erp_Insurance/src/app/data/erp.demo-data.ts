import { ErpDemoData, NavigationItem } from '../models/erp.models';

export const NAVIGATION_ITEMS: NavigationItem[] = [
  { key: 'overview', label: 'Resumen', icon: 'grid', section: 'OPERACIONES' },
  { key: 'audit', label: 'Auditoría IA', icon: 'scan', section: 'OPERACIONES', count: '12' },
  { key: 'claims', label: 'Siniestros', icon: 'file', section: 'OPERACIONES', count: '8' },
  { key: 'enrollment', label: 'Afiliaciones', icon: 'users', section: 'GESTIÓN' },
  { key: 'patient', label: 'Pacientes', icon: 'user', section: 'GESTIÓN' },
  { key: 'cloud', label: 'Optimización nube', icon: 'cloud', section: 'SISTEMA' }
];

export const ERP_DEMO_DATA: ErpDemoData = {
  anomalies: [
    {
      id: 'AN-2481', memberId: 'NX-0048219', member: 'María González', initials: 'MG',
      issue: 'Pólizas solapadas', detail: 'Ambetter · Silver 73', severity: 'critical', source: 'Marketplace', date: 'Hoy, 09:42', confidence: 94,
      comparison: { field: 'Inicio de cobertura', recordA: '01/01/2026', recordB: '02/01/2026', recommendedValue: '02/01/2026', evidence: 'Confirmación de cobertura de la aseguradora' }
    },
    {
      id: 'AN-2479', memberId: 'NX-0031820', member: 'James Rodriguez', initials: 'JR',
      issue: 'Discrepancia de facturación', detail: 'Prima mensual · $428.16', severity: 'critical', source: 'Facturación', date: 'Hoy, 09:18', confidence: 91,
      comparison: { field: 'Prima mensual', recordA: '$428.16', recordB: '$482.16', recommendedValue: '$428.16', evidence: 'Factura vigente de la aseguradora' }
    },
    {
      id: 'AN-2476', memberId: 'NX-0057104', member: 'Lucía Martínez', initials: 'LM',
      issue: 'Posible cliente duplicado', detail: 'Coincidencia del 94%', severity: 'review', source: 'CRM', date: 'Hoy, 08:56', confidence: 94,
      comparison: { field: 'Identidad del miembro', recordA: 'Lucía Martínez · NX-0057104', recordB: 'Lucia M. · NX-0057120', recommendedValue: 'Consolidar en NX-0057104', evidence: 'Coincidencia de fecha de nacimiento y dirección' }
    },
    {
      id: 'AN-2472', memberId: 'NX-0019822', member: 'Robert Chen', initials: 'RC',
      issue: 'Elegibilidad por verificar', detail: 'Ingreso anual · $38,400', severity: 'review', source: 'ACA', date: 'Ayer, 16:34', confidence: 82,
      comparison: { field: 'Ingreso anual', recordA: '$38,400', recordB: '$39,200', recommendedValue: 'Verificar con el solicitante', evidence: 'Solicitud ACA y comprobante de ingresos' }
    },
    {
      id: 'AN-2468', memberId: 'NX-0043190', member: 'Ana Pérez', initials: 'AP',
      issue: 'Póliza sin asignación', detail: 'Oscar Health · EPO', severity: 'review', source: 'Marketplace', date: 'Ayer, 15:12', confidence: 88,
      comparison: { field: 'Asesora asignada', recordA: 'Sin asignación', recordB: 'Cola de nuevas afiliaciones', recommendedValue: 'Asignar a un agente disponible', evidence: 'Regla de distribución de cartera' }
    }
  ],
  claims: [
    { id: 'CL-48291', memberId: 'NX-0048219', memberName: 'María González', service: 'Consulta de especialidad', location: 'Miami', status: 'open', priority: 'high', updatedAt: 'Hoy, 09:16' },
    { id: 'CL-48260', memberId: 'NX-0027401', memberName: 'David Williams', service: 'Traslado de alta', location: 'Tampa', status: 'logistics-review', priority: 'normal', updatedAt: 'Hoy, 09:42' },
    { id: 'CL-48234', memberId: 'NX-0032930', memberName: 'Elena Morales', service: 'Procedimiento ambulatorio', location: 'Miami', status: 'approved', priority: 'normal', updatedAt: 'Ayer, 09:16', amount: 1240 },
    { id: 'CL-48197', memberId: 'NX-0061702', memberName: 'Patricia López', service: 'Servicio fuera de red', location: 'Orlando', status: 'rejected', priority: 'normal', updatedAt: 'Oct 2, 13:21' }
  ],
  patient: {
    memberId: 'NX-0048219', fullName: 'María González', initials: 'MG', status: 'active', enrolledAt: '1 ene 2025',
    city: 'Miami', state: 'Florida', zipCode: '33130', phone: '(305) 555-0142', email: 'maria.gonzalez@example.com',
    assignedBroker: 'Camila Reyes', lastUpdated: 'Hoy, 09:42 AM',
    policy: {
      insurer: 'Ambetter', planName: 'Clear Silver 73', policyId: 'AMB-2847-11903', effectiveDate: 'Ene 1, 2026', renewalDate: 'Dic 31, 2026',
      monthlyPremium: 428.16, individualDeductible: 4500, deductibleMet: 1800
    },
    billingRecords: [
      { id: 'BILL-1026', date: 'Oct 1, 2026', description: 'Prima mensual', amount: 428.16, status: 'paid' },
      { id: 'BILL-0926', date: 'Sep 1, 2026', description: 'Prima mensual', amount: 428.16, status: 'paid' },
      { id: 'BILL-0826', date: 'Ago 1, 2026', description: 'Prima mensual', amount: 428.16, status: 'paid' }
    ],
    timeline: [
      { id: 'EV-48291', date: 'Hoy, 09:16', title: 'Siniestro abierto · CL-48291', description: 'Consulta de especialidad, Miami. Pendiente de coordinación.', category: 'claim', status: 'Requiere seguimiento' },
      { id: 'EV-TRANS-0928', date: 'Sep 28, 14:30', title: 'Transporte médico completado', description: 'Traslado de ida y vuelta · Coral Gables Medical Center', category: 'service', status: 'Completado' },
      { id: 'EV-CARE-0912', date: 'Sep 12, 10:05', title: 'Servicio de coordinación asignado', description: 'Coordinadora de cuidados: Andrea Torres.', category: 'coordination' },
      { id: 'EV-BILL-0901', date: 'Sep 1, 08:42', title: 'Pago de prima procesado', description: 'Pago mensual recibido · $428.16', category: 'billing', status: 'Confirmado' }
    ]
  },
  archiveCandidates: [
    { id: 'NX-001274', description: 'Archivo de póliza', documentType: 'Póliza ACA', lastActivity: 'Mar 12, 2018', sizeMb: 248, eligibility: 'archivable' },
    { id: 'NX-000981', description: 'Comunicaciones', documentType: 'Correspondencia', lastActivity: 'Ago 4, 2017', sizeMb: 86, eligibility: 'legal-review' }
  ]
};