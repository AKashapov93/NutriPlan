FROM maven:3.8.5-openjdk-17-slim AS build
COPY . .
RUN mvn clean install

FROM openjdk:17-jdk
COPY --from=build target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]