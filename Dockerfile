FROM openjdk:21-jdk
WORKDIR /advertisement
COPY . .
RUN ./mvnw package -DskipTests
CMD ["java", "-jar", "web/target/web-0.0.1-SNAPSHOT.jar"]
