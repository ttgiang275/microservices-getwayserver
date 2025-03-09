#Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

#Add Maintainer Info
MAINTAINER "https://github.com/ttgiang275"

#Add the application's jar to the image
COPY target/*.jar accounts.jar

#When the container runs, launch the Spring Boot application
#When create the container from this image will run the command
ENTRYPOINT ["java", "-jar", "/accounts.jar"]