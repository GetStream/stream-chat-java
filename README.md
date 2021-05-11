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

Query banned users

```java
List<User> bannedUsers = User.list()
    .filterCondition("banned", true)
    .request()
    .getUsers();
```

Query users with teams

```java
//TODO

// search for users with name Jordan that are part of the red team 
client.queryUsers({ 
   $and: [ 
      { name: { $eq: "Jordan" } }, 
      { teams: { $contains: "red" } } 
   ], 
}); 

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
    .filterCondition("members", Collections.singletonMap("$in", Arrays.asList(userId)))
    .request();

// retrieve all muted channels
Channel.list()
    .filterCondition("muted", true)
    .filterCondition("members", Collections.singletonMap("$in", Arrays.asList(userId)))
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
//TODO after requestobject helper
const update = await channel.update( 
	{ frozen: true },  
	{ text: 'Thierry has frozen the channel', user_id: "Thierry" } 
)
```

Unfreeze a channel

```java
//TODO after requestobject helper
const update = await channel.update( 
	{ frozen: false },  
	{ text: 'Thierry has unfrozen the channel', user_id: "Thierry" } 
) 

```

Add moderators to a channel

```java
Channel.update("livestream", "fortnite").addModerator("thierry").addModerator("tommaso").request();
```

Remove moderators from a channel

```java
Channel.update(type, id).demoteModerator("thierry").request();
```

**Delete channel**

```java
Channel.delete(type, id).request();
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
    .filterCondition("name", Collections.singletonMap("$autocomplete", "tomm"))
    .request();

// query member by id
Channel.queryMembers().filterCondition("user_id", "tommaso").request();

// query multiple members by id
Channel.queryMembers()
    .filterCondition(
        "user_id", Collections.singletonMap("$in", Arrays.asList("tommaso", "thierry")))
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

```java
ChannelType.create()
    .name("public")
    .permission(
        PermissionRequestObject.builder()
            .name("Allow reads for all")
            .priority(999)
            .resources(Arrays.asList(Resource.READ_CHANNEL, Resource.CREATE_MESSAGE))
            .action(Action.ALLOW)
            .build())
    .permission(
        PermissionRequestObject.builder()
            .name("Deny all")
            .priority(1)
            .resources(Arrays.asList(Resource.ALL))
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
TODO after PermissionRequestObject helper from permission

const useFrozenChannel = new Permission("Admin users can use frozen channels", 600, ["UseFrozenChannel"], ["admin"], false, Allow);  
const { permissions } = await client.getChannelType("messaging"); 
permissions.push(useFrozenChannel); 
await client.updateChannelType("messaging", { permissions }); 

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

**Mark all read**

```java

```

**Mark read**

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