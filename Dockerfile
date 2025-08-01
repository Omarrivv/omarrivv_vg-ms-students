# Usa una imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo JAR de la aplicación a la imagen
COPY target/msv-students-0.0.1-SNAPSHOT.jar /app/app.jar

# Expone el puerto 8081 para que la aplicación sea accesible
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
