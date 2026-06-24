# --- Etapa 1: Compilación ---
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar el archivo de configuración de dependencias y descargar el caché
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el archivo JAR (saltándose los tests para agilizar)
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Imagen de producción ligera ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copiar el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto configurado
EXPOSE 8081

# Pre-crear la estructura de carpetas para la base de datos H2 local
RUN mkdir -p ./data/dataLocales

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]