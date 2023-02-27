FROM openjdk:17-alpine
EXPOSE 8082
COPY ./build/libs/chat-service-0.0.1.jar /tmp/
WORKDIR /tmp
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=docker", "chat-service-0.0.1.jar"]