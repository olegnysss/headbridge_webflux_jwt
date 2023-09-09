FROM openjdk:11-jdk

WORKDIR /app

COPY build.gradle .
COPY settings.gradle .

COPY . .

RUN ./gradlew build

CMD ["java", "-jar", "build/libs/webflux_jwt-0.0.1-SNAPSHOT.jar"]