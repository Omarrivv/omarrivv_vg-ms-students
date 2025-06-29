# Microservicio de Estudiantes (MSV-Students)

Este microservicio gestiona la información de estudiantes y sus matrículas en aulas para Valle Grande. Implementado con Spring WebFlux para manejo reactivo de datos.

## Estructura del Proyecto

```
src/main/java/pe/edu/vallegrande/msvstudents/
├── MsvStudentsApplication.java          # Punto de entrada de la aplicación Spring Boot
├── application/                         # Capa de aplicación
│   └── service/                        # Servicios de la aplicación
│       ├── ClassroomStudentService     # Interfaz del servicio de matrículas
│       ├── StudentService              # Interfaz del servicio de estudiantes
│       └── impl/                       # Implementaciones de servicios
│           ├── ClassroomStudentServiceImpl  # Implementación de matrículas
│           └── StudentServiceImpl           # Implementación de estudiantes
├── domain/                             # Capa de dominio
│   ├── enums/                         # Enumeraciones del dominio
│   │   ├── DocumentType               # Tipos de documento
│   │   ├── Gender                     # Géneros
│   │   └── Status                     # Estados de registros
│   └── model/                         # Modelos de dominio
│       ├── ClassroomStudent           # Entidad de matrícula
│       └── Student                    # Entidad de estudiante
└── infrastructure/                     # Capa de infraestructura
    ├── config/                        # Configuraciones
    │   └── WebConfig                  # Configuración CORS y Web
    ├── dto/                          # Objetos de transferencia de datos
    │   ├── request/                  # DTOs para peticiones
    │   │   ├── ClassroomStudentRequest
    │   │   └── StudentRequest
    │   └── response/                 # DTOs para respuestas
    │       ├── ClassroomStudentResponse
    │       └── StudentResponse
    ├── exception/                    # Manejo de excepciones
    │   └── GlobalExceptionHandler    # Manejador global de excepciones
    ├── repository/                   # Repositorios
    │   ├── ClassroomStudentRepository
    │   ├── StudentRepository
    │   └── impl/                     # Implementaciones de repositorios
    │       ├── ClassroomStudentRepositoryImpl
    │       └── StudentRepositoryImpl
    └── rest/                         # Controladores REST
        ├── ClassroomStudentController
        └── StudentController
```

## Documentación de API

### 1. API de Estudiantes
Base URL: `/api/v1/students`

#### Endpoints Principales

| Método | Endpoint | Descripción | Código de Respuesta |
|--------|----------|-------------|-------------------|
| GET | `/` | Obtener todos los estudiantes | 200 |
| GET | `/{id}` | Obtener estudiante por ID | 200, 404 |
| POST | `/` | Crear nuevo estudiante | 201 |
| PUT | `/{id}` | Actualizar estudiante | 200, 404 |
| DELETE | `/{id}` | Desactivar estudiante | 204 |
| PUT | `/{id}/restore` | Restaurar estudiante | 200, 404 |

#### Endpoints de Filtrado

| Método | Endpoint | Descripción | Código de Respuesta |
|--------|----------|-------------|-------------------|
| GET | `/institution/{institutionId}` | Filtrar por institución | 200 |
| GET | `/status/{status}` | Filtrar por estado | 200 |
| GET | `/gender/{gender}` | Filtrar por género | 200 |

#### Ejemplo de Estudiante (POST/PUT)
```json
{
    "institutionId": "2",
    "firstName": "Omar code",
    "lastName": "Rivera Rosas",
    "documentType": "DNI",
    "documentNumber": "09092929",
    "gender": "M",
    "birthDate": "2010-01-01",
    "address": "EU. Rio de janeiro",
    "phone": "900800700",
    "email": "codex@mail.com",
    "nameQr": "Omar code_Rivera Rosas_09235464"
}
```

### 2. API de Matrículas
Base URL: `/api/v1/classroom-students`

#### Endpoints Principales

| Método | Endpoint | Descripción | Código de Respuesta |
|--------|----------|-------------|-------------------|
| GET | `/` | Obtener todas las matrículas | 200 |
| GET | `/{id}` | Obtener matrícula por ID | 200, 404 |
| POST | `/` | Crear nueva matrícula | 201 |
| PUT | `/{id}` | Actualizar matrícula | 200, 404 |
| DELETE | `/{id}` | Desactivar matrícula | 204 |
| PUT | `/{id}/restore` | Restaurar matrícula | 200, 404 |

#### Endpoints de Filtrado

| Método | Endpoint | Descripción | Código de Respuesta |
|--------|----------|-------------|-------------------|
| GET | `/student/{studentId}` | Filtrar por estudiante | 200 |
| GET | `/classroom/{classroomId}` | Filtrar por aula | 200 |
| GET | `/status/{status}` | Filtrar por estado | 200 |
| GET | `/year/{year}` | Filtrar por año | 200 |
| GET | `/period/{period}` | Filtrar por periodo | 200 |
| GET | `/year/{year}/period/{period}` | Filtrar por año y periodo | 200 |

#### Ejemplo de Matrícula (POST/PUT)
```json
{
    "classroomId": "3",
    "studentId": "682fdf276966753b037b8afb",
    "enrollmentDate": "2024-01-01",
    "status": "A",
    "enrollmentYear": "2024",
    "enrollmentPeriod": "2024-1"
}
```

### 3. Valores Permitidos

#### Estados (Status)
- `A`: Activo
- `I`: Inactivo

#### Géneros (Gender)
- `M`: Masculino
- `F`: Femenino

#### Tipos de Documento (DocumentType)
- `DNI`: Documento Nacional de Identidad
- `CARNET DE EXTRANJERIA`: Carnet de Extranjería

### 4. Respuestas de Error

#### Error 404 - Recurso no encontrado
```json
{
    "message": "Estudiante no encontrado con ID: 682fdf276966753b037b8afb"
}
```

#### Error 400 - Error de validación
```json
{
    "message": "Error de validación",
    "errors": {
        "firstName": "El nombre es requerido",
        "documentNumber": "El número de documento debe tener 8 dígitos"
    }
}
```

#### Error 500 - Error interno del servidor
```json
{
    "message": "Error interno del servidor"
}
```

### 5. Notas Importantes

1. **Matrículas Activas**:
   - Un estudiante solo puede tener una matrícula activa a la vez
   - Al intentar crear una nueva matrícula para un estudiante con una activa, se retornará un error

2. **Eliminación Lógica**:
   - Los endpoints DELETE realizan una eliminación lógica (cambio de estado a 'I')
   - Use el endpoint restore para reactivar registros

3. **CORS**:
   - La API permite solicitudes desde cualquier origen (*)
   - Métodos permitidos: GET, POST, PUT, DELETE, OPTIONS
   - Headers permitidos: Todos (*)

## Tecnologías Utilizadas

- Spring Boot
- Spring WebFlux (Programación Reactiva)
- MongoDB
- Lombok
- Java 17

## Endpoints

### Estudiantes (`/api/v1/students`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Obtener todos los estudiantes |
| GET | `/{id}` | Obtener estudiante por ID |
| POST | `/` | Crear nuevo estudiante |
| PUT | `/{id}` | Actualizar estudiante existente |
| DELETE | `/{id}` | Eliminar estudiante (cambio de estado a inactivo) |
| GET | `/institution/{institutionId}` | Obtener estudiantes por ID de institución |
| GET | `/status/{status}` | Obtener estudiantes por estado |
| GET | `/gender/{gender}` | Obtener estudiantes por género |
| PUT | `/{id}/restore` | Restaurar estudiante eliminado |

### Matrículas (`/api/v1/classroom-students`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Obtener todas las matrículas |
| GET | `/{id}` | Obtener matrícula por ID |
| POST | `/` | Crear nueva matrícula |
| PUT | `/{id}` | Actualizar matrícula existente |
| DELETE | `/{id}` | Eliminar matrícula (cambio de estado a inactivo) |
| GET | `/student/{studentId}` | Obtener matrículas por ID de estudiante |
| GET | `/classroom/{classroomId}` | Obtener matrículas por ID de aula |
| GET | `/status/{status}` | Obtener matrículas por estado |
| GET | `/year/{year}` | Obtener matrículas por año |
| GET | `/period/{period}` | Obtener matrículas por periodo |
| GET | `/year/{year}/period/{period}` | Obtener matrículas por año y periodo |
| PUT | `/{id}/restore` | Restaurar matrícula eliminada |

## Modelos de Datos

### StudentRequest
```json
{
    "institutionId": "string",
    "firstName": "string",
    "lastName": "string",
    "documentType": "DNI | PASAPORTE | CARNET DE EXTRANJERIA | OTROS",
    "documentNumber": "string",
    "gender": "M | F",
    "birthDate": "YYYY-MM-DD",
    "address": "string",
    "phone": "string",
    "email": "string",
    "nameQr": "string"
}
```

### ClassroomStudentRequest
```json
{
    "classroomId": "string",
    "studentId": "string",
    "enrollmentDate": "2024-01-01",
    "status": "A",
    "enrollmentYear": "string",
    "enrollmentPeriod": "string"
}
```

## Enumeraciones

### Estado (Status)
- `ACTIVE`: "A" - Registro activo
- `INACTIVE`: "I" - Registro inactivo

### Género (Gender)
- `MALE`: "M" - Masculino
- `FEMALE`: "F" - Femenino

### Tipo de Documento (DocumentType)
- `DNI`: "DNI" - Documento Nacional de Identidad
- `PASSPORT`: "PASAPORTE" - Pasaporte
- `FOREIGN_CARD`: "CARNET DE EXTRANJERIA" - Carnet de Extranjería
- `OTHERS`: "OTROS" - Otros tipos de documento

## Configuración CORS

El microservicio está configurado para permitir solicitudes CORS desde cualquier origen (*) con los métodos HTTP GET, POST, PUT, DELETE y OPTIONS.

## Manejo de Errores

El sistema incluye un manejador global de excepciones que proporciona respuestas consistentes para los siguientes casos:

- Recurso no encontrado (404)
- Errores de validación (400)
- Errores internos del servidor (500)

## Estado de Registros

Los registros (estudiantes y matrículas) utilizan los siguientes estados:

- "A": Activo
- "I": Inactivo

## Control de Versiones

### Repositorios Configurados
```bash
# GitHub
github  https://github.com/Omarrivv/vg-ms-students.git

# GitLab
origin  https://gitlab.com/vallegrande/as231s5_prs2/vg-ms-students.git
```

### Ramas Disponibles
- `main`: Rama principal
- `temp-main`: Rama temporal
- `vg-ms-students`: Rama de desarrollo

### Pasos para Pushear Cambios

1. **Verificar la Rama Actual**:
```bash
git branch
# Deberías estar en la rama vg-ms-students
```

2. **Verificar Cambios Pendientes**:
```bash
git status
```

3. **Agregar y Commitear Cambios**:
```bash
# Agregar todos los cambios
git add .

# Crear commit
git commit -m "descripción de tus cambios"
```

4. **Push a GitLab (origin)**:
```bash
# Actualizar cambios remotos primero
git pull origin vg-ms-students

# Subir cambios
git push origin vg-ms-students
```

5. **Push a GitHub**:
```bash
# Actualizar cambios remotos primero
git pull github vg-ms-students

# Subir cambios
git push github vg-ms-students
```

### Solución de Problemas

1. **Si hay conflictos en el pull**:
```bash
# 1. Resolver conflictos en los archivos marcados
# 2. Agregar los archivos resueltos
git add .

# 3. Completar el merge
git commit -m "resolver conflictos de merge"

# 4. Continuar con el push
git push origin vg-ms-students
git push github vg-ms-students
```

2. **Si necesitas cambiar de rama**:
```bash
git checkout nombre-rama
```

3. **Si necesitas descartar cambios locales**:
```bash
git checkout -- archivo-modificado
# o para todos los archivos
git checkout -- .
```

### Recomendaciones

1. **Siempre hacer pull antes de empezar a trabajar**:
```bash
git pull origin vg-ms-students
git pull github vg-ms-students
```

2. **Verificar el estado antes de commits**:
```bash
git status
git diff
```

3. **En caso de duda, verificar la configuración**:
```bash
git remote -v
git branch
```
