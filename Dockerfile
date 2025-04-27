FROM amazoncorretto:21.0.4-alpine3.18
COPY target/AuditService-1.0-SNAPSHOT.jar /app/AuditService.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "AuditService.jar"]