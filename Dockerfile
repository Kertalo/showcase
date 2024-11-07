FROM maven:3.9.9-ibm-semeru-23-jammy AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:23-jdk-oracle
COPY --from=build target/showcase-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]