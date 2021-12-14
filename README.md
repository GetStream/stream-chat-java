# Official Java SDK for [Stream Chat](https://getstream.io/chat/docs/) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.getstream/stream-chat-java/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/io.getstream/stream-chat-java) [![build](https://github.com/GetStream/stream-chat-java/workflows/Build/badge.svg)](https://github.com/GetStream/stream-chat-java/actions)

The official Java API client for [Stream chat](https://getstream.io/chat/) a service for building chat applications.

You can sign up for a Stream account at https://getstream.io/chat/get_started/.

You can use this library to access chat API endpoints server-side, for the client-side integrations (web and mobile) have a look at the Javascript, iOS and Android SDK libraries (https://getstream.io/chat/).

<details open>
<summary><b>Table of Contents</b></summary>

- [Usage](#usage)
  - [Requirements](#requirements)
  - [Compatibility](#compatibility)
  - [Installation for Java](#installation-for-java)
  - [Installation for Groovy](#installation-for-groovy)
  - [Installation for Scala](#installation-for-scala)
  - [Installation for Kotlin](#installation-for-kotlin)
  - [Installation for Clojure](#installation-for-clojure)
  - [Dependencies](#dependencies)
  - [Configuration](#configuration)
  - [JavaDoc](#javadoc)
  - [Simple test](#simple-test)
  - [Usage Principles](#usage-principles)
    - [Create a StreamRequest](#create-a-streamrequest)
    - [Set all information you want in the StreamRequest](#set-all-information-you-want-in-the-streamrequest)
    - [Perform the request](#perform-the-request)
  - [Simple Example](#simple-example)
  - [Supported Features](#supported-features)
  - [All Examples](#all-examples)
- [Changelog](#changelog)
- [FAQ](#faq)
- [Contribute](#contribute)
- [License](#license)
- [We are hiring](#we-are-hiring)
</details>

## Usage
### Requirements
The Stream chat Java SDK requires Java 11+.

> It supports latest LTS. If you need support an older Java, please contact at [support](https://getstream.io/contact/support/).
### Compatibility
The Stream chat Java SDK is compatible with Groovy, Scala, Kotlin and Clojure.
### Installation for Java

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

### Installation for Groovy

**With Gradle**: Add the library as a dependency in your *module* level `build.gradle` file:
> See the [releases page](https://github.com/GetStream/stream-chat-java/releases) for the latest version number.

```gradle
dependencies {
    implementation 'io.getstream:stream-chat-java:$stream_version'
}
```

> You can see an example project at [GetStream/stream-chat-groovy-example](https://github.com/GetStream/stream-chat-groovy-example).

### Installation for Scala

**With Gradle**: Add the library as a dependency in your *module* level `build.gradle` file:
> See the [releases page](https://github.com/GetStream/stream-chat-java/releases) for the latest version number.

```gradle
dependencies {
    implementation 'io.getstream:stream-chat-java:$stream_version'
}
```

> You can see an example project at [GetStream/stream-chat-scala-example](https://github.com/GetStream/stream-chat-scala-example).

### Installation for Kotlin

**With Gradle**: Add the library as a dependency in your *module* level `build.gradle.kts` file:
> See the [releases page](https://github.com/GetStream/stream-chat-java/releases) for the latest version number.

```gradle
dependencies {
    implementation("io.getstream:stream-chat-java:$stream_version")
}
```

> You can see an example project at [GetStream/stream-chat-kotlin-example](https://github.com/GetStream/stream-chat-kotlin-example).

### Installation for Clojure

**With Leiningen**: Add the library as a dependency in your `project.clj` file:
> See the [releases page](https://github.com/GetStream/stream-chat-java/releases) for the latest version number.

```leiningen
:dependencies [[io.getstream/stream-chat-java "$stream_version"]]
```

> You can see an example project at [GetStream/stream-chat-clojure-example](https://github.com/GetStream/stream-chat-clojure-example).

### Dependencies
This SDK uses lombok (code generation), retrofit (http client), jackson (json) and jjwt (jwt).

> You can find the exact versions in [build.gradle](./build.gradle).

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

### JavaDoc

It's automatically built and published at https://getstream.github.io/stream-chat-java/

### Simple test
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

### Usage principles
To perform a request on the Stream Chat API, you need to:

#### Create a StreamRequest
You do so by calling static methods on Stream Model classes.

#### Set all information you want in the StreamRequest
StreamRequest objects have builder style methods. Some methods require xxxRequestObject instances. All xxxRequestObject classes have builder included, and when there is a corresponding model they have a `buildFrom` method.

#### Perform the request
This can be done either synchronously, calling the `request()` method and handling the StreamException exceptions, or asynchronously, calling the `requestAsync(Consumer<Response> onSuccess, Consumer<StreamException> onError)`

### Simple Example
**Synchronous:**

```java
try {
    Message message = Message.send("team", "sample_channel")
        .message(MessageRequestObject.builder().text("Sample message").userId("fakeUserId").build())
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
**Channel types**
- Create channel type
- Delete channel type
- Get channel type
- List channel types
- Update channel type

**Channels**
- Delete channel
- Delete many channels
- Export channels
- Export channels status
- Get or create channel (type,id)
- Get or create channel (type)
- Hide channel
- Mark all read
- Mark read
- Mute channel
- Partially update channel
- Query channels
- Query members
- Search messages
- Show channel
- Truncate channel
- Unmute channel
- Update channel

**Custom commands**
- Create command
- Delete command
- Get command
- List commands
- Update command

**Devices**
- Create device
- Delete device
- List devices

**Events**
- Send event
- Send user event

**Files**
- Delete file
- Delete image
- Upload file
- Upload image

**GDPR**
- Deactivate user
- Delete user
- Delete many users
- Reactivate user

**Messages**
- Delete file
- Delete image
- Delete message
- Delete reaction
- Flag
- Get many messages
- Get message
- Get reactions
- Get replies
- Mark all read
- Mark read
- Run message command action
- Search messages
- Send new message
- Send reaction
- Translate message
- Unflag
- Update message
- Upload file
- Upload image

**Moderation**
- Ban user
- Create block list
- Deactivate user
- Delete block list
- Delete user
- Flag
- Get block list
- List block lists
- Mute user
- Query banned users
- Reactivate user
- Unban user
- Unflag
- Unmute user
- Update block list

**Permissions V2**
- Create custom permission
- Create custom role
- Delete custom permission
- Delete custom role
- Get custom permission
- List custom permission
- List custom roles
- Update custom permission

**Reactions**
- Delete reaction
- Get reactions
- Send reaction

**Settings**
- Check push
- Check SQS
- Create block list
- Create channel type
- Delete block list
- Delete channel type
- Get App Settings
- Get block list
- Get channel type
- Get rate limits
- List block lists
- List channel types
- Update App Settings
- Update block list
- Update channel type

**Testing**
- Check push
- Check SQS

**Users**
- Ban user
- Create guest
- Deactivate user
- Delete user
- Delete many users
- Export user
- Flag
- Mute user
- Partially update user
- Query Banned Users
- Query users
- Reactivate user
- Unban user
- Unflag
- Unmute user
- Upsert users

**Tasks**
- Get task status

### All examples
**Upsert users**

Single user

```java
User.upsert()
    .user(
        UserRequestObject.builder()
            .id(userId)
            .role("admin")
            .additionalField("book", "dune")
            .build())
    .request();
```

Batch of users

```java
User.upsert()
    .user(
        UserRequestObject.builder()
            .id(userId1)
            .role("admin")
            .additionalField("book", "dune")
            .build())
    .user(
        UserRequestObject.builder()
            .id(userId2)
            .role("user")
            .additionalField("book", "1984")
            .build())
    .user(
        UserRequestObject.builder()
            .id(userId3)
            .role("admin")
            .additionalField("book", "Fahrenheit 451")
            .build())
    .request();
```

Set user teams

```java
// creates or updates a user from backend to be part of the "red" and "blue" teams
User.upsert()
    .user(UserRequestObject.builder().id(id).teams(Arrays.asList("red", "blue")).build())
    .request();

```

**Partially update user**

Standard

```java
/*
 * make partial update call for userID
 * it set's user.role to "admin", sets  user.field = {'text': 'value'}
 * and user.field2.subfield = 'test'.
 * NOTE:
 * changing role is available only for server-side auth.
 * field name should not contain dots or spaces, as dot is used as path separator.
 */
// response will contain user object with updated users info
User.partialUpdate()
    .user(
        UserPartialUpdateRequestObject.builder()
            .id("userId")
            .setValue("role", "admin")
            .setValue("field", Collections.singletonMap("text", "value"))
            .setValue("field2.subfield", "test")
            .build())
    .request();
// partial update for multiple users
User.partialUpdate()
    .user(
        UserPartialUpdateRequestObject.builder()
            .id("userId")
            .setValue("field", "value")
            .build())
    .user(
        UserPartialUpdateRequestObject.builder()
            .id("userId2")
            .unsetValue("field.value")
            .build())
    .request();
```

Change a user role

```java
User.partialUpdate()
    .user(
        UserPartialUpdateRequestObject.builder()
            .id("tommaso")
            .setValue("name", "Tommy Doe")
            .setValue("role", "admin")
            .build())
```

**Update App Settings**

Standard

```java
// disable auth checks, allows dev token usage
App.update().disableAuthChecks(true).request();
// re-enable auth checks
App.update().disableAuthChecks(false).request();
// Disallow guests from using queryUsers
App.update().userSearchDisallowedRoles(Arrays.asList("guest")).request();
```

Disable permissions checks

```java
// disable permission checks
App.update().disablePermissionsChecks(true).request();
// re-enable permission checks
App.update().disablePermissionsChecks(false).request();
```

Enforce unique usernames in app

```java
App.update().enforceUniqueUsernames("app").request();
```

Enforce unique usernames in team

```java
App.update().enforceUniqueUsernames("team").request();
```

Enable teams

```java
App.update().multiTenantEnabled(true).request();
```

Enable image moderation

```java
App.update().imageModerationEnabled(true).request();
```

Configure webhooks

```java
// update webhook URLs
App.update()
    .webhookURL("https://example.com/webhooks/stream/push") // sets Push webhook address
    .beforeMessageSendHookUrl(
        "https://example.com/webhooks/stream/before-message-send") // sets Before Message Send
                                                                   // webhook address
    .customActionHandlerUrl(
        "https://example.com/webhooks/stream/custom-commands?type={type}") // sets Custom
                                                                           // Commands webhook
                                                                           // address
    .request();
```

Configure APN

```java
    App.update()
        .aPNConfig(
            APNConfigRequestObject.builder()
                .authKey(Files.readAllBytes(Paths.get("./auth-key.p8")))
                .authType(AuthenticationType.TOKEN)
                .keyId("key_id")
                .bundleId("com.apple.test")
                .teamId("team_id")
                .notificationTemplate(
                    "{\"aps\" :{\"alert\":{\"title\":\"{{ sender.name }}\",\"subtitle\":\"New direct message from {{ sender.name }}\",\"body\":\"{{ message.text }}\"},\"badge\":\"{{ unread_count }}\",\"category\":\"NEW_MESSAGE\"}}")
                .build())
        .request();
```

Configure APN for development

```java
App.update()
    .aPNConfig(
        APNConfigRequestObject.builder()
            .authKey(Files.readAllBytes(Paths.get("./auth-key.p8")))
            .authType(AuthenticationType.TOKEN)
            .development(true)
            .keyId("key_id")
            .bundleId("com.apple.test")
            .teamId("team_id")
            .notificationTemplate(
                "{\"aps\" :{\"alert\":{\"title\":\"{{ sender.name }}\",\"subtitle\":\"New direct message from {{ sender.name }}\",\"body\":\"{{ message.text }}\"},\"badge\":\"{{ unread_count }}\",\"category\":\"NEW_MESSAGE\"}}")
            .build())
    .request();
```

Configure Firebase

```java
App.update()
    .firebaseConfig(
        FirebaseConfigRequestObject.builder()
            .serverKey("server_key")
            .notificationTemplate(
                "{\"message\":{\"notification\":{\"title\":\"New messages\",\"body\":\"You have {{ unread_count }} new message(s) from {{ sender.name }}\"},\"android\":{\"ttl\":\"86400s\",\"notification\":{\"click_action\":\"OPEN_ACTIVITY_1\"}}}}")
            .dataTemplate(
                "{\"sender\":\"{{ sender.id }}\",\"channel\":{\"type\": \"{{ channel.type }}\",\"id\":\"{{ channel.id }}\"},\"message\":\"{{ message.id }}\"}")
            .build())
    .request();
```

**Query users**

```java
User.list().filterCondition(FilterCondition.in("id", "john", "jack", "jessie")).request();
```

```java
UserListResponse response =
    User.list()
        .filterCondition(FilterCondition.in("id", "jessica"))
        .filterCondition("last_active", -1)
        .filterCondition("presence", true)
        .request();
```

Query banned users

```java
List<User> bannedUsers = User.list()
    .filterCondition("banned", true)
    .request()
    .getUsers();
```

Query users with teams

```java
User.list()
    .filterConditions(
        FilterCondition.and(
            FilterCondition.eq("name", "Jordan")),
            FilterCondition.contains("teams", "red"))));
```

**Get or create channel (type,id)**

Standard

```java
Channel.getOrCreate("messaging", "travel")
    .data(
        ChannelRequestObject.builder()
            .additionalField("name", "Awesome channel about traveling")
            .createdBy(UserRequestObject.builder().id("myuserid").build())
            .build())
    .request();
```

Channel pagination

```java
Channel.getOrCreate(type, id)
    .messages(MessagePaginationParameters.builder().limit(20).idLt(lastMessageId).build())
    .members(PaginationParameters.builder().limit(20).offset(0).build())
    .watchers(PaginationParameters.builder().limit(20).offset(0).build())
    .request();
```

Create a channel with team

```java
Channel.getOrCreate("messaging", "red-general")
    .data(ChannelRequestObject.builder().team("red").build())
    .request();
```

**Get or create channel (type)**

```java
Channel.getOrCreate("messaging")
    .data(
        ChannelRequestObject.builder()
            .member(ChannelMemberRequestObject.builder().userId("thierry").build())
            .member(ChannelMemberRequestObject.builder().userId("tommaso").build())
            .createdBy(UserRequestObject.builder().id("myuserid").build())
            .build())
    .request();
```

**Query channels**

Query channels

```java
List<Channel> channels =
    Channel.list().filterCondition("type", "messaging")
        .sort(Sort.builder().field("last_message_at").direction(Direction.DESC).build())
        .request().getChannels().stream()
        .map(channelResponse -> channelResponse.getChannel())
        .collect(Collectors.toList());
for (Channel channel : channels) {
  System.out.println(channel.getAdditionalFields().get("name") + ":" + channel.getCId());
}
```

Pagination

```java
Channel.list().filterCondition("type", "messaging")
    .sort(Sort.builder().field("last_message_at").direction(Direction.DESC).build())
    .limit(20).offset(10).request();
```

Query accepted invites

```java
Channel.list().filterCondition("invite", "accepted").userId("u2").request();
```

Query rejected invites

```java
Channel.list().filterCondition("invite", "rejected").userId("u2").request();
```

Query muted channels

```java
// retrieve all channels excluding muted ones
Channel.list()
    .filterCondition("muted", false)
    .filterCondition(FilterCondition.in("members", userId))
    .request();

// retrieve all muted channels
Channel.list()
    .filterCondition("muted", true)
    .filterCondition(FilterCondition.in("members", userId))
    .request();
```

With teams

```java
Channel.list().filterCondition("team", "red-team").request();
```

**Partially update channel**

Standard

```java
Map<String, String> channelDetail = new HashMap<>();
channelDetail.put("topic", "Plants and Animals");
channelDetail.put("rating", "pg");
// Here's a channel with some custom field data that might be useful
Channel.getOrCreate(type, id)
    .data(
        ChannelRequestObject.builder()
            .additionalField("source", "user")
            .additionalField("source_detail", Collections.singletonMap("user_id", 123))
            .additionalField("channel_detail", channelDetail)
            .build())
    .request();
// let's change the source of this channel
Channel.partialUpdate(type, id).setValue("source", "system").request();
// since it's system generated we no longer need source_detail
Channel.partialUpdate(type, id).unsetValue("source_detail").request();
// and finally update one of the nested fields in the channel_detail
Channel.partialUpdate(type, id).setValue("channel_detail.topic", "Nature").request();
// and maybe we decide we no longer need a rating
Channel.partialUpdate(type, id).unsetValue("channel_detail.rating").request();
```

Use a different blocklist

```java
Map<String, Object> configOverrides = new HashMap<>();
configOverrides.put("blocklist", "medical_blocklist");
configOverrides.put("blocklist_behavior", "block");
Channel.partialUpdate(type, id).setValue("config_overrides", configOverrides).request();
```

Disable replies

```java
Channel.partialUpdate(type, id)
    .setValue("config_overrides", Collections.singletonMap("replies", false))
    .request();
```

Remove overrides and go back to default settings

```java
Channel.partialUpdate(type, id).setValue("config_overrides", Collections.EMPTY_MAP).request();
```

**Update channel**
Full update (overwrite)

```java
Channel.update(type, id)
    .data(
        ChannelRequestObject.builder()
            .additionalField("name", "myspecialchannel")
            .additionalField("color", "green")
            .build())
    .message(
        MessageRequestObject.builder()
            .text("Thierry changed the channel color to green")
            .userId("Thierry")
            .build())
    .request();
```

Add/remove members

```java
Channel.update(type, id).addMember("thierry").addMember("josh").request();
// Or hiding the history of the channel when adding a new member
Channel.update(type, id).addMember("john").hideHistory(true).request();

// Removing a member
Channel.update(type, id).removeMember("tommaso").request();
```

```java
Channel.update(type, id)
    .addMember("tommaso")
    .message(
        MessageRequestObject.builder()
            .text("Tommaso joined the channel")
            .userId("tommaso")
            .build())
    .request();
```

Leaving a channel

```java
Channel.update(type, id).removeMember(myUserId).request();
```

Add/remove moderators

```java
Channel.update(type, id).addModerator("thierry").addModerator("josh").request();
Channel.update(type, id).demoteModerator("tommaso").request();
```

Inviting users

```java
Channel.update("messaging", "awesome-chat").invite("nick").request();
```

Accepting an invite

```java
Channel.update("messaging", "awesome-chat")
    .acceptInvite(true)
    .userId("nick")
    .message(MessageRequestObject.builder().text("Nick joined the channel").build())
    .request();
```

Rejecting an invite

```java
Channel.update("messaging", "awesome-chat")
    .acceptInvite(false)
    .userId("nick")
    .request();
```

Freeze a channel

```java
ChannelRequestObject channelRequestObject = ChannelRequestObject.buildFrom(channel);
channelRequestObject.setFrozen(true);
Channel.update(channel.getType(), channel.getId())
    .data(channelRequestObject)
    .message(
        MessageRequestObject.builder()
            .text("Thierry has frozen the channel")
            .userId("Thierry")
            .build())
    .request();
```

Unfreeze a channel

```java
ChannelRequestObject channelRequestObject = ChannelRequestObject.buildFrom(channel);
channelRequestObject.setFrozen(false);
Channel.update(channel.getType(), channel.getId())
    .data(channelRequestObject)
    .message(
        MessageRequestObject.builder()
            .text("Thierry has unfrozen the channel")
            .userId("Thierry")
            .build())
    .request();
```

Add moderators to a channel

```java
Channel.update("livestream", "fortnite").addModerator("thierry").addModerator("tommaso").request();
```

Remove moderators from a channel

```java
Channel.update(type, id).demoteModerator("thierry").request();
```

Enable automatic translation

```java
// enable auto-translation only for this channel
ChannelRequestObject channelRequestObject = ChannelRequestObject.buildFrom(channel);
channelRequestObject.setAutoTranslationEnabled(true);
Channel.update(channel.getType(), channel.getId()).data(channelRequestObject).request();

// ensure all messages are translated in english for this channel
ChannelRequestObject channelRequestObject2 = ChannelRequestObject.buildFrom(channel);
channelRequestObject2.setAutoTranslationEnabled(true);
channelRequestObject2.setAutoTranslationLanguage(Language.EN);
Channel.update(channel.getType(), channel.getId()).data(channelRequestObject2).request();

// auto translate messages for all channels
App.update().autoTranslationEnabled(true).request();
```

Enable/Disable slow mode

```java
// Enable slow mode and set cooldown to 1s
Channel.update("messaging", "general").cooldown(1).request();

// Increase cooldown to 30s
Channel.update("messaging", "general").cooldown(30).request();

// Disable slow mode
Channel.update("messaging", "general").cooldown(0).request();
```

**Delete channel**

```java
Channel.delete(type, id).request();
```

**Delete many channels**

```java
var taskId = Channel.deleteMany(List.of("c:1", "c:2"), DeleteStrategy.HARD).request().getTaskId();
```

**Hide channel**

```java
// hides the channel until a new message is added there
Channel.hide(type, id).userId(userId).request();

// hides the channel until a new message is added there. This also clears the history for the user
Channel.hide(type, id).clearHistory(true).userId(userId).request();
```

**Show channel**

```java
Channel.show(type, id).userId(userId).request();
```

**Truncate channel**

```java
Channel.truncate(type, id).request();
```

**Mute channel**

```java
// mute channel for a user
Channel.mute().channelCid(cid).userId(userId).request();
// mute a channel for 2 weeks
Channel.mute()
    .channelCid(cid)
    .userId(userId)
    .expiration(TimeUnit.MILLISECONDS.convert(14, TimeUnit.DAYS))
    .request();
// mute a channel for 10 seconds
Channel.mute().channelCid(cid).userId(userId).expiration(10000L).request();
// check if a channel is muted for the user
user.getChannelMutes().stream()
    .anyMatch(channelMute -> channelMute.getChannel().getCId().equals(channel.getCId()));
```

**Unmute channel**

```java
Channel.unmute().channelCid(cid).userId(userId).request();
```

**Query members**

Pagination and ordering

```java
// returns up to 100 members ordered by created_at ascending
Channel.queryMembers().type(type).id(id).request();
// returns up to 100 members ordered by created_at descending
Channel.queryMembers()
    .type(type)
    .id(id)
    .sort(Sort.builder().field("created_at").direction(Direction.DESC).build())
    .request();
// returns up to 100 members ordered by user_id descending
Channel.queryMembers()
    .type(type)
    .id(id)
    .sort(Sort.builder().field("user_id").direction(Direction.DESC).build())
    .request();
// paginate by user_id in descending order
Channel.queryMembers()
    .type(type)
    .id(id)
    .sort(Sort.builder().field("user_id").direction(Direction.DESC).build())
    .userIdLt(lastMember.getUserId())
    .request();
// paginate by created at in ascending order
Channel.queryMembers()
    .type(type)
    .id(id)
    .sort(Sort.builder().field("created_at").direction(Direction.ASC).build())
    .createdAtAfter(lastMember.getCreatedAt())
    .request();
// paginate using offset
Channel.queryMembers().type(type).id(id).offset(20);
```

Few examples

```java
// query members by user.name
Channel.queryMembers().filterCondition("name", "tommaso").request();

// autocomplete members by user name
Channel.queryMembers()
    .filterCondition(FilterCondition.autocomplete("name", "tomm"))
    .request();

// query member by id
Channel.queryMembers().filterCondition("user_id", "tommaso").request();

// query multiple members by id
Channel.queryMembers()
    .filterCondition(
        FilterCondition.in("user_id", "tommaso", "thierry"))
    .request();

// query channel moderators
Channel.queryMembers().filterCondition("is_moderator", true).request();

// query for banned members in channel
Channel.queryMembers().filterCondition("banned", true).request();

// query members with pending invites
Channel.queryMembers().filterCondition("invite", "pending").request();

// query members who joined the channel directly or accepted an invite
Channel.queryMembers().filterCondition("joined", true).request();

// query members who have rejected invite or have pending invite
Channel.queryMembers().filterCondition("joined", false).request();

// query all the members
Channel.queryMembers().request();

// order results by member created at descending
Channel.queryMembers()
    .sort(Sort.builder().field("created_at").direction(Direction.DESC).build())
    .request();

// query for user.email (currently the only supported custom field)
Channel.queryMembers().filterCondition("user.email", "awesome@getstream.io").request();
```

**Export channels**

```java
String taskId =
    Channel.export()
        .channel(
            ChannelExportRequestObject.builder()
                .type("livestream")
                .id("white_room")
                .messagesSince(
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                        .parse("2020-11-10T09:30:00.000Z"))
                .messagesSince(
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                        .parse("2020-11-10T11:30:00.000Z"))
                .build())
        .request()
        .getTaskId();
```

**Export channels status**

```java
ChannelExportStatusResponse response = Channel.exportStatus(taskId).request();
System.out.println(response.getStatus()); // the status for this task
System.out.println(response.getResult()); // the result object, only present if the task is completed
System.out.println(response.getResult().getUrl()); // the link to the JSON export
System.out.println(response.getError()); // if not null the description of the task error
```

**Create channel type**

Standard

```java
ChannelType.create()
    .name("public")
    .permission(
        PermissionRequestObject.builder()
            .name("Allow reads for all")
            .priority(999)
            .resources(List.of(ResourceAction.READ_CHANNEL, ResourceAction.CREATE_MESSAGE))
            .action(Action.ALLOW)
            .build())
    .permission(
        PermissionRequestObject.builder()
            .name("Deny all")
            .priority(1)
            .resources(List.of(ResourceAction.ALL))
            .action(Action.DENY)
            .build())
    .mutes(false)
    .reactions(false)
    .request();
```

With command

```java
ChannelType.create().name("support-channel-type").commands(Arrays.asList("ticket")).request();
```

**List channel types**

```java
ChannelType.list().request();
```

**Get channel type**

```java
ChannelType.get("public").request();
```

**Update channel type**

Update a few elements in channel type

```java
ChannelType.update("public")
    .permission(
        PermissionRequestObject.builder()
            .name("Allow reads for all")
            .priority(999)
            .resources(Arrays.asList(Resource.READ_CHANNEL, Resource.CREATE_MESSAGE))
            .roles(Arrays.asList("*"))
            .action(Action.ALLOW)
            .build())
    .permission(
        PermissionRequestObject.builder()
            .name("Deny all")
            .priority(1)
            .resources(Arrays.asList(Resource.ALL))
            .roles(Arrays.asList("*"))
            .action(Action.DENY)
            .build())
    .replies(false)
    .commands(Arrays.asList("all"))
    .request();
```

Update channel type features

```java
ChannelType.update("public")
    .typingEvents(false)
    .readEvents(true)
    .connectEvents(true)
    .search(false)
    .reactions(true)
    .replies(false)
    .mutes(true)
    .request();
```

Update channel type settings

```java
ChannelType.update("public")
    .automod(AutoMod.DISABLED)
    .messageRetention("7")
    .maxMessageLength(140)
    .commands(Arrays.asList("ban", "unban"))
    .request();
```

Grant the UseFrozenChannel permission

```java
List<PermissionRequestObject> permissions =
    ChannelType.get("messaging").request().getPermissions().stream()
        .map(policy -> PermissionRequestObject.buildFrom(policy))
        .collect(Collectors.toList());
permissions.add(
    PermissionRequestObject.builder()
        .name("Admin users can use frozen channels")
        .priority(600)
        .resources(Arrays.asList(Resource.USE_FROZEN_CHANNEL))
        .roles(Arrays.asList("admin"))
        .owner(false)
        .action(Action.ALLOW)
        .build());
ChannelType.update("messaging").permissions(permissions).request();
```

Set permissions

```java
ChannelType.update("messaging")
    .permission(
        PermissionRequestObject.builder()
            .name("Admin users can perform any action")
            .priority(600)
            .resources(Arrays.asList(Resource.ALL))
            .roles(Arrays.asList("admin"))
            .owner(false)
            .action(Action.ALLOW)
            .build())
    .permission(
        PermissionRequestObject.builder()
            .name("Anonymous users are not allowed")
            .priority(500)
            .resources(Arrays.asList(Resource.ALL))
            .roles(Arrays.asList("anonymous"))
            .owner(false)
            .action(Action.DENY)
            .build())
    .permission(
        PermissionRequestObject.builder()
            .name("Users can modify their own messages")
            .priority(400)
            .resources(Arrays.asList(Resource.UPDATE_MESSAGE))
            .roles(Arrays.asList("user"))
            .owner(true)
            .action(Action.ALLOW)
            .build())
    .permission(
        PermissionRequestObject.builder()
            .name("Users can create channels")
            .priority(300)
            .resources(Arrays.asList(Resource.CREATE_CHANNEL))
            .roles(Arrays.asList("user"))
            .owner(false)
            .action(Action.ALLOW)
            .build())
    .permission(
        PermissionRequestObject.builder()
            .name("Members of a channel can read and send messages")
            .priority(200)
            .resources(Arrays.asList(Resource.READ_CHANNEL, Resource.CREATE_MESSAGE))
            .roles(Arrays.asList("channel_member"))
            .owner(false)
            .action(Action.ALLOW)
            .build())
    .permission(
        PermissionRequestObject.builder()
            .name("Anything not matching the previous list should not be allowed")
            .priority(100)
            .resources(Arrays.asList(Resource.ALL))
            .roles(Arrays.asList("*"))
            .owner(false)
            .action(Action.DENY)
            .build())
    .request();
```

**Delete channel type**

```java
ChannelType.delete("public").request();
```

**Send new message**

Simple example

```java
Message.send(type, id)
    .message(
        MessageRequestObject.builder()
            .text(
                "@Josh I told them I was pesca-pescatarian. Which is one who eats solely fish who eat other fish.")
            .userId(userId)
            .build())
    .request();
```

Complex example

```java
Message.send(type, id)
    .message(
        MessageRequestObject.builder()
            .text(
                "@Josh I told them I was pesca-pescatarian. Which is one who eats solely fish who eat other fish.")
            .attachment(
                AttachmentRequestObject.builder()
                    .type("image")
                    .assetURL("https://bit.ly/2K74TaG")
                    .thumbURL("https://bit.ly/2Uumxti")
                    .additionalField("myCustomField", 123)
                    .build())
            .mentionedUsers(Arrays.asList(josh.getId()))
            .additionalField("anotherCustomField", 234)
            .userId(userId)
            .build())
    .skipPush(true)
    .request();
```

With url enrichment

```java
Message.send(type, id)
    .message(
        MessageRequestObject.builder()
            .text(
                "Check this bear out https://imgur.com/r/bears/4zmGbMN")
            .userId(userId)
            .build())
    .request();
```

Create a thread

```java
Message.send(type, id)
    .message(
        MessageRequestObject.builder()
            .text("Hey, I am replying to a message!")
            .parentId(parentId)
            .showInChannel(false)
            .userId(userId)
            .build())
    .request();
```

Quote a message

```java
// Create the initial message
Message.send(type, id)
    .message(
        MessageRequestObject.builder()
            .id("first_message_id")
            .text("The initial message")
            .userId(userId)
            .build())
    .request();

// Quote the initial message
Message.send(type, id)
    .message(
        MessageRequestObject.builder()
            .text("This is the first message that quotes another message")
            .quotedMessageId("first_message_id")
            .userId(userId)
            .build())
    .request();
```

Silent message

```java
Message.send(type, id)
    .message(
        MessageRequestObject.builder()
            .text("You completed your trip")
            .userId(systemUserId)
            .silent(true)
            .attachment(
                AttachmentRequestObject.builder()
                    .type("trip")
                    .additionalField("tripData", tripData)
                    .build())
            .build())
    .request();
```

**Get message**

```java
Message.get(messageId).request();
```

**Update message**

Standard

```java
Message message = Message.get("123").request().getMessage();
MessageRequestObject messageRequestObject = MessageRequestObject.buildFrom(message);
messageRequestObject.setText("the edited version of my text");
Message.update(message.getId()).message(messageRequestObject).request();
```

Pin and unpin message

```java
// create pinned message
Message message =
    Message.send(channelType, channelId)
        .message(
            MessageRequestObject.builder()
                .text("my message")
                .pinned(true)
                .pinExpires(
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                        .parse("2077-01-01T00:00:00Z"))
                .userId(userId)
                .build())
        .request()
        .getMessage();

// unpin message
MessageRequestObject messageRequestObject = MessageRequestObject.buildFrom(message);
messageRequestObject.setPinned(false);
Message message2 =
    Message.update(message.getId()).message(messageRequestObject).request().getMessage();

// pin message for 120 seconds
MessageRequestObject messageRequestObject2 = MessageRequestObject.buildFrom(message2);
messageRequestObject2.setPinned(true);
Calendar calendar = Calendar.getInstance();
calendar.add(Calendar.SECOND, 120);
messageRequestObject2.setPinExpires(calendar.getTime());
Message message3 =
    Message.update(message2.getId()).message(messageRequestObject2).request().getMessage();

// change message expiration to 2077
MessageRequestObject messageRequestObject3 = MessageRequestObject.buildFrom(message3);
messageRequestObject3.setPinExpires(
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse("2077-01-01T00:00:00Z"));
Message message4 =
    Message.update(message3.getId()).message(messageRequestObject3).request().getMessage();

// remove expiration date from pinned message
MessageRequestObject messageRequestObject4 = MessageRequestObject.buildFrom(message4);
messageRequestObject4.setPinExpires(null);
Message.update(message4.getId()).message(messageRequestObject4).request();
```

**Partial update message**

```java
Message originalMessage =
    Message.send(channelType, channelId)
        .message(
            MessageRequestObject.builder()
                .text("this message is about to be partially updated")
                .additionalField("color", "red")
                .additionalField("details", Collections.singletonMap("status", "pending"))
                .userId(userId)
                .build())
        .request()
        .getMessage();

// partial update message text
Message updated =
    Message.partialUpdate(originalMessage.getId())
        .setValue("text", "the text was partial updated")
        .userId(userId)
        .request()
        .getMessage();

// unset color property
updated =
    Message.partialUpdate(originalMessage.getId())
        .unsetValue("color")
        .userId(userId)
        .request()
        .getMessage();

// set nested property
updated =
    Message.partialUpdate(originalMessage.getId())
        .setValue("details.status", "complete")
        .userId(userId)
        .request()
        .getMessage();
```

**Delete message**

```java
Message.delete(messageId).request();

// hard delete the message (works only server-side)
Message.delete(messageId).hard(true).request();
```

**Upload file or image**

```java
String pdfFileUrl =
    Message.uploadFile("messaging", "general", userId, "application/pdf")
        .file(new File("./helloworld.pdf"))
        .request()
        .getFile();
String pngFileUrl =
    Message.uploadImage("messaging", "general", userId, "image/png")
        .file(new File("./helloworld.png"))
        .request()
        .getFile();
Message.send("messaging", "general")
    .message(
        MessageRequestObject.builder()
            .text("Check out what I have uploaded in parallel")
            .attachment(
                AttachmentRequestObject.builder()
                    .type("image")
                    .assetURL(pngFileUrl)
                    .thumbURL(pngFileUrl)
                    .build())
            .attachment(
                AttachmentRequestObject.builder().type("url").assetURL(pdfFileUrl).build())
            .userId(userId)
            .build())
    .request();
```

**Send reaction**

Standard

```java
// Add reaction 'love' with custom field
Reaction.send(messageId)
    .enforceUnique(false)
    .reaction(
        ReactionRequestObject.builder()
            .type("love")
            .additionalField("myCustomField", 123)
            .userId(userId)
            .build())
    .request();

// Add reaction 'like' and replace all other reactions of this user by it
Reaction.send(messageId)
    .enforceUnique(true)
    .reaction(ReactionRequestObject.builder().type("like").userId(userId).build())
    .request();
```

Clap reaction

```java
// user claps 5 times on a message
Reaction.send(messageId)
    .enforceUnique(false)
    .reaction(ReactionRequestObject.builder().type("clap").score(5).userId(userId).build())
    .request();
// same user claps 20 times more
Reaction.send(messageId)
    .enforceUnique(false)
    .reaction(ReactionRequestObject.builder().type("clap").score(25).userId(userId).build())
    .request();
```

**Delete reaction**

```java
Reaction.delete(messageId, "love").request();
```

**Get reactions**

```java
// get the first 10 reactions
Reaction.list(messageId).limit(10).request();

// get 3 reactions past the first 10
Reaction.list(messageId).limit(3).offset(10).request();
```

**Get replies**

```java
// retrieve the first 20 messages inside the thread
Message.getReplies(parentMessageId).limit(20).request();

// retrieve the 20 more messages before the message with id "42"
Message.getReplies(parentMessageId).limit(20).idLte("42").request();
```

**Search messages**

Search by user and text

```java
Message.search()
    .filterCondition(FilterCondition.in("members", "john"))
    .messageFilterCondition(
        FilterCondition.autocomplete("text", "supercalifragilisticexpialidocious"))
    .limit(2)
    .offset(0)
    .request();
```

Search messages with attachment

```java
// Search by Attachment
Message.search().messageFilterCondition(FilterCondition.exists("attachments")).request();
```

**Flag message**

```java
Message.flag(messageId).userId(userId).request();
```

**Mute user**

```java
// mute
User.mute().targetId(targetUserId).userId(userId).request();

// mute for 60 minutes
User.mute().targetId(targetUserId).timeout(60).userId(userId).request();
```

**Ban/unban user**

Standard

```java
// ban a user for 60 minutes from all channel
User.ban()
    .targetUserId("eviluser")
    .timeout(60)
    .reason("Banned for one hour")
    .bannedById(userId)
    .request();
// ban a user and their IP address for 24 hours
User.ban()
    .targetUserId("eviluser")
    .timeout(24 * 60)
    .ipBan(true)
    .reason("Please come back tomorrow")
    .bannedById(userId)
    .request();

// ban a user from the livestream:fortnite channel
User.ban()
    .targetUserId("eviluser")
    .id("livestream:fortnite")
    .reason("Profanity is not allowed here")
    .bannedById(userId)
    .request();

// remove ban from channel
User.unban("eviluser").type("livestream").id("fortnite").request();

// remove global ban
User.unban("eviluser").request();
```

Shadow ban

```java
// shadow ban a user from all channels
User.ban().targetUserId("eviluser").shadow(true).bannedById(userId).request();

// shadow ban a user from a channel
User.ban().targetUserId("eviluser").type(type).id(id).shadow(true).bannedById(userId).request();

// remove shadow ban from channel
User.unban("eviluser").type(type).id(id).request();

// remove global shadow ban
User.unban("eviluser").request();
```

**Query Banned Users**

Standard

```java
// retrieve the list of banned users
User.list().filterCondition("banned", true).limit(10).offset(0).request();

// query for banned members from one channel
User.queryBanned().filterCondition("channel_cid", "livestream:123").request();
```

With pagination

```java
// get the bans for channel livestream:123 in descending order
List<Ban> bans =
    User.queryBanned()
        .filterCondition("channel_cid", "livestream:123")
        .sort(Sort.builder().field("created_at").direction(Direction.DESC).build())
        .request()
        .getBans();

// get the next page of bans for the same channel
List<Ban> nextPageBans =
    User.queryBanned()
        .filterCondition("channel_cid", "livestream:123")
        .createdAtBefore(bans.get(bans.size() - 1).getCreatedAt())
        .sort(Sort.builder().field("created_at").direction(Direction.DESC).build())
        .request()
        .getBans();
```

**Create block list**

```java
// add a new block list for this app
Blocklist.create().name("no-cakes").words(Arrays.asList("fudge", "cream", "sugar")).request();

// use the block list for all channels of type messaging
ChannelType.update("messaging")
    .blocklist("no-cakes")
    .blocklistBehavior(BlocklistBehavior.BLOCK)
    .request();
```

**List block lists**

```java
Blocklist.list().request();
```

**Get block list**

```java
Blocklist.get("no-cakes").request();
```

**Update block list**

```java
Blocklist.update("no-cakes").words(Arrays.asList("fudge", "cream", "sugar", "vanilla")).request();
```

**Delete block list**

```java
Blocklist.delete("no-cakes").request();
```

**Send event**

```java
// sends an event to all connected clients on the channel
Event.send(channelType, channelId)
    .event(
        EventRequestObject.builder()
            .type("friendship_request")
            .additionalField("text", "Hey there, long time no see!")
            .userId(userId)
            .build())
    .request();
```

**Send user event**

```java
Event.sendUserCustom(targetUserId)
    .event(
        EventUserCustomRequestObject.builder()
            .type("friendship_request")
            .additionalField("text", "Hey there, long time no see!")
            .build())
    .request();
```

**Create command**

```java
Command.create()
    .name("ticket")
    .description("Create a support ticket")
    .args("[description]")
    .setValue("support_commands_set")
    .request();
```

**List commands**

```java
Command.list().request();
```

**Get command**

```java
Command.get("ticket").request();
```

**Update command**

```java
Command.update("ticket").description("Create customer support tickets").request();
```

**Delete command**

```java
Command.delete("ticket").request();
```

**Check SQS**

```java
// set your SQS queue details
App.update()
    .sqsKey("yourkey")
    .sqsKey("yoursecret")
    .sqsUrl("https://sqs.us-east-1.amazonaws.com/123456789012/MyQueue")
    .request();

// send a test message
App.checkSqs().request();
```

**Create device**

```java
Device.create().id("firebase-token").userId(userId).request();
```

**Delete device**

```java
Device.delete("firebase-token", userId).request();
```

**List devices**

```java
Device.list(targetUserId).request();
```

**Check push**

```java
App.checkPush().messageId(messageId).userId(userId).request();
```

**Get rate limits**

```java
// 1. Get Rate limits
App.getRateLimits().request();
// 2. Get Rate limits, iOS and Android
App.getRateLimits().ios(true).android(true).request();
// 3. Get Rate limits for specific endpoints
App.getRateLimits().endpoint("QueryChannels").endpoint("SendMessage").request();
```

**Export user**

```java
User.export(userId).request();
```

**Deactivate user**

```java
User.deactivate(targetUserId).request();

User.deactivate(targetUserId).createdById(userId).markMessagesDeleted(true).request();
```

**Reactivate user**

```java
User.reactivate(targetUserId).request();

User.reactivate(targetUserId).restoreMessages(true).name("I am back").createdById(userId).request();
```

**Delete user**

Standard

```java
User.delete(targetUserId).markMessagesDeleted(false).request();
```

Hard delete

```java
User.delete(targetUserId)
    .deleteConversationChannels(true)
    .markMessagesDeleted(true)
    .hardDelete(true)
    .request();
```

**Delete many users**

```java
var taskId = User.deleteMany(List.of("u1", "u2"))
        .deleteUserStrategy(DeleteStrategy.SOFT)
        .deleteMessagesStrategy(DeleteStrategy.HARD)
        .request().getTaskId();

var taskStatusResponse = TaskStatus.get(taskId).request();
// "completed".equals(taskStatusResponse.status);
```

**Translate message**

```java
Message.send(channelType, channelId)
    .message(
        MessageRequestObject.builder()
            .id(messageId)
            .text("Hello, I would like to have more information about your product.")
            .userId(userId)
            .build())
    .request();
// returns the message.text translated into French
MessageTranslateResponse response =
    Message.translate(messageId).language(Language.FR).request();
System.out.println(response.getMessage().getI18n().get("fr_text"));
// "Bonjour, J'aimerais avoir plus d'informations sur votre produit."
```

**Mark all read**

```java
Channel.markAllRead().userId(userId).request();
```

**Mark read**

```java
Channel.markRead(channelType, channelId).request();
```

**Delete file**

```java
Message.deleteFile(channelType, channelId, url).request();
```

**Delete image**

```java
Message.deleteImage(channelType, channelId, url).request();
```

**Flag user**

```java
User.flag(targetUserId).userId(userId).request();
```

**Get many messages**

```java
Message.getMany(channelType, channelId, Arrays.asList(messageId1, messageId2)).request();
```

**Run message command action**

```java
Message.runCommandAction(messageId)
    .formData(Collections.singletonMap("image_action", "send"))
    .userId(userId)
    .request();
```

**Unflag message**

```java
Message.unflag(messageId).userId(userId).request();
```

**Unflag user**

```java
User.unflag(targetUserId).userId(userId).request();
```

**Create custom permission**

```java
Permission.create().id("MyCustomId").name("My custom permission").action("DeleteChannel").request());
```

**Create custom role**

```java
Role.create().name("My custom role").request();
```

**Delete custom permission**

```java
Permission.delete("MyCustomId").request()
```

**Delete custom role**

```java
Role.delete("My custom role").request();
```

**Get custom permission**

```java
Permission.get("MyCustomId").request()
```

**List custom permission**

```java
Permission.list().request();
```

**List custom roles**

```java
Role.list().request();
```

**Update custom permission**

```java
Permission.update("MyCustomId", "My custom permission")
          .action("DeleteChannel")
          .owner(true)
          .request());
```

**Get App Settings**

```java
App.get().request();
```

**Create guest**

```java
User.createGuest()
    .user(UserRequestObject.builder().id(guestId).name("Guest user").build())
    .request();
```

**Unmute user**

```java
User.unmute().singleTargetId(targetUserId).userId(userId).request();
```

**Query message flags**

```java
Message.queryFlags().request();
```

**Get task status**

```java
var taskId = "123";
var taskStatusResponse = TaskStatus.get(taskId).request();
// {LinkedHashMap@4341}  size = 2
// id = "b9843be8-bcf0-484b-af01-726e1d3b82a3"
// status = "completed"
// createdAt = {Date@4339} "Tue Nov 02 17:54:28 CET 2021"
// updatedAt = {Date@4340} "Tue Nov 02 17:54:32 CET 2021"
// rateLimit = {RateLimit@4342} "RateLimit(limit=300, remaining=299, reset=Tue Nov 02 17:55:00 CET 2021)"
// duration = "10.07ms"
```

**Verify webhook**

```java
// signature comes from the HTTP header x-signature
boolean valid =  App.verifyWebhook(body, signature)
```

## Changelog
See [the detailed changes](CHANGELOG.md).

## FAQ

1. If you get this exception: `java.lang.ClassNotFoundException: io.jsonwebtoken.SignatureAlgorithm`:

See issue [#16](https://github.com/GetStream/stream-chat-java/issues/16) for a work around. We only provide runtime only dependency for JWT per [recommendation](https://github.com/jwtk/jjwt#understanding-jjwt-dependencies). That's why it might be missing in your runtime and by addding implementation library into your deps, it should be gone.

## Contribute
See [the guide to contribute](CONTRIBUTING.md).

## License

Project is licensed under the [Stream License](LICENSE).

## We are hiring!

We've recently closed a [$38 million Series B funding round](https://techcrunch.com/2021/03/04/stream-raises-38m-as-its-chat-and-activity-feed-apis-power-communications-for-1b-users/) and we keep actively growing.
Our APIs are used by more than a billion end-users, and you'll have a chance to make a huge impact on the product within a team of the strongest engineers all over the world.

Check out our current openings and apply via [Stream's website](https://getstream.io/team/#jobs).
