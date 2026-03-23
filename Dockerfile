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

# Cài đặt thư viện bổ trợ cho AI/LangChain4j (libgomp1)
RUN apt-get update && apt-get install -y \
    libgomp1 \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

# Khai báo Port
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]