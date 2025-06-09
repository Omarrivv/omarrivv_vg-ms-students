# Microservicio de Gestión de Estudiantes

Este es un microservicio reactivo construido con Spring WebFlux y MongoDB para la gestión de estudiantes y sus matrículas en aulas.

## Stack Tecnológico

- Java 17
- Spring Boot 2.7.0
- Spring WebFlux (Programación Reactiva)
- MongoDB Reactive
- Project Reactor
- Lombok
- Maven

## Arquitectura del Proyecto

El proyecto sigue una Arquitectura Limpia (Clean Architecture) con la siguiente estructura:

```
src/main/java/pe/edu/vallegrande/msvstudents/
├── application/
│   └── service/         # Servicios de aplicación que implementan la lógica de negocio
├── domain/
│   ├── enums/          # Enumeraciones del dominio
│   ├── models/         # Entidades y modelos del dominio
│   └── repository/     # Interfaces de repositorio (puertos)
└── infrastructure/
    ├── config/         # Clases de configuración
    ├── dto/            # Objetos de Transferencia de Datos (DTOs)
    ├── exception/      # Manejo de excepciones
    ├── repository/     # Implementaciones de repositorios (adaptadores)
    ├── rest/           # Controladores REST
    └── service/        # Implementaciones de servicios
```

## Dependencias Principales

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- Lombok para reducir código boilerplate -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
</dependency>

<!-- Reactor para programación reactiva -->
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-core</artifactId>
</dependency>
```

## Endpoints de la API

### Controlador de Estudiantes (`/api/v1/students`)

| Método | Endpoint | Descripción | Tipo de Respuesta |
|--------|----------|-------------|-------------------|
| GET | `/api/v1/students` | Obtener todos los estudiantes | Flux<StudentResponse> |
| GET | `/api/v1/students/{id}` | Obtener estudiante por ID | Mono<StudentResponse> |
| POST | `/api/v1/students` | Crear nuevo estudiante | Mono<StudentResponse> |
| PUT | `/api/v1/students/{id}` | Actualizar estudiante | Mono<StudentResponse> |
| DELETE | `/api/v1/students/{id}` | Eliminar estudiante (baja lógica) | Mono<Void> |
| GET | `/api/v1/students/institution/{institutionId}` | Buscar estudiantes por institución | Flux<StudentResponse> |
| GET | `/api/v1/students/status/{status}` | Buscar estudiantes por estado | Flux<StudentResponse> |
| GET | `/api/v1/students/gender/{gender}` | Buscar estudiantes por género | Flux<StudentResponse> |
| PUT | `/api/v1/students/{id}/restore` | Restaurar estudiante eliminado | Mono<StudentResponse> |

### Controlador de Matrículas (`/api/v1/classroom-students`)

| Método | Endpoint | Descripción | Tipo de Respuesta |
|--------|----------|-------------|-------------------|
| GET | `/api/v1/classroom-students` | Obtener todas las matrículas | Flux<ClassroomStudentResponse> |
| GET | `/api/v1/classroom-students/{id}` | Obtener matrícula por ID | Mono<ClassroomStudentResponse> |
| POST | `/api/v1/classroom-students` | Crear nueva matrícula | Mono<ClassroomStudentResponse> |
| PUT | `/api/v1/classroom-students/{id}` | Actualizar matrícula | Mono<ClassroomStudentResponse> |
| DELETE | `/api/v1/classroom-students/{id}` | Eliminar matrícula (baja lógica) | Mono<Void> |
| GET | `/api/v1/classroom-students/student/{studentId}` | Buscar matrículas por estudiante | Flux<ClassroomStudentResponse> |
| GET | `/api/v1/classroom-students/classroom/{classroomId}` | Buscar matrículas por aula | Flux<ClassroomStudentResponse> |
| GET | `/api/v1/classroom-students/status/{status}` | Buscar matrículas por estado | Flux<ClassroomStudentResponse> |
| GET | `/api/v1/classroom-students/year/{year}` | Buscar matrículas por año | Flux<ClassroomStudentResponse> |
| GET | `/api/v1/classroom-students/period/{period}` | Buscar matrículas por periodo | Flux<ClassroomStudentResponse> |
| GET | `/api/v1/classroom-students/year/{year}/period/{period}` | Buscar por año y periodo | Flux<ClassroomStudentResponse> |
| PUT | `/api/v1/classroom-students/{id}/restore` | Restaurar matrícula eliminada | Mono<ClassroomStudentResponse> |

## Características Principales

1. **Programación Reactiva**
   - Uso de Project Reactor para operaciones no bloqueantes
   - Mejor manejo de la concurrencia y recursos del sistema
   - Respuestas asíncronas con Flux y Mono

2. **Base de Datos**
   - MongoDB como base de datos NoSQL
   - Acceso reactivo a datos con Spring Data MongoDB Reactive
   - Operaciones CRUD asíncronas

3. **Arquitectura**
   - Implementación de Clean Architecture
   - Separación clara de responsabilidades
   - Fácil mantenimiento y pruebas
   - Independencia de frameworks

4. **Seguridad y Configuración**
   - CORS habilitado para integración con frontends
   - Endpoints de Actuator para monitoreo
   - Manejo de excepciones personalizado

## Configuración e Instalación

1. Requisitos previos:
   - Java 17 instalado
   - MongoDB instalado y en ejecución
   - Maven instalado

2. Pasos de instalación:
   ```bash
   # Clonar el repositorio
   git clone [URL_DEL_REPOSITORIO]

   # Entrar al directorio
   cd msv-students

   # Instalar dependencias
   mvn clean install

   # Ejecutar la aplicación
   mvn spring-boot:run
   ```

3. Configuración de MongoDB:
   - Editar `application.properties` o `application.yml`
   - Configurar la URL de conexión a MongoDB
   - Configurar el nombre de la base de datos

## Códigos de Estado HTTP

- 200: Éxito en la operación
- 201: Recurso creado exitosamente
- 204: Operación exitosa sin contenido de respuesta
- 404: Recurso no encontrado
- 500: Error interno del servidor

## Tipos de Respuesta

El microservicio utiliza tipos reactivos para todas las respuestas:
- `Flux<T>`: Para colecciones de elementos (streams)
- `Mono<T>`: Para elementos únicos

## Manejo de Errores

El sistema incluye un manejo de excepciones personalizado para:
- Recursos no encontrados
- Errores de validación
- Errores de base de datos
- Errores internos del servidor

## Monitoreo

Se incluyen endpoints de Actuator para monitoreo:
- `/actuator/health`: Estado de salud del servicio
- `/actuator/info`: Información del servicio
- `/actuator/metrics`: Métricas del servicio

## Repositorios

- GitHub: https://github.com/Omarrivv/vg-ms-students
- GitLab: https://gitlab.com/vallegrande/as231s5_prs2/vg-ms-students

## Contribución

1. Fork el repositorio
2. Cree una rama para su feature (`git checkout -b feature/AmazingFeature`)
3. Commit sus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abra un Pull Request

