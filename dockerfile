#Todo: Criar e chamar script de espera para subir a api apenas quando o banco estiver pronto

# Build stage
FROM maven:3.9.11-amazoncorretto-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Use clean package to produce target/*.jar
RUN mvn -B -DskipTests clean package

# Run stage
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app

# Copia o jar gerado (assumindo padrão maven target/*.jar)
# Ajuste se seu artifactId/version diferir.
COPY --from=build /app/target/*.jar app.jar

# Exponha porta (documentação)
EXPOSE 8080

# Variável de ambiente para ajustar Java memory se quiser
ENV JAVA_OPTS=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app/app.jar" ]
