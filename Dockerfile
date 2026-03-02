FROM amazoncorretto:11

WORKDIR /code

# Copy the Gradle wrapper files
COPY gradlew build.gradle settings.gradle ./
COPY gradle/ gradle/

# Make gradlew executable
RUN chmod +x gradlew

CMD ["sh", "-c", "./gradlew :spotlessApply"] 
