FROM amazoncorretto:11

WORKDIR /code

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle gradle/
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

CMD ["sh", "-c", "./gradlew :spotlessApply"] 