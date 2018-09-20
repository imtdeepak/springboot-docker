FROM openjdk:8-jre-alpine

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/example-1.0-SNAPSHOT.jar

ADD ${JAR_FILE} example.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/example.jar"]
