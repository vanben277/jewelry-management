# Stage 1: Build - Dùng Maven để đóng gói file JAR
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml và tải dependencies trước để tận dụng Docker Cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy toàn bộ code và build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run - Sử dụng JRE gọn nhẹ để chạy ứng dụng
FROM eclipse-temurin:21-jre
WORKDIR /app

# Cài đặt thư viện bổ trợ cho AI/LangChain4j (libgomp1) và curl cho health check
RUN apt-get update && apt-get install -y \
    libgomp1 \
    curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

# Khai báo Port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]