FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=launchable/*.jar

COPY ${JAR_FILE} JRC.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/JRC.jar", "8080"]