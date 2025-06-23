# Microservicio de Estudiantes (MSV-Students)

Este microservicio gestiona la información de estudiantes y sus matrículas en aulas para Valle Grande.

## Estructura del Proyecto

```
src/main/java/pe/edu/vallegrande/msvstudents/
├── MsvStudentsApplication.java
├── application
│   └── service
│       ├── ClassroomStudentService.java
│       ├── StudentService.java
│       └── impl
│           ├── ClassroomStudentServiceImpl.java
│           └── StudentServiceImpl.java
├── domain
│   ├── enums
│   │   ├── DocumentType.java
│   │   ├── Gender.java
│   │   └── Status.java
│   └── model
│       ├── ClassroomStudent.java
│       └── Student.java
└── infrastructure
    ├── config
    │   └── WebConfig.java
    ├── dto
    │   ├── request
    │   │   ├── ClassroomStudentRequest.java
    │   │   └── StudentRequest.java
    │   └── response
    │       ├── ClassroomStudentResponse.java
    │       └── StudentResponse.java
    ├── exception
    │   └── GlobalExceptionHandler.java
    ├── repository
    │   ├── ClassroomStudentRepository.java
    │   ├── StudentRepository.java
    │   └── impl
    │       ├── ClassroomStudentRepositoryImpl.java
    │       └── StudentRepositoryImpl.java
    └── rest
        ├── ClassroomStudentController.java
        └── StudentController.java
```

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

Este proyecto se mantiene en dos repositorios remotos: GitHub y GitLab. Los repositorios configurados son:
- GitHub: https://github.com/Omarrivv/vg-ms-students.git
- GitLab: https://gitlab.com/vallegrande/as231s5_prs2/vg-ms-students.git

### Estado Actual de Remotos

```bash
# Ver los remotos configurados
git remote -v

# Resultado esperado:
github  https://github.com/Omarrivv/vg-ms-students.git (fetch)
github  https://github.com/Omarrivv/vg-ms-students.git (push)
origin  https://gitlab.com/vallegrande/as231s5_prs2/vg-ms-students.git (fetch)
origin  https://gitlab.com/vallegrande/as231s5_prs2/vg-ms-students.git (push)
```

### Subir Cambios a Ambos Repositorios

1. Guardar los cambios locales:
```bash
# Ver el estado de los archivos
git status

# Agregar los archivos modificados
git add .

# Crear un commit con los cambios
git commit -m "descripción de los cambios realizados"
```

2. Subir a GitLab (origin):
```bash
# Actualizar rama main desde GitLab
git pull origin main

# Subir cambios a GitLab
git push origin main
```

3. Subir a GitHub:
```bash
# Actualizar rama main desde GitHub
git pull github main

# Subir cambios a GitHub
git push github main
```

### Comandos Útiles

```bash
# Ver el historial de commits
git log --oneline --graph --all

# Ver en qué rama estás y su estado
git status

# Ver las diferencias antes de hacer commit
git diff

# Deshacer cambios en un archivo antes de hacer commit
git checkout -- nombre-archivo

# Crear una nueva rama
git checkout -b nombre-rama

# Cambiar de rama
git checkout nombre-rama
```

### Resolución de Conflictos

Si hay conflictos al hacer pull de algún repositorio:

1. Identificar los archivos con conflictos (aparecerán en rojo al hacer git status)

2. Abrir los archivos con conflictos y buscar las marcas de conflicto:
```
<<<<<<< HEAD
tus cambios locales
=======
cambios del repositorio remoto
>>>>>>> branch-name
```

3. Editar el archivo para mantener el código correcto y eliminar las marcas de conflicto

4. Agregar los archivos resueltos:
```bash
git add .
```

5. Completar el merge:
```bash
git commit -m "resolver conflictos de merge"
```

6. Continuar con el push al repositorio correspondiente

### Recomendaciones

1. Siempre hacer pull antes de empezar a trabajar:
```bash
git pull origin main
git pull github main
```

2. Crear commits pequeños y descriptivos

3. En caso de duda sobre el estado del repositorio:
```bash
# Ver estado actual
git status

# Ver historial
git log --oneline
```

4. Si necesitas deshacer el último commit (pero mantener los cambios):
```bash
git reset --soft HEAD~1
```
