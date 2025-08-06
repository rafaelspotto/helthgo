# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia o arquivo pom.xml
COPY pom.xml .

# Baixa as dependências
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Compila o projeto
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copia o JAR compilado
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"] 