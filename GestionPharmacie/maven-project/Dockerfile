FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -q -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libxtst6 \
    libxrender1 \
    libxxf86vm1 \
    libgl1 \
    fonts-dejavu \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target /app/target
COPY --from=build /root/.m2 /root/.m2
COPY pom.xml .
COPY src ./src

RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

ENV DISPLAY=:0

CMD ["mvn", "javafx:run"]
