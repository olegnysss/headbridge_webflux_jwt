# Set the base image
FROM openjdk:11-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle build files
COPY build.gradle .
COPY settings.gradle .

# Copy the entire project directory
COPY . .

# Build the application
RUN ./gradlew build

# Set the command to run when the container starts
CMD ["java", "-jar", "build/libs/webflux_jwt-0.0.1-SNAPSHOT.jar"]