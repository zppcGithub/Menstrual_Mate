# 使用OpenJDK 17作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制Maven构建文件
COPY pom.xml .
COPY src ./src

# 安装Maven
RUN apt-get update && apt-get install -y maven

# 构建应用
RUN mvn clean package -DskipTests

# 暴露端口
EXPOSE 8080

# 设置JVM参数（可根据需要调整）
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 启动应用
CMD ["java", "-jar", "target/menstrual-mate-backend-1.0.0.jar"]