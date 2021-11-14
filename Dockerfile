FROM openjdk:17 
ARG JAR_FILE=target/*.jar
COPY ./target/XsdtoAvroKafkaStreams-1.0-SNAPSHOT.jar app.jar
ENV SPRING_PROFILES_ACTIVE ccloud
ENTRYPOINT ["java","-jar","/app.jar"]

