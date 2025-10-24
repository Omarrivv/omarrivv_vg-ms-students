# API REST - Sistema de Gestión de Estudiantes

## Descripción

API REST para la gestión de estudiantes y matrículas de una institución educativa. Desarrollada con Spring Boot WebFlux, MongoDB Reactivo y arquitectura hexagonal.

## Base URL

```
http://localhost:8102/api/v1
```

## Autenticación y Headers

Todos los endpoints requieren los siguientes headers:

| Header               | Descripción                         | Valores                       |
| -------------------- | ------------------------------------ | ----------------------------- |
| `X-User-Id`        | ID del usuario que hace la petición | UUID                          |
| `X-Institution-Id` | ID de la institución                | UUID                          |
| `X-User-Roles`     | Rol del usuario                      | SECRETARY, TEACHER, AUXILIARY |

## Endpoints

### 🎓 GESTIÓN DE ESTUDIANTES

#### **POST** `/students/secretary/create`

Crear un nuevo estudiante (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
Content-Type: application/json
```

**Body:**

```json
{
  "firstName": "Juan Carlos",
  "lastName": "Pérez García",
  "documentType": "DNI",
  "documentNumber": "12345678",
  "birthDate": "2010-05-15",
  "gender": "MALE",
  "address": "Av. Los Incas 123, Lima",
  "phone": "987654321",
  "parentPhone": "987654322",
  "parentEmail": "padre@email.com",
  "parentName": "Carlos Pérez"
}
```

**Response:**

```json
{
  "success": true,
  "message": "Student created successfully",
  "data": {
    "student": {
      "id": "507f1f77bcf86cd799439011",
      "firstName": "Juan Carlos",
      "lastName": "Pérez García",
      "documentType": "DNI",
      "documentNumber": "12345678",
      "birthDate": "2010-05-15",
      "gender": "MALE",
      "address": "Av. Los Incas 123, Lima",
      "phone": "987654321",
      "parentPhone": "987654322",
      "parentEmail": "padre@email.com",
      "parentName": "Carlos Pérez",
      "status": "ACTIVE",
      "createdAt": "2024-10-23T10:30:00Z"
    }
  }
}
```

#### **GET** `/students/secretary`

Obtener todos los estudiantes de la institución (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

**Response:**

```json
{
  "success": true,
  "message": "Students retrieved successfully",
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "firstName": "Juan Carlos",
      "lastName": "Pérez García",
      "documentNumber": "12345678",
      "status": "ACTIVE"
    }
  ]
}
```

#### **PUT** `/students/secretary/update/{studentId}`

Actualizar datos de un estudiante (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
Content-Type: application/json
```

**Body:**

```json
{
  "firstName": "Juan Carlos",
  "lastName": "Pérez García",
  "phone": "987654321",
  "address": "Nueva dirección 456",
  "parentPhone": "987654322",
  "parentEmail": "nuevo_padre@email.com",
  "parentName": "Carlos Pérez"
}
```

#### **GET** `/students/secretary/unenrolled`

Obtener estudiantes no matriculados (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

#### **POST** `/students/secretary/bulk-create`

Crear múltiples estudiantes (hasta 1000) (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
Content-Type: application/json
```

**Body:**

```json
{
  "students": [
    {
      "firstName": "Estudiante 1",
      "lastName": "Apellido 1",
      "documentType": "DNI",
      "documentNumber": "11111111",
      "birthDate": "2010-01-01",
      "gender": "MALE",
      "address":"Av. Siempre Viva 123",
      "phone":"928188288",
      "parentName":"PADRE estudiante",
      "parentPhone":"928838833",
      "parentEmail":"padreestudiante@gmail.com"
    },
    {
      "firstName": "Estudiante 2",
      "lastName": "Apellido 2",
      "documentType": "DNI",
      "documentNumber": "22222222",
      "birthDate": "2010-02-02",
      "gender": "FEMALE",
      "address":"lima norte",
      "phone":"980929299",
      "parentName":"Padre de una estudiante",
      "parentPhone":"932148998",
      "parentEmail":"madreestudiante@madre.com"
    }
  ]
}
```

**Response:**

```json
{
  "success": true,
  "message": "Bulk student creation completed",
  "data": {
    "totalRequested": 2,
    "successfullyCreated": 2,
    "errors": 0,
    "students": [...]
  }
}
```

#### **GET** `/students/secretary/search?query={searchTerm}`

Buscar estudiantes por nombre, apellido o documento (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

**Query Parameters:**

- `query`: Término de búsqueda

#### **GET** `/students/secretary/by-grade/{grade}`

Obtener estudiantes por grado (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

#### **GET** `/students/secretary/by-status/{status}`

Obtener estudiantes por estado (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

**Path Parameters:**

- `status`: ACTIVE, INACTIVE

#### **GET** `/students/secretary/statistics`

Obtener estadísticas de estudiantes (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

**Response:**

```json
{
  "success": true,
  "message": "Student statistics retrieved successfully",
  "data": {
    "totalStudents": 150,
    "activeStudents": 140,
    "inactiveStudents": 10,
    "unenrolledStudents": 15
  }
}
```

#### **PUT** `/students/secretary/deactivate/{studentId}`

Desactivar un estudiante (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

#### **PUT** `/students/secretary/activate/{studentId}`

Activar un estudiante (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

#### **GET** `/students/teacher/my-students`

Obtener estudiantes asignados al profesor (solo profesores)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: TEACHER
```

#### **GET** `/students/auxiliary`

Obtener estudiantes para auxiliares (solo auxiliares)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: AUXILIARY
```

### 📋 GESTIÓN DE MATRÍCULAS

#### **POST** `/enrollments/secretary/create`

Crear una nueva matrícula (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
Content-Type: application/json
```

**Body:**

```json
{
  "studentId": "507f1f77bcf86cd799439011",
  "classroomId": "507f1f77bcf86cd799439022",
  "enrollmentDate": "2024-01-15",
  "enrollmentType": "REGULAR"
}
```

**Response:**

```json
{
  "success": true,
  "message": "Enrollment created successfully",
  "data": {
    "enrollment": {
      "id": "507f1f77bcf86cd799439033",
      "studentId": "507f1f77bcf86cd799439011",
      "classroomId": "507f1f77bcf86cd799439022",
      "enrollmentDate": "2024-01-15",
      "enrollmentType": "REGULAR",
      "status": "ACTIVE",
      "qrCode": "QR123456789",
      "createdAt": "2024-10-23T10:30:00Z"
    }
  }
}
```

#### **GET** `/enrollments/secretary`

Obtener todas las matrículas de la institución (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

#### **PUT** `/enrollments/secretary/update/{enrollmentId}`

Actualizar una matrícula (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
Content-Type: application/json
```

**Body:**

```json
{
  "classroomId": "507f1f77bcf86cd799439022",
  "enrollmentType": "TRANSFER",
  "status": "ACTIVE"
}
```

#### **GET** `/enrollments/secretary/by-classroom/{classroomId}`

Obtener matrículas por aula (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

#### **POST** `/enrollments/secretary/bulk-create`

Crear múltiples matrículas (hasta 500) (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
Content-Type: application/json
```

**Body:**

```json
{
  "enrollments": [
    {
      "studentId": "507f1f77bcf86cd799439011",
      "classroomId": "507f1f77bcf86cd799439022",
      "enrollmentDate": "2024-01-15",
      "enrollmentType": "REGULAR"
    }
  ]
}
```

#### **GET** `/enrollments/secretary/by-status/{status}`

Obtener matrículas por estado (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

**Path Parameters:**

- `status`: ACTIVE, COMPLETED, TRANSFER, RETIRED

#### **GET** `/enrollments/secretary/statistics`

Obtener estadísticas de matrículas (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

**Response:**

```json
{
  "success": true,
  "message": "Enrollment statistics retrieved successfully",
  "data": {
    "totalEnrollments": 120,
    "activeEnrollments": 110,
    "retiredEnrollments": 5,
    "transferredEnrollments": 3,
    "completedEnrollments": 2
  }
}
```

#### **PUT** `/enrollments/secretary/transfer/{enrollmentId}`

Transferir estudiante a otra aula (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
Content-Type: application/json
```

**Body:**

```json
{
  "newClassroomId": "507f1f77bcf86cd799439044",
  "reason": "Cambio de nivel académico"
}
```

#### **GET** `/enrollments/secretary/by-student/{studentId}`

Obtener matrículas de un estudiante específico (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

#### **PUT** `/enrollments/secretary/cancel/{enrollmentId}`

Cancelar una matrícula (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
Content-Type: application/json
```

**Body:**

```json
{
  "reason": "Retiro voluntario del estudiante"
}
```

#### **GET** `/enrollments/secretary/qr/{enrollmentId}`

Obtener código QR de una matrícula (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

**Response:**

```json
{
  "success": true,
  "message": "QR retrieved successfully",
  "data": {
    "qrCode": "QR123456789"
  }
}
```

#### **GET** `/enrollments/teacher/my-enrollments`

Obtener matrículas asignadas al profesor (solo profesores)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: TEACHER
```

#### **PUT** `/enrollments/teacher/observations/{enrollmentId}`

Actualizar observaciones de una matrícula (solo profesores)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: TEACHER
Content-Type: application/json
```

**Body:**

```json
{
  "observations": "Estudiante destacado en matemáticas"
}
```

#### **GET** `/enrollments/teacher/by-student/{studentId}`

Obtener matrículas de un estudiante (solo profesores)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: TEACHER
```

#### **GET** `/enrollments/auxiliary/by-classroom/{classroomId}`

Obtener matrículas por aula para auxiliares (solo auxiliares)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: AUXILIARY
```

#### **GET** `/enrollments/auxiliary/by-student/{studentId}`

Obtener matrículas de un estudiante para auxiliares (solo auxiliares)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: AUXILIARY
```

### 📊 REPORTES Y ESTADÍSTICAS

#### **GET** `/reports/secretary/dashboard`

Obtener estadísticas del dashboard (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

**Response:**

```json
{
  "success": true,
  "message": "Dashboard statistics retrieved successfully",
  "data": {
    "studentStats": {
      "totalStudents": 150,
      "activeStudents": 140,
      "inactiveStudents": 10,
      "unenrolledStudents": 15
    },
    "enrollmentStats": {
      "totalEnrollments": 120,
      "activeEnrollments": 110,
      "retiredEnrollments": 5,
      "transferredEnrollments": 3,
      "completedEnrollments": 2
    }
  }
}
```

#### **GET** `/reports/secretary/overview`

Obtener resumen general de la institución (solo secretarias)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: SECRETARY
```

**Response:**

```json
{
  "success": true,
  "message": "Institution overview retrieved successfully",
  "data": {
    "totalStudents": 150,
    "unenrolledStudents": 15,
    "totalEnrollments": 120,
    "activeEnrollments": 110,
    "enrollmentPercentage": 80.0,
    "unenrolledPercentage": 10.0
  }
}
```

#### **GET** `/reports/teacher/my-stats`

Obtener estadísticas del profesor (solo profesores)

**Headers:**

```
X-User-Id: {userId}
X-Institution-Id: {institutionId}
X-User-Role: TEACHER
```

**Response:**

```json
{
  "success": true,
  "message": "Teacher statistics retrieved successfully",
  "data": {
    "myStudents": 25,
    "myEnrollments": 25
  }
}
```

### 🔍 ENDPOINTS GENERALES

#### **GET** `/students`

Obtener todos los estudiantes (sin filtros)

#### **GET** `/students/{id}`

Obtener estudiante por ID

## Códigos de Respuesta

| Código | Descripción                          |
| ------- | ------------------------------------- |
| 200     | OK - Solicitud exitosa                |
| 201     | Created - Recurso creado exitosamente |
| 400     | Bad Request - Error en la solicitud   |
| 401     | Unauthorized - No autorizado          |
| 403     | Forbidden - Sin permisos              |
| 404     | Not Found - Recurso no encontrado     |
| 500     | Internal Server Error - Error interno |

## Estructura de Respuesta de Error

```json
{
  "success": false,
  "message": "Error description",
  "timestamp": "2024-10-23T10:30:00Z",
  "path": "/api/v1/students/secretary/create"
}
```

## Notas Importantes

1. **Roles de Usuario:**

   - `SECRETARY`: Acceso completo a gestión de estudiantes y matrículas
   - `TEACHER`: Acceso a sus estudiantes y matrículas asignadas
   - `AUXILIARY`: Acceso de solo lectura a estudiantes y matrículas
2. **Validaciones:**

   - Los números de documento deben ser únicos por institución
   - Las fechas deben estar en formato ISO (YYYY-MM-DD)
   - Los bulk inserts tienen límites (1000 estudiantes, 500 matrículas)
3. **Seguridad:**

   - Todos los endpoints validan que el usuario pertenezca a la institución
   - Los roles se validan en cada endpoint
   - Los recursos se filtran por institución automáticamente
4. **Códigos QR:**

   - Se generan automáticamente para cada matrícula
   - Son únicos en todo el sistema
   - Se pueden utilizar para identificación rápida

## Ejemplos de Uso

### Flujo típico de creación de estudiante y matrícula:

1. **Crear estudiante:**

```bash
curl -X POST http://localhost:8080/api/v1/students/secretary/create \
  -H "X-User-Id: user123" \
  -H "X-Institution-Id: inst456" \
  -H "X-User-Role: SECRETARY" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "Pérez",
    "documentType": "DNI",
    "documentNumber": "12345678",
    "birthDate": "2010-05-15",
    "gender": "MALE"
  }'
```

2. **Matricular estudiante:**

```bash
curl -X POST http://localhost:8080/api/v1/enrollments/secretary/create \
  -H "X-User-Id: user123" \
  -H "X-Institution-Id: inst456" \
  -H "X-User-Role: SECRETARY" \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "507f1f77bcf86cd799439011",
    "classroomId": "507f1f77bcf86cd799439022",
    "enrollmentDate": "2024-01-15",
    "enrollmentType": "REGULAR"
  }'
```

### Búsqueda y filtros:

```bash
# Buscar estudiantes
curl -X GET "http://localhost:8080/api/v1/students/secretary/search?query=Juan" \
  -H "X-User-Id: user123" \
  -H "X-Institution-Id: inst456" \
  -H "X-User-Role: SECRETARY"

# Obtener estadísticas
curl -X GET http://localhost:8080/api/v1/reports/secretary/dashboard \
  -H "X-User-Id: user123" \
  -H "X-Institution-Id: inst456" \
  -H "X-User-Role: SECRETARY"
```
