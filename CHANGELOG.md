# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

## [1.6.0](https://github.com/GetStream/stream-chat-java/compare/1.5.0...1.6.0) (2022-01-18)


### Features

* add grants to channeltype ([#37](https://github.com/GetStream/stream-chat-java/issues/37)) ([2a2d5fb](https://github.com/GetStream/stream-chat-java/commit/2a2d5fb2b3ad7db4427bfad9bda3f6c92b33b31f))
* add hidehistory to addmember ([#35](https://github.com/GetStream/stream-chat-java/issues/35)) ([ce27c41](https://github.com/GetStream/stream-chat-java/commit/ce27c4174aa7e922d9f2ce9aca23d62bc2a25037))
* add missing requests ([#39](https://github.com/GetStream/stream-chat-java/issues/39)) ([237c7cb](https://github.com/GetStream/stream-chat-java/commit/237c7cbe2abf9462835532a09b5f6eae13c999cc))
* add options to truncate ([#34](https://github.com/GetStream/stream-chat-java/issues/34)) ([25c3fb9](https://github.com/GetStream/stream-chat-java/commit/25c3fb9538c999e1a33557a10af21916746d81f4))
* extend app config ([#36](https://github.com/GetStream/stream-chat-java/issues/36)) ([cdc129d](https://github.com/GetStream/stream-chat-java/commit/cdc129d48b91019b5a2e7382391c0e0f35754511))

## [1.5.0] - 2021-11-12

- Make StreamException constructors public
- Add ability to use SDK with multiple clients. Now, we can pass a custom client implementation to every
request object.
```java
    var client = new DefaultClient();
    var response = App.get().withClient(client).request();
```
- Add `App.verifyWebhookSignature`
- Add `User.create_token(String apiSecret, String userId, Date expiresAt, Date issuedAt)`

## [1.4.0] - 2021-11-03

- Add deleteMany for Channels
- Add deleteMany for Users
- Add TaskStatus to track async operations results

## [1.3.3] - 2021-11-01

- Add async url enrichment configuration

## [1.3.2] - 2021-10-27

- Fix channel type creation with commands

## [1.3.1] - 2021-10-18

- Support setting resources on channel types
- Add FAQ for common problems

## [1.3.0] - 2021-10-11

- Support for permissions v2
- Use edge by default
- Fix timeout setup and application into the requests
- Fix some nullability of some types (channel created_by_id, user id etc.)
- Fix NPE in some tests
- Improve CI to cancel unnecessary tasks

## [1.2.0] - 2021-07-09

- Add support for permission v2
- Fix token creation
- Make formatting a part of CI build

## [1.1.0] - 2021-06-25

- Add improved search support.
- Add javadoc publish into github pages.

## [1.0.1] - 2021-06-24

- Fix of release process. Grab this version over 1.0.0

## [1.0.0] - 2021-06-24

### Added

- Initial release of SDK with complete support of API.
