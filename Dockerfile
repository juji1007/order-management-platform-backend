FROM openjdk:17-alpine

COPY build/libs/*.jar app.jar

ENV PROFILE_NAME blue
ENV REDIS_HOST localhost

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE_NAME}", "-Dspring.data.redis.host=${REDIS_HOST}", "-jar", "app.jar"]
