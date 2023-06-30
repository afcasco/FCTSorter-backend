FROM eclipse-temurin:17-jdk-alpine
ADD target/fctfinder-backend-0.0.1.jar fctfinder-backend-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar fctfinder-backend-0.0.1.jar"]