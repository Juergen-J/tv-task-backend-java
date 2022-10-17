FROM openjdk:17
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} java-task.jar
ENTRYPOINT ["java", "-jar", "/java-task.jar"]
