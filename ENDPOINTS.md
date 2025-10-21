# README de Endpoints — VG MS Students (v6.0)

Breve, organizado y directo. Usa esto como referencia rápida para Postman o curl.

Base URL: `http://localhost:8102/api/v1`

Headers obligatorios (endpoints protegidos)
- `X-User-Id`: UUID del usuario
- `X-User-Roles`: Roles (ej: `SECRETARY`, `TEACHER`)
- `X-Institution-Id`: UUID de la institución (obligatorio para `SECRETARY` y `TEACHER`)

Formato JSON: usar camelCase (ej. `firstName`). No incluyas `institutionId` en el body: se toma del header.

Índice rápido
- SECRETARY (gestión completa)
- ENROLLMENTS (matrículas)
- TEACHER (consultas/observaciones)
- Validaciones públicas

---

SECRETARY — gestión de estudiantes

1) Crear estudiante
- POST /students/secretary/create
- Headers: `X-User-Id`, `X-User-Roles` (contiene `SECRETARY`), `X-Institution-Id`
- Body (mínimo recomendado):
  {
    "firstName": "Carlos",
    "lastName": "Pérez",
    "documentType": "DNI",
    "documentNumber": "99887726",
    "birthDate": "2015-01-10",
    "gender": "MALE",
    "address": "Av. Siempre Viva 123",
    "phone": "955666727",
    "parentName": "Ana Pérez",
    "parentPhone": "955111222",
    "parentEmail": "ana.perez@example.com"
  }

Respuesta: ApiResponse con `student` (objeto) y `message`.

2) Listar estudiantes de la institución
- GET /students/secretary
- Headers: `X-User-Id`, `X-User-Roles` (SECRETARY), `X-Institution-Id`

3) Actualizar estudiante
- PUT /students/secretary/update/{studentId}
- Headers: `X-User-Id`, `X-User-Roles` (SECRETARY), `X-Institution-Id`
- Body: solo campos a actualizar (camelCase).

---

MATRÍCULAS — SECRETARY

1) Crear matrícula
- POST /enrollments/secretary/create
- Headers: `X-User-Id`, `X-User-Roles` (SECRETARY), `X-Institution-Id`
- Body ejemplo:
  {
    "studentId": "<student-id>",
    "classroomId": "classroom-uuid-001",
    "enrollmentDate": "2025-03-01",
    "enrollmentType": "REGULAR"
  }

La API genera `qrCode` automáticamente y lo guarda en la matrícula.

2) Listar matrículas por institución
- GET /enrollments/secretary
- Headers: `X-User-Id`, `X-User-Roles` (SECRETARY), `X-Institution-Id`

3) Actualizar matrícula
- PUT /enrollments/secretary/update/{enrollmentId}
- Headers iguales a los anteriores.

4) Obtener QR (simplificado)
- GET /enrollments/secretary/qr/{enrollmentId}

---

TEACHER — consultas y observaciones

1) Mis estudiantes asignados
- GET /students/teacher/my-students
- Headers: `X-User-Id`, `X-User-Roles` (TEACHER), `X-Institution-Id`

2) Mis matrículas asignadas
- GET /enrollments/teacher/my-enrollments

3) Actualizar observaciones (conceptual)
- PUT /enrollments/teacher/observations/{enrollmentId}
- Body: { "observations": "Texto" }

---

VALIDACIONES PÚBLICAS (sin auth)

1) Validar estudiante
- GET /validate-students/{studentId}
- Respuesta: 200 si existe, 404 si no.

2) Validar matrícula
- GET /validate-enrollments/{enrollmentId}

3) Validar QR
- GET /validate-qr/{qrCode}

---

Ejemplos rápidos (curl)

Crear estudiante (ejemplo):
```bash
curl -X POST "http://localhost:8102/api/v1/students/secretary/create" \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 123e4567-e89b-12d3-a456-426614174000" \
  -H "X-User-Roles: SECRETARY" \
  -H "X-Institution-Id: 96960392-1e5f-4e66-afc9-4b5bcd771d9f" \
  -d '{ "firstName": "Carlos", "lastName": "Pérez", "documentType": "DNI", "documentNumber": "99887726", "birthDate": "2015-01-10" }'
```

Comprobación rápida (debug)
- Si no ves el documento en Mongo:
  1) Revisa los logs del backend (nivel DEBUG está activado en `application.yml`).
  2) Confirma que tu IP está permitida en MongoDB Atlas.
  3) Revisa si hay errores por índice único (`documentNumber`).

Errores comunes y soluciones
- 401/403 -> falta o rol incorrecto en headers.
- 400 -> payload inválido (revisa JSON y validaciones `@Valid`).
- 500 con excepción Mongo -> revisar URI/permiso IP y credenciales.

Si quieres, genero una colección Postman exportable y ejemplos más detallados para copiar/pegar.
