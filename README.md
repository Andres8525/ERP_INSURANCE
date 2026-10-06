# ERP de Seguros Médicos

Base de una aplicación ERP para operaciones de una corredora de seguros médicos ACA/Obamacare. El repositorio contiene una interfaz Angular con datos de demostración y un backend REST independiente en Spring Boot conectado a PostgreSQL.

## Estado actual

- **Frontend:** Angular 21 en `fronted/Erp_Insurance`. Incluye vistas de resumen, auditoría de anomalías, pipeline de siniestros, afiliación, perfil del paciente y archivo en la nube. En este momento trabaja con `ERP_DEMO_DATA` y estado local de Angular; todavía no consume los endpoints del backend.
- **Backend:** Spring Boot 3, Java 21 y Maven en `backend/`. Incluye entidades JPA para pacientes, pólizas, siniestros, servicios logísticos, usuarios y anomalías; migración PostgreSQL con Flyway; autenticación JWT; validación de DTOs; CORS configurable; paginación y filtros para pacientes/anomalías.
- **Seguridad inicial:** BCrypt para contraseñas, JWT Bearer, cifrado AES-256-GCM de datos personales/sensibles y filtros de auditoría restringidos a roles ADMIN/AUDITOR. La base no constituye por sí sola una certificación HIPAA.

## Estructura

```text
backend/
  pom.xml
  src/main/java/com/erp/insurance/
    auth/          Login, usuarios y bootstrap opcional de administrador
    audit/         Anomalías, filtros y resolución masiva
    claims/        Siniestros y pipeline Kanban
    patients/      Afiliados y endpoints de pacientes
    policies/      Entidad y persistencia de pólizas
    security/      JWT, CORS, cifrado y hash de búsqueda de email
  src/main/resources/
    application.yml
    db/migration/  Esquema inicial PostgreSQL
  src/test/        Pruebas del cifrado
fronted/
  Erp_Insurance/  Aplicación Angular y datos demo
```

## Requisitos

- Node.js compatible con Angular CLI y npm.
- JDK 21.
- Maven 3.8 o posterior.
- PostgreSQL para ejecutar el backend.

## Ejecutar el frontend

```powershell
cd fronted/Erp_Insurance
npm install
npm start
```

La interfaz queda disponible en `http://localhost:4200`.

## Ejecutar el backend

1. Crea la base `erp_insurance` en PostgreSQL.
2. Configura las variables de entorno descritas en [backend/.env.example](backend/.env.example). Spring no carga automáticamente un archivo `.env`; exporta los valores en la terminal o configúralos en el entorno de ejecución.
3. Genera secretos únicos. `ENCRYPTION_KEY` debe ser Base64 de exactamente 32 bytes; `JWT_SECRET` debe tener al menos 32 bytes. No uses los valores de ejemplo en producción.
4. Inicia la API:

```powershell
cd backend
mvn spring-boot:run
```

Flyway aplica las migraciones al iniciar. La API escucha en `http://localhost:8080`; Swagger UI está en `/api/docs`. El origen Angular `http://localhost:4200` está permitido por defecto y se puede cambiar con `FRONTEND_ORIGINS`.

El primer administrador se puede crear al iniciar definiendo temporalmente `BOOTSTRAP_ADMIN_EMAIL` y `BOOTSTRAP_ADMIN_PASSWORD` (mínimo 16 caracteres). El bootstrap solo crea una cuenta cuando no hay usuarios; elimina esas variables después.

## API disponible

Todos los endpoints requieren `Authorization: Bearer <token>`, excepto login, OpenAPI y health.

| Método | Endpoint | Propósito |
| --- | --- | --- |
| `POST` | `/api/auth/login` | Obtener un JWT con email y contraseña |
| `POST` | `/api/patients` | Registrar un paciente/afiliado |
| `GET` | `/api/patients?page=0&size=25` | Listar pacientes paginados; permite filtrar por `accountStatus` |
| `GET` | `/api/patients/{id}` | Consultar paciente |
| `PATCH` | `/api/patients/{id}` | Actualizar datos del paciente |
| `GET` | `/api/claims/pipeline` | Devolver siniestros agrupados por estado |
| `GET` | `/api/audit/anomalies?page=0&size=25` | Listar anomalías paginadas; filtros por severidad, tipo y estado |
| `POST` | `/api/audit/resolve-batch` | Archivar o autocorregir anomalías en una transacción; solo ADMIN/AUDITOR |

Spring Data pagina desde cero (`page=0`). La respuesta incluye `content`, `totalElements`, `totalPages`, `number` y `size`. La autocorrección está limitada a `Patient.email`, `Patient.phone` y `Policy.premiumAmount`; el lote acepta hasta 100 IDs.

Para más detalles de contratos, configuración y seguridad, consulta [backend/README.md](backend/README.md).

## Comprobaciones

Backend:

```powershell
cd backend
mvn test
```

Frontend:

```powershell
cd fronted/Erp_Insurance
npm test
npm run build
```

## Pendiente antes de producción

- Conectar las vistas Angular a los servicios REST y adaptar sus modelos al contrato del backend.
- Definir autorización y aislamiento por organización/tenant.
- Completar auditoría de accesos a PHI, TLS, almacenamiento gestionado de secretos, retención, backups, respuesta a incidentes y revisión de cumplimiento HIPAA.
- Añadir pruebas de integración con PostgreSQL y pruebas de los flujos completos de API.