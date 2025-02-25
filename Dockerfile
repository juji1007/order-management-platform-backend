FROM openjdk:17-alpine

COPY build/libs/*.jar app.jar

ENV PROFILE_NAME profile_blue

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE_NAME}", "-jar", "app.jar"]