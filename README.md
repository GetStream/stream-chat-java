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
**Channel types**
- Create channel type
- Delete channel type
- Get channel type
- List channel types
- Update channel type

**Channels**
- Delete channel
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

### All examples
**Upsert users**

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

**Partially update user**

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

**Update App Settings**

```java
App.update().enforceUniqueUsernames("app").request();
```

```java
App.update().enforceUniqueUsernames("team").request();
```

**Query users**

```java
User.list()
    .filterCondition(
        "id", Collections.singletonMap("$in", Arrays.asList("john", "jack", "jessie")))
    .request();
```

```java
UserListResponse response = User.list()
    .filterCondition(
        "id", Collections.singletonMap("$in", Arrays.asList("jessica")))
    .filterCondition("last_active", -1)
    .filterCondition("presence", true)
    .request();
```

```java
List<User> bannedUsers = User.list()
    .filterCondition("banned", true)
    .request()
    .getUsers();
```

**Get or create channel (type,id)**

```java
Channel.getOrCreate("messaging", "travel")
    .data(
        ChannelRequestObject.builder()
            .additionalField("name", "Awesome channel about traveling")
            .createdBy(UserRequestObject.builder().id("myuserid").build())
            .build())
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

**Create channel type**

```java
ChannelType.create()
    .name("public")
    .permission(
        PermissionRequestObject.builder()
            .name("Allow reads for all")
            .priority(999)
            .resources(Arrays.asList("ReadChannel", "CreateMessage"))
            .action(Action.ALLOW)
            .build())
    .permission(
        PermissionRequestObject.builder()
            .name("Deny all")
            .priority(1)
            .resources(Arrays.asList("*"))
            .action(Action.DENY)
            .build())
    .mutes(false)
    .reactions(false)
    .request();
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

```java
ChannelType.update("public")
    .permission(
        PermissionRequestObject.builder()
            .name("Allow reads for all")
            .priority(999)
            .resources(Arrays.asList("ReadChannel", "CreateMessage"))
            .roles(Arrays.asList("*"))
            .action(Action.ALLOW)
            .build())
    .permission(
        PermissionRequestObject.builder()
            .name("Deny all")
            .priority(1)
            .resources(Arrays.asList("*"))
            .roles(Arrays.asList("*"))
            .action(Action.DENY)
            .build())
    .replies(false)
    .commands(Arrays.asList("all"))
    .request();
```

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

```java
ChannelType.update("public")
    .automod(AutoMod.DISABLED)
    .messageRetention("7")
    .maxMessageLength(140)
    .commands(Arrays.asList("ban", "unban"))
    .request();
```

**Delete channel type**

```java
ChannelType.delete("public").request();
```

**Delete channel**

```java

```

**Export channels**

```java

```

**Export channels status**

```java

```

**Hide channel**

```java

```

**Mark all read**

```java

```

**Mark read**

```java

```

**Mute channel**

```java

```

**Partially update channel**

```java

```

**Query members**

```java

```

**Show channel**

```java

```

**Truncate channel**

```java

```

**Unmute channel**

```java

```

**Update channel**

```java

```

**Create command**

```java

```

**Delete command**

```java

```

**Get command**

```java

```

**List commands**

```java

```

**Update command**

```java

```

**Create device**

```java

```

**Delete device**

```java

```

**List devices**

```java

```

**Send event**

```java

```

**Send user event**

```java

```

**Delete file**

```java

```

**Delete image**

```java

```

**Upload file**

```java

```

**Upload image**

```java

```

**Delete message**

```java

```

**Delete reaction**

```java

```

**Flag message**

```java

```

**Flag user**

```java

```

**Get many messages**

```java

```

**Get message**

```java

```

**Get reactions**

```java

```

**Get replies**

```java

```

**Run message command action**

```java

```

**Search messages**

```java

```

**Send new message**

```java

```

**Send reaction**

```java

```

**Translate message**

```java

```

**Unflag message**

```java

```

**Unflag user**

```java

```

**Update message**

```java

```

**Create custom permission**

```java

```

**Create custom role**

```java

```

**Delete custom permission**

```java

```

**Delete custom role**

```java

```

**Get custom permission**

```java

```

**List custom permission**

```java

```

**List custom roles**

```java

```

**Update custom permission**

```java

```

**Check push**

```java

```

**Check SQS**

```java

```

**Create block list**

```java

```

**Delete block list**

```java

```

**Get App Settings**

```java

```

**Get block list**

```java

```

**Get rate limits**

```java

```

**List block lists**

```java

```

**Update block list**

```java

```

**Ban user**

```java

```

**Create guest**

```java

```

**Deactivate user**

```java

```

**Delete user**

```java

```

**Export user**

```java

```

**Mute user**

```java

```

**Query Banned Users**

```java

```

**Reactivate user**

```java

```

**Unban user**

```java

```

**Unmute user**

```java

```

## Contribute
> See [The guide to contribute](CONTRIBUTING.md)