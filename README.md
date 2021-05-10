# Official Java SDK for [Stream Chat](https://getstream.io/chat/sdk/java/)
This SDK is a wrapper for Stream API
> See the [full API documentation](https://getstream.io/chat/docs/rest)
## Use the Java SDK
### Requirements
The Stream chat Java SDK requires Java 1.8+.
### Compatibility
The Stream chat Java SDK is compatible with Kotlin and Scala.
### Installation

**With Gradle**: Add the library as a dependency in your *module* level `build.gradle` file:
> See the [releases page](https://github.com/GetStream/stream-chat-java/releases) for the latest version number.

```gradle
dependencies {
    implementation "io.getstream:stream-chat-java:$stream_version"
}
```

**With Maven**: Add the library as a dependency in `pom.xml` file:
> See the [releases page](https://github.com/GetStream/stream-chat-java/releases) for the latest version number.

```maven
<dependency>
  <groupId>io.getstream</groupId>
  <artifactId>stream-chat-java</artifactId>
  <version>$stream_version</version>
</dependency>
```

### Dependencies
This SDK uses the following dependencies:
- com.squareup.retrofit2/retrofit version 2.9.0
- com.squareup.retrofit2/converter-jackson version 2.9.0
- io.jsonwebtoken/jjwt-api version 0.11.2
- io.jsonwebtoken/jjwt-impl version 0.11.2
- io.jsonwebtoken/jjwt-jackson version 0.11.2

### Configuration
To configure the SDK, you need to setup the key and secret of your application.
You can do so either by:
- setting STREAM_KEY and STREAM_SECRET System properties
- setting STREAM_KEY and STREAM_SECRET environment variables  
You can also customize the base url and the timeout, with the STREAM_CHAT_URL and STREAM_CHAT_TIMEOUT System properties or environment variables.

### Usage principles
To perform a request on the Stream Chat API, you need to:

#### Create a StreamRequest
You do so by calling static methods on Stream Model classes.

#### Set all information you want in the StreamRequest
StreamRequest objects have builder style methods. Some methods require xxxRequestObject instances. All xxxRequestObject classes have builder included.

#### Perform the request 
This can be done either synchronously, calling the `request()` method and handling the StreamException exceptions, or asynchronously, calling the `requestAsync(Consumer<Response> onSuccess, Consumer<StreamException> onError)`

### Examples
**Synchronous:**

```java
try {
      Message message = Message.send("team", "sample_channel")
          .message(
              MessageRequestObject.builder().text("Sample message").userId("fakeUserId").build())
          .request().getMessage();
    } catch (StreamException e) {
      // Handle the exception
    }  
```

**Asynchronous:**

```java
Message.send("team", "sample_channel")
        .message(MessageRequestObject.builder().text("Sample message").userId("fakeUserId").build())
        .requestAsync(
            (sendMessageResponse) -> {
              Message message = sendMessageResponse.getMessage();
            },
            (exception) -> {
              // Handle the exception
            });
```

### Supported features
TODO
### All examples
TODO
## Contribute
> See [The guide to contribute](CONTRIBUTING.md)