FROM openjdk:11.0.6-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY start.sh /usr/bin/start.sh
ENTRYPOINT ["/usr/bin/start.sh"]
