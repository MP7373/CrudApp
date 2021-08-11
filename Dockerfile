FROM maven:3.8.1-openjdk-16-slim AS builder
COPY . ./
RUN mvn package

FROM adoptopenjdk:16-jre AS runner
COPY --from=builder /target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]