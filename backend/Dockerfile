# Estágio 1: build com Maven
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
# Copia pom primeiro para aproveitar cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Agora copia o código e compila
COPY src ./src
RUN mvn clean package -DskipTests -B

# Estágio 2: imagem final, só com JRE (mais leve)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
