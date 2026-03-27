FROM gradle:8.14.3-jdk21 AS builder
WORKDIR /workspace
COPY . .
RUN gradle :study:boot:bootJar --no-daemon

FROM amazoncorretto:21
WORKDIR /
COPY --from=builder /workspace/study/boot/build/libs/boot.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
