# Official Java SDK for [Stream Chat](https://getstream.io/chat/docs/)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.getstream/stream-chat-java/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.getstream/stream-chat-java) [![build](https://github.com/GetStream/stream-chat-java/workflows/Build/badge.svg)](https://github.com/GetStream/stream-chat-java/actions)

<p align="center">
    <img src="./assets/logo.svg" width="50%" height="50%">
</p>
<p align="center">
    Official Java API client for Stream Chat, a service for building chat applications.
    <br />
    <a href="https://getstream.io/chat/docs/"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://getstream.github.io/stream-chat-java/">JavaDoc</a>
    ·
    <a href="https://github.com/GetStream/stream-chat-java/issues">Report Bug</a>
    ·
    <a href="https://github.com/GetStream/stream-chat-java/issues">Request Feature</a>
</p>

## 📝 About Stream

You can sign up for a Stream account at our [Get Started](https://getstream.io/chat/get_started/) page.

You can use this library to access chat API endpoints server-side.

For the client-side integrations (web and mobile) have a look at the JavaScript, iOS and Android SDK libraries ([docs](https://getstream.io/chat/)).

## ⚙️ Installation

> The Stream chat Java SDK requires Java 11+. It supports latest LTS. If you need support an older Java, please contact at [support](https://getstream.io/contact/support/).

> The Stream chat Java SDK is compatible with Groovy, Scala, Kotlin and Clojure.
### Installation for Java

#### Gradle

```gradle
dependencies {
    implementation "io.getstream:stream-chat-java:$stream_version"
}
```
#### Maven

```maven
<dependency>
  <groupId>io.getstream</groupId>
  <artifactId>stream-chat-java</artifactId>
  <version>$stream_version</version>
</dependency>
```

### Installation for Groovy

#### Gradle

```gradle
dependencies {
    implementation 'io.getstream:stream-chat-java:$stream_version'
}
```

> You can see an example project at [GetStream/stream-chat-groovy-example](https://github.com/GetStream/stream-chat-groovy-example).

### Installation for Scala

#### Gradle

```gradle
dependencies {
    implementation 'io.getstream:stream-chat-java:$stream_version'
}
```

> You can see an example project at [GetStream/stream-chat-scala-example](https://github.com/GetStream/stream-chat-scala-example).

### Installation for Kotlin

#### Gradle

```gradle
dependencies {
    implementation("io.getstream:stream-chat-java:$stream_version")
}
```

> You can see an example project at [GetStream/stream-chat-kotlin-example](https://github.com/GetStream/stream-chat-kotlin-example).

### Installation for Clojure

#### Leiningen

```leiningen
:dependencies [[io.getstream/stream-chat-java "$stream_version"]]
```

> You can see an example project at [GetStream/stream-chat-clojure-example](https://github.com/GetStream/stream-chat-clojure-example).

## 🔀 Dependencies
This SDK uses lombok (code generation), retrofit (http client), jackson (json) and jjwt (jwt).

> You can find the exact versions in [build.gradle](./build.gradle).

## ✨ Getting started
### Configuration

To configure the SDK you need to provide required properties

| Property  | ENV | Default  | Required |
| ------------- | ------------- | --- | --- |
| io.getstream.chat.apiKey  | STREAM_KEY  | - | Yes |
| io.getstream.chat.secretKey  | STREAM_SECRET  | - | Yes |
| io.getstream.chat.timeout  | STREAM_CHAT_TIMEOUT  | 10000 | No |
| io.getstream.chat.url  | STREAM_CHAT_URL  | https://chat.stream-io-api.com | No |

You can also use your own CDN by creating an implementation of FileHandler and setting it this way

```java
Message.fileHandlerClass = MyFileHandler.class
```
All setup must be done prior to any request to the API.

## Print Chat app configuration
<table>
<tbody>
<tr><td><strong>Java</strong></td><td>

```java
System.out.println(App.get().request());
```

</td></tr><tr><td><strong>Groovy</strong><td>

```groovy
println App.get().request()
```

</td></tr><tr><td><strong>Scala</strong><td>

```scala
println(App.get.request)
```

</td></tr><tr><td><strong>Kotlin</strong><td>

```kotlin
println(App.get().request())
```

</td></tr><tr><td><strong>Clojure</strong><td>

```clojure
println (.request (App/get))
```

</td></tr>
</tbody></table>

## 📚 Code examples

Head over to [DOCS.md](./DOCS.md) for code snippets.

## 🙋 FAQ

1. If you get this exception: `java.lang.ClassNotFoundException: io.jsonwebtoken.SignatureAlgorithm`:

See issue [#16](https://github.com/GetStream/stream-chat-java/issues/16) for a work around. We only provide runtime only dependency for JWT per [recommendation](https://github.com/jwtk/jjwt#understanding-jjwt-dependencies). That's why it might be missing in your runtime and by addding implementation library into your deps, it should be gone.

## ✍️ Contributing

We welcome code changes that improve this library or fix a problem, please make sure to follow all best practices and add tests if applicable before submitting a Pull Request on Github. We are very happy to merge your code in the official repository. Make sure to sign our [Contributor License Agreement (CLA)](https://docs.google.com/forms/d/e/1FAIpQLScFKsKkAJI7mhCr7K9rEIOpqIDThrWxuvxnwUq2XkHyG154vQ/viewform) first. See our [license file](./LICENSE) for more details.

Head over to [CONTRIBUTING.md](./CONTRIBUTING.md) for some development tips.

## 🧑‍💻 We are hiring!

We've recently closed a [$38 million Series B funding round](https://techcrunch.com/2021/03/04/stream-raises-38m-as-its-chat-and-activity-feed-apis-power-communications-for-1b-users/) and we keep actively growing.
Our APIs are used by more than a billion end-users, and you'll have a chance to make a huge impact on the product within a team of the strongest engineers all over the world.

Check out our current openings and apply via [Stream's website](https://getstream.io/team/#jobs).
