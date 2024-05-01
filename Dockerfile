#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jdk-slim
COPY --from=build /target/localization-0.0.1-SNAPSHOT.jar localization.jar
#The below code is to copy the file GeoLite2-City.mmdb from src/main/resources/GeoLite2-City.mmdb and be able to call it as GeoLite2-City.mmdb in new File("GeoLite2-City.mmdb")
COPY src/main/resources/GeoLite2-City.mmdb GeoLite2-City.mmdb 
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","localization.jar"]