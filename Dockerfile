# Используем официальный образ OpenJDK 21 с поддержкой JDK
FROM openjdk:21-jdk

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /advertisement

# Копируем файл Maven wrapper и POM файл
COPY mvnw .
COPY pom.xml .

# Копируем директорию Maven wrapper
COPY .mvn .mvn

# Делаем mvnw исполняемым
RUN chmod +x ./mvnw

# Качаем зависимости проекта
RUN ./mvnw dependency:go-offline

# Копируем все файлы из текущей директории в рабочую директорию контейнера
COPY . .

# Собираем проект с использованием Maven
RUN ./mvnw package -DskipTests

# Указываем команду для запуска приложения
CMD ["java", "-jar", "target/advertisement-0.0.1-SNAPSHOT.jar"]
