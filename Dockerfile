FROM openjdk:17-slim
ADD target/fctfinder-backend-0.0.1.jar fctfinder-backend-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "fctfinder-backend-0.0.1.jar"]