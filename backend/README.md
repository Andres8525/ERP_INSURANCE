# ERP Insurance API

API REST con Spring Boot 3, Java 21 y PostgreSQL para un ERP de corredora de seguros medicos ACA. Flyway administra el esquema; Hibernate no crea ni modifica tablas automaticamente.

## Inicio local

1. Crea una base PostgreSQL y copia `.env.example` a `.env` (exporta sus valores al entorno de la aplicacion).
2. Define `ENCRYPTION_KEY` como una clave aleatoria de 32 bytes codificada en Base64 y `JWT_SECRET` como minimo 32 bytes aleatorios. Nunca publiques secretos.
3. Ejecuta `mvn spring-boot:run` desde `backend`.
4. Swagger UI: `http://localhost:8080/api/docs`.

Flyway aplica `V1__initial_schema.sql` al arranque. Para crear el primer administrador, define temporalmente `BOOTSTRAP_ADMIN_EMAIL` y `BOOTSTRAP_ADMIN_PASSWORD` (minimo 16 caracteres); el arranque crea la cuenta solo si no existe ningun usuario. Elimina ambos valores despues de provisionarla.

`FRONTEND_ORIGINS` es una lista separada por comas. Los origenes locales Angular/React se permiten por defecto; en produccion configura solo los origenes exactos. Todos los endpoints excepto login, OpenAPI y health requieren JWT Bearer.

## Endpoints

- `POST /api/auth/login`: valida email y contrasena; devuelve token JWT de corta duracion.
- `POST /api/patients`, `GET /api/patients`, `GET /api/patients/{id}`, `PATCH /api/patients/{id}`: stepper de afiliacion. El listado acepta `page`, `size` y `accountStatus`.
- `GET /api/claims/pipeline`: siniestros agrupados por `OPEN`, `IN_REVIEW`, `APPROVED` y `REJECTED`.
- `GET /api/audit/anomalies`: anomalías paginadas; filtros `page`, `size`, `severity`, `resolutionStatus` y `type`. Acceso solo ADMIN/AUDITOR.
- `POST /api/audit/resolve-batch`: `{ "ids": ["uuid"], "action": "ARCHIVAR_DEFINITIVO" | "AUTOCORREGIR" }`. Maximo 100 IDs, transaccion atomica y solo anomalías abiertas. La autocorreccion permite `Patient.email`, `Patient.phone` y `Policy.premiumAmount`; propuestas no permitidas o incompletas revierten el lote entero.

Las respuestas paginadas siguen el formato Spring `Page`: `content`, `totalElements`, `totalPages`, `number`, `size` y `first/last`.

Ejemplos para el frontend:

```json
POST /api/patients
{
	"firstName": "Ana",
	"lastName": "Lopez",
	"email": "ana@example.test",
	"phone": "+1-555-0100",
	"dateOfBirth": "1990-04-12",
	"householdIncome": 42000.00,
	"acaEligibilityStatus": "PENDING",
	"accountStatus": "PENDING"
}
```

```json
POST /api/audit/resolve-batch
{
	"ids": ["a7e93fc7-a6c6-468b-a0bd-f538aeb1dd75"],
	"action": "ARCHIVAR_DEFINITIVO"
}
```

`GET /api/audit/anomalies?page=0&size=25&severity=RED&resolutionStatus=OPEN` devuelve `content` con campos `affectedRecordType`, `affectedRecordId` y sugerencia opcional. Los números de página comienzan en cero, como Spring Data.

Pruebas y build: `mvn test` desde `backend`.

## Seguridad y limites de cumplimiento

Nombres, email, telefono, fecha de nacimiento, ingreso del hogar, numero de poliza, descripcion de siniestro y valores sugeridos se cifran con AES-256-GCM en la capa JPA. El email se busca mediante un HMAC aparte para no guardar un indice reversible. Contrasenas se almacenan con BCrypt; JWT firma HS256. CORS usa allowlist y las rutas de auditoria requieren rol. La clave no se persiste junto a los datos.

Esto es una base tecnica, no una certificacion HIPAA. Produccion requiere TLS, gestion de secretos, control de acceso por tenant/organizacion, auditoria de acceso PHI, retencion, backups probados, respuesta a incidentes, BAAs y revision de seguridad/privacidad. La autorizacion por organizacion y el ciclo completo de auditoria deben completarse antes de almacenar PHI real.