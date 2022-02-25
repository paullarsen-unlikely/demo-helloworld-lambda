FROM openjdk:17

WORKDIR /app

COPY target/*jar-with-dependencies.jar app.jar
COPY deployment/entrypoint.sh .
RUN chmod +x entrypoint.sh

SHELL ["/bin/bash", "-c"]
CMD ./entrypoint.sh
