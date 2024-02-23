FROM ubuntu:latest As build

RUN apt-get update
RUN apt-get isntall openjdk-17-jdk -y

COPY . .

RUN ./gradlew bootJar --no-daemon
