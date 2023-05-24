FROM gradle:7.4.2-jdk17-alpine as build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew gradle.properties ./
COPY src src

RUN gradle build

FROM eclipse-temurin:17-jdk-alpine as blockchain-run
VOLUME /app

COPY --from=build /app/build/libs/blockchain.jar ./app/