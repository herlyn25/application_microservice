#Deploy desde Dockerfile
FROM eclipse-temurin:21-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Copiar el .jar al contenedor
COPY ../applications/app-service/build/libs/applicationservices.jar applicationservices.jar
COPY .env /app/.env

#Exponer puerto
EXPOSE 8081

# Punto de entrada
ENTRYPOINT [ "java", "-jar", "applicationservices.jar" ]