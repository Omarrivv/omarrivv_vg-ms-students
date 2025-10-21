# Imagen base OpenJDK 17 - Estándar PRS
FROM openjdk:17-jdk-slim

# Metadatos PRS
LABEL service.name="vg-ms-students"
LABEL service.version="1.0"

# Directorio de trabajo
WORKDIR /app

# Copiar JAR siguiendo nomenclatura PRS
COPY target/vg-ms-students-1.0.jar app.jar

# Exponer puerto usando variable PRS
EXPOSE 8102

# Ejecutar aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
