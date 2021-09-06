FROM adoptopenjdk/openjdk8:latest

ENV TZ='GMT-3'

VOLUME /tmp

EXPOSE 8086

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} api-integra-api.jar

ENTRYPOINT ["java","-Xmx512M","-jar","/api-integra-api.jar"]
