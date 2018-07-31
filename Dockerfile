FROM openjdk:8-jre

COPY build/install/snl-notes /opt/app/

WORKDIR /opt/app

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy="" curl --silent --fail http://localhost:8093/health

EXPOSE 8093

ENTRYPOINT ["/opt/app/bin/snl-notes"]
