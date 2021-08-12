FROM maven:3.8.1-openjdk-16-slim AS builder
COPY pom.xml pom.xml
COPY /src/main/java /src/main/java
RUN mvn package

FROM adoptopenjdk:16-jre AS runner
RUN mkdir /app
COPY --from=builder /target/*.jar /app/app.jar
RUN addgroup --system app-runner
RUN adduser --system --group app-runner
RUN chown -R app-runner:app-runner /app
WORKDIR /app
USER app-runner
ENTRYPOINT ["java", "-jar", "app.jar"]