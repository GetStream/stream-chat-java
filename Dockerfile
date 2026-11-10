FROM amazoncorretto:11

WORKDIR /code

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle gradle/
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

RUN yum install -y shadow-utils findutils && yum clean all \
    && useradd --create-home --uid 1000 formatter \
    && chown -R formatter:formatter /code

ENV GRADLE_USER_HOME=/tmp/gradle
USER formatter

CMD ["sh", "-c", "./gradlew :spotlessApply"]
