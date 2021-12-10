build:
	./gradlew build

format:
	./gradlew :spotlessApply

test:
	./gradlew test

test_with_docker:
	docker run -t -i -w /code -v $(PWD):/code --env-file .env openjdk:15 sh -c "sh ./gradlew test"

format_with_docker:
	docker run -t -i -w /code -v $(PWD):/code openjdk:15 sh -c "sh ./gradlew :spotlessApply"
