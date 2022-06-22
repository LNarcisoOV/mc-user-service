FROM openjdk:17-oracle
EXPOSE 8082
ADD /target/mc-user-service-0.0.1-SNAPSHOT.jar mc-user-service.jar
ENTRYPOINT ["java", "-jar", "mc-user-service.jar"]