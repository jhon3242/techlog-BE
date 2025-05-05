FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

ARG VERSION=0.0.1
COPY . .
RUN ./gradlew build -x test --parallel
RUN mkdir -p build/extracted \
    && java -Djarmode=layertools \
           -jar build/libs/techlog-${VERSION}.jar \
           extract --destination build/extracted

FROM mcr.microsoft.com/playwright:v1.52.0-noble

USER root
RUN apt-get update && apt-get install -y \
      openjdk-17-jdk-headless \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /workspace/app
VOLUME /tmp

ARG VERSION=0.0.1
ARG EXTRACTED=/workspace/app/build/extracted

COPY --from=build ${EXTRACTED}/dependencies/     ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/       ./

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "org.springframework.boot.loader.launch.JarLauncher"]
