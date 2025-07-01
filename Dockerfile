# Используем официальный образ с JDK 17
FROM eclipse-temurin:17-jdk

# Создаём рабочую директорию в контейнере
WORKDIR /app

# Копируем всё из текущей директории проекта
COPY .. .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
EXPOSE 8080

# Указываем, как запускать приложение
CMD ["java", "-jar", "target/popitka2-0.0.1-SNAPSHOT.jar"]
