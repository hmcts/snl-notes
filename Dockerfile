FROM openjdk:8-jre

ENV APP snl-notes.jar

COPY build/libs/$APP /opt/app/

WORKDIR /opt/app

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy="" curl --silent --fail http://localhost:8093/health

EXPOSE 8093
