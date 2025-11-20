# Estágio 1: Build (Compilar o .jar)
# Mudamos de 17 para 21 aqui
FROM maven:3.9.8-eclipse-temurin-21 AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o pom.xml primeiro para aproveitar o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código-fonte
COPY src ./src

# Compila a aplicação e gera o .jar
RUN mvn package -DskipTests

# Estágio 2: Run (Imagem final, leve)
# Mudamos de 17 para 21 aqui também
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copia o .jar gerado no Estágio 1 para a imagem final
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta que o Spring Boot usa
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
