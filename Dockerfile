FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Копируем pom.xml и исходники
COPY pom.xml .
COPY src ./src
# Собираем готовый jar-пакет, пропуская тесты для скорости
RUN mvn clean package -DskipTests

# Этап 2: Запуск готового приложения на легком образе Java
FROM amazoncorretto:17-alpine
WORKDIR /app
# Копируем собранный jar-файл из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar
# Открываем порт приложения наружу
EXPOSE 8080
# Команда для запуска
ENTRYPOINT ["java", "-jar", "app.jar"]