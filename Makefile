build:
	./gradlew build

format:
	./gradlew :spotlessApply

test:
	./gradlew test

test_with_docker:
	docker run -t -i -w /code -v $(PWD):/code --env-file .env amazoncorretto:17 sh -c "sh ./gradlew test"

format_with_docker:
	docker build -t stream-chat-java-formatter . && \
	docker run --user $$(id -u):$$(id -g) -v $(PWD):/code stream-chat-java-formatter
