# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

## [1.11.0](https://github.com/GetStream/stream-chat-java/compare/1.10.0...1.11.0) (2022-05-13)


### Features

* **push:** add apn template fields ([#67](https://github.com/GetStream/stream-chat-java/issues/67)) ([3627c9b](https://github.com/GetStream/stream-chat-java/commit/3627c9b6e027b0d2b70b23021b532efe84068c82))

## [1.10.0](https://github.com/GetStream/stream-chat-java/compare/1.9.0...1.10.0) (2022-04-13)


### Features

* add commands to update ([#58](https://github.com/GetStream/stream-chat-java/issues/58)) ([9fc42e0](https://github.com/GetStream/stream-chat-java/commit/9fc42e0df71ec15f8d2d86ea2414fdf12ed29848))
* add moderation apis ([#56](https://github.com/GetStream/stream-chat-java/issues/56)) ([106ded4](https://github.com/GetStream/stream-chat-java/commit/106ded442f4b89cb7e9ebded98845d914125215e))
* add provider fields ([#59](https://github.com/GetStream/stream-chat-java/issues/59)) ([e6e1d44](https://github.com/GetStream/stream-chat-java/commit/e6e1d44428379b6e75a9827a8b350f3571497249))
* add push provider ([#57](https://github.com/GetStream/stream-chat-java/issues/57)) ([6a655fc](https://github.com/GetStream/stream-chat-java/commit/6a655fc939f184412cc65b8663bf8332b2461976))
* add reminders ([#60](https://github.com/GetStream/stream-chat-java/issues/60)) ([0df895d](https://github.com/GetStream/stream-chat-java/commit/0df895d0e9e5218af090686bb65a73b29211112d))
* **app_settings:** add offlineonly ([#62](https://github.com/GetStream/stream-chat-java/issues/62)) ([d334f6a](https://github.com/GetStream/stream-chat-java/commit/d334f6ab0a8911c4db1b1684cc5e8236ad069203))
* **permissions:** add channel role property ([#64](https://github.com/GetStream/stream-chat-java/issues/64)) ([1626ff9](https://github.com/GetStream/stream-chat-java/commit/1626ff921e81fd17652ddd3428e8d6edfeabc39e))
* **truncate:** add truncated by id ([#61](https://github.com/GetStream/stream-chat-java/issues/61)) ([c302c7a](https://github.com/GetStream/stream-chat-java/commit/c302c7a819d102c73557c453903d0b0f7240c314))

## [1.9.0](https://github.com/GetStream/stream-chat-java/compare/1.6.0...1.9.0) (2022-03-18)


### Features

* add missing properties for export channel ([#51](https://github.com/GetStream/stream-chat-java/issues/51)) ([5a0706e](https://github.com/GetStream/stream-chat-java/commit/5a0706e0cbbebd1b5487db1ef1ea55b519c1c6b2))
* set pool connection pool lifetime to 59seconds ([#50](https://github.com/GetStream/stream-chat-java/issues/50)) ([3037209](https://github.com/GetStream/stream-chat-java/commit/3037209edf501ed5007650d119171b0bfcdce4dd))


### Bug Fixes

* additional fields mutable ([#53](https://github.com/GetStream/stream-chat-java/issues/53)) ([3f33a59](https://github.com/GetStream/stream-chat-java/commit/3f33a595fb373a11340c3b385d437d197e5b62a1))

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
