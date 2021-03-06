FROM hmcts/cnp-java-base:openjdk-jre-8-alpine-1.4

# Mandatory!
ENV APP snl-notes.jar
ENV APPLICATION_TOTAL_MEMORY 512M
ENV APPLICATION_SIZE_ON_DISK_IN_MB 100

# Optional
ENV JAVA_OPTS ""

COPY build/libs/$APP /opt/app/

WORKDIR /opt/app

HEALTHCHECK --interval=10s --timeout=10s --retries=10 CMD http_proxy="" wget --spider -q http://localhost:8093/health || exit 1

EXPOSE 8093
