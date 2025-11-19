# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### [1.37.1](https://github.com/GetStream/stream-chat-java/compare/1.37.0...1.37.1) (2025-11-19)

## [1.37.0](https://github.com/GetStream/stream-chat-java/compare/1.36.0...1.37.0) (2025-11-12)


### Features

* add hide_history_before option when adding members ([#214](https://github.com/GetStream/stream-chat-java/issues/214)) ([d5158cd](https://github.com/GetStream/stream-chat-java/commit/d5158cd7125cbe7a361ce2bb11b01ed39f72860e))
* add support for filter tags to channels ([#215](https://github.com/GetStream/stream-chat-java/issues/215)) ([f6dc4d2](https://github.com/GetStream/stream-chat-java/commit/f6dc4d25407236515e7113f75d4a8d8a3a0d7890))


### Bug Fixes

* fix message count test after ff removal ([#212](https://github.com/GetStream/stream-chat-java/issues/212)) ([d99c580](https://github.com/GetStream/stream-chat-java/commit/d99c5804f65905313c3e4e957f00a4ecc69e5f96))

## [1.36.0](https://github.com/GetStream/stream-chat-java/compare/1.35.0...1.36.0) (2025-10-22)


### Bug Fixes

* restore @JsonAnyGetter functionality for additionalFields serialization ([#209](https://github.com/GetStream/stream-chat-java/issues/209)) ([cfbc54f](https://github.com/GetStream/stream-chat-java/commit/cfbc54f30e31505dc36983ddf6150e1cc003ccc4)), closes [#205](https://github.com/GetStream/stream-chat-java/issues/205) [#205](https://github.com/GetStream/stream-chat-java/issues/205) [#205](https://github.com/GetStream/stream-chat-java/issues/205)

## [1.35.0](https://github.com/GetStream/stream-chat-java/compare/1.34.0...1.35.0) (2025-10-15)


### Features

* [CHA-1300] add count messages to channel type ([#207](https://github.com/GetStream/stream-chat-java/issues/207)) ([4dcd6e0](https://github.com/GetStream/stream-chat-java/commit/4dcd6e0b94946b2164bda79dd3a9cb75d51d2c21))

## [1.34.0](https://github.com/GetStream/stream-chat-java/compare/1.33.2...1.34.0) (2025-10-06)

### [1.33.2](https://github.com/GetStream/stream-chat-java/compare/1.33.1...1.33.2) (2025-09-24)


### Bug Fixes

* Change timeout_ms in event hooks to Number to match the old async moderation config ([#203](https://github.com/GetStream/stream-chat-java/issues/203)) ([b80e6b7](https://github.com/GetStream/stream-chat-java/commit/b80e6b7228cc759c6bbe433726311673a2952eff))

### [1.33.1](https://github.com/GetStream/stream-chat-java/compare/1.33.0...1.33.1) (2025-09-24)


### Bug Fixes

* Expose getEventHooks() ([#201](https://github.com/GetStream/stream-chat-java/issues/201)) ([f5951a9](https://github.com/GetStream/stream-chat-java/commit/f5951a91305220277b013ddf74e21bfbe2e73c2a))

## [1.33.0](https://github.com/GetStream/stream-chat-java/compare/1.32.0...1.33.0) (2025-09-23)


### Features

* add additional fields to channel member request ([#199](https://github.com/GetStream/stream-chat-java/issues/199)) ([3a319f6](https://github.com/GetStream/stream-chat-java/commit/3a319f680ea09f6e5b0d590231b989d43a4e9d62))


### Bug Fixes

* Failing webhook tests ([#198](https://github.com/GetStream/stream-chat-java/issues/198)) ([1592e3f](https://github.com/GetStream/stream-chat-java/commit/1592e3f0aed5492c9ed8f0ed29664b81452539d7))

## [1.32.0](https://github.com/GetStream/stream-chat-java/compare/1.31.1...1.32.0) (2025-09-01)

### [1.31.1](https://github.com/GetStream/stream-chat-java/compare/1.31.0...1.31.1) (2025-09-01)


### Features

* Added `Delete for me` support on behalf of a user ([#193](https://github.com/GetStream/stream-chat-java/issues/193)) ([0f81c7e](https://github.com/GetStream/stream-chat-java/commit/0f81c7e272e8fa5867e1ddee8ffe2073d06ed322))

## [1.31.0](https://github.com/GetStream/stream-chat-java/compare/1.30.0...1.31.0) (2025-08-14)


### Features

* Add avg response time to app setting and user response ([#186](https://github.com/GetStream/stream-chat-java/issues/186)) ([2cfe3f6](https://github.com/GetStream/stream-chat-java/commit/2cfe3f63c2844837fd870d21908af1e105ff7c52))


### Bug Fixes

* don't reset webhook events when updating other fields ([#188](https://github.com/GetStream/stream-chat-java/issues/188)) ([4814163](https://github.com/GetStream/stream-chat-java/commit/4814163c2c4184360b10d9477cbbb49b52220d04))

## [1.31.0](https://github.com/GetStream/stream-chat-java/compare/1.30.0...1.31.0) (2025-07-04)

## [1.30.0](https://github.com/GetStream/stream-chat-java/compare/1.29.0...1.30.0) (2025-06-18)


### Features

* add draft endpoints ([#177](https://github.com/GetStream/stream-chat-java/issues/177)) ([866d832](https://github.com/GetStream/stream-chat-java/commit/866d8321fe19ade2a145fa98f1e2675bf6a0ba05))
* implemented the message reminders feature ([#175](https://github.com/GetStream/stream-chat-java/issues/175)) ([4f107dd](https://github.com/GetStream/stream-chat-java/commit/4f107dd25551be9ec6b4be698a495ead88b3501f))

## [1.29.0](https://github.com/GetStream/stream-chat-java/compare/1.28.0...1.29.0) (2025-03-11)


### Features

* **server:** added deleted by filed ([#173](https://github.com/GetStream/stream-chat-java/issues/173)) ([d046dc1](https://github.com/GetStream/stream-chat-java/commit/d046dc1d042dbb966b0c3912037e2c044e156af8))

## [1.28.0](https://github.com/GetStream/stream-chat-java/compare/1.27.2...1.28.0) (2025-02-11)


### Features

* added restricted visibility ([#170](https://github.com/GetStream/stream-chat-java/issues/170)) ([16d5354](https://github.com/GetStream/stream-chat-java/commit/16d535489da7695f0cc6a81ff64e5bbc476fb491))

### [1.27.2](https://github.com/GetStream/stream-chat-java/compare/1.27.1...1.27.2) (2025-01-15)


### Bug Fixes

* add missing field ([#168](https://github.com/GetStream/stream-chat-java/issues/168)) ([659c044](https://github.com/GetStream/stream-chat-java/commit/659c04432891b8475419643784e831482dd7a193))

### [1.27.1](https://github.com/GetStream/stream-chat-java/compare/1.27.0...1.27.1) (2024-12-30)


### Bug Fixes

* add missing enum value ([#166](https://github.com/GetStream/stream-chat-java/issues/166)) ([b5a1843](https://github.com/GetStream/stream-chat-java/commit/b5a1843b47f124b15f341bde1e40399c6f3fc08f))

## [1.27.0](https://github.com/GetStream/stream-chat-java/compare/1.26.2...1.27.0) (2024-12-13)


### Features

* member custom data, pinning and archiving ([#162](https://github.com/GetStream/stream-chat-java/issues/162)) ([38c7348](https://github.com/GetStream/stream-chat-java/commit/38c73481c5df5e513ba54c73f5852f03db0f0bb3))


### Bug Fixes

* javadoc ([#164](https://github.com/GetStream/stream-chat-java/issues/164)) ([69f4266](https://github.com/GetStream/stream-chat-java/commit/69f426684cce5ed13cd4a391344b16b1c03a1333))
* update the readme to include the shadowed version ([#159](https://github.com/GetStream/stream-chat-java/issues/159)) ([97a7119](https://github.com/GetStream/stream-chat-java/commit/97a7119d82c063478f9b6229cc6cc994b8e8f48a))
* version file read happens only once ([#160](https://github.com/GetStream/stream-chat-java/issues/160)) ([570548b](https://github.com/GetStream/stream-chat-java/commit/570548bbba73ad49d2a94e81a7b1052b86cce33a))

### [1.26.2](https://github.com/GetStream/stream-chat-java/compare/1.25.0...1.26.2) (2024-08-16)


### Features

* publish the shadow publication as well ([#153](https://github.com/GetStream/stream-chat-java/issues/153)) ([117e3d7](https://github.com/GetStream/stream-chat-java/commit/117e3d7d9385a70c7ce6b1b2c3c9ef01e644ce8b))


### Bug Fixes

* ensure publish tasks run after sign tasks ([#157](https://github.com/GetStream/stream-chat-java/issues/157)) ([8929766](https://github.com/GetStream/stream-chat-java/commit/8929766a79189cc09255ab264e47aa6ca92ec22e))

### [1.26.1](https://github.com/GetStream/stream-chat-java/compare/1.25.0...1.26.1) (2024-08-16)


### Features

* publish the shadow publication as well ([#153](https://github.com/GetStream/stream-chat-java/issues/153)) ([117e3d7](https://github.com/GetStream/stream-chat-java/commit/117e3d7d9385a70c7ce6b1b2c3c9ef01e644ce8b))

## [1.26.0](https://github.com/GetStream/stream-chat-java/compare/1.25.0...1.26.0) (2024-08-15)


### Features

* Publish the shadow publication (`stream-chat-java-all`) ([#153](https://github.com/GetStream/stream-chat-java/issues/153)) ([117e3d7](https://github.com/GetStream/stream-chat-java/commit/117e3d7d9385a70c7ce6b1b2c3c9ef01e644ce8b))
* Block user ([#147](https://github.com/GetStream/stream-chat-java/pull/147)) ([76bfad2](https://github.com/GetStream/stream-chat-java/commit/76bfad254b9116f919b50776e359b5a3bd76b675))

## [1.25.0](https://github.com/GetStream/stream-chat-java/compare/1.24.0...1.25.0) (2024-08-08)


### Bug Fixes

* request param name file_name -> filename ([#150](https://github.com/GetStream/stream-chat-java/issues/150)) ([28eaac0](https://github.com/GetStream/stream-chat-java/commit/28eaac0a2428a4ebc522a6a6c35672a150047186))

## [1.24.0](https://github.com/GetStream/stream-chat-java/compare/v1.23.0...v1.24.0) (2024-05-24)


### Features

* add message history api ([#145](https://github.com/GetStream/stream-chat-java/issues/145)) ([dce8529](https://github.com/GetStream/stream-chat-java/commit/dce8529c718a9be5248030c23e2736ee9a028b2d))

## [1.23.0](https://github.com/GetStream/stream-chat-java/compare/1.22.2...1.23.0) (2024-05-22)


### Features

* add test for `notifications_muted` query members ([#143](https://github.com/GetStream/stream-chat-java/issues/143)) ([c25d107](https://github.com/GetStream/stream-chat-java/commit/c25d1077b435b1749e04baef4c5a73c368c9900e))

### [1.22.2](https://github.com/GetStream/stream-chat-java/compare/1.22.1...1.22.2) (2024-04-18)


### Bug Fixes

* update dependency for security. fixes [#138](https://github.com/GetStream/stream-chat-java/issues/138) ([#141](https://github.com/GetStream/stream-chat-java/issues/141)) ([839b525](https://github.com/GetStream/stream-chat-java/commit/839b5258700e10dd6ef24c1a1c90016a7420a5ae))

### [1.22.1](https://github.com/GetStream/stream-chat-java/compare/1.22.0...1.22.1) (2024-04-03)


### Features

* add the new property ([#139](https://github.com/GetStream/stream-chat-java/issues/139)) ([8aa012d](https://github.com/GetStream/stream-chat-java/commit/8aa012d760d372fd7a037c837c447f30b961cd85))

## [1.22.0](https://github.com/GetStream/stream-chat-java/compare/1.21.0...1.22.0) (2024-03-27)

## [1.21.0](https://github.com/GetStream/stream-chat-java/compare/1.20.1...1.21.0) (2024-03-18)


### Features

* query deactivated users ([#134](https://github.com/GetStream/stream-chat-java/issues/134)) ([374d0e9](https://github.com/GetStream/stream-chat-java/commit/374d0e9b650abbce0062ee966a4a89415d233dd9))
* unread counts ([#133](https://github.com/GetStream/stream-chat-java/issues/133)) ([7a075ae](https://github.com/GetStream/stream-chat-java/commit/7a075ae9b0d605dc8c166c2d16c84ae9af61c730))

### [1.20.1](https://github.com/GetStream/stream-chat-java/compare/1.20.0...1.20.1) (2024-02-14)


### Bug Fixes

* remove unused dependency ([#130](https://github.com/GetStream/stream-chat-java/issues/130)) ([ad4e624](https://github.com/GetStream/stream-chat-java/commit/ad4e624bde94f9ceeaf2adb1d3bf25d2282eb81f))

## [1.20.0](https://github.com/GetStream/stream-chat-java/compare/1.19.0...1.20.0) (2023-12-06)


### Features

* add the ability to set message type when sending a message ([#127](https://github.com/GetStream/stream-chat-java/issues/127)) ([8cb5f47](https://github.com/GetStream/stream-chat-java/commit/8cb5f471f7c6521af08fb9479f99b0412788c2df))

## [1.19.0](https://github.com/GetStream/stream-chat-java/compare/v1.18.0...v1.19.0) (2023-11-02)


### Features

* allow sending invites when creating a channel ([#124](https://github.com/GetStream/stream-chat-java/issues/124)) ([937dc3a](https://github.com/GetStream/stream-chat-java/commit/937dc3a7736ba60ea7a32d0ffb34138ab28e4cf8))

## [1.18.0](https://github.com/GetStream/stream-chat-java/compare/1.17.0...1.18.0) (2023-10-18)


### Features

* sns support ([#122](https://github.com/GetStream/stream-chat-java/issues/122)) ([bfac399](https://github.com/GetStream/stream-chat-java/commit/bfac399eb0cb23c35ee810709e337f1f1305af04))

## [1.17.0](https://github.com/GetStream/stream-chat-java/compare/1.16.2...1.17.0) (2023-10-05)


### Features

* support message unblock endpoint ([#120](https://github.com/GetStream/stream-chat-java/issues/120)) ([763e442](https://github.com/GetStream/stream-chat-java/commit/763e442fe0f2b8473d5e552ecef7f5a0883f83b6))

### [1.16.2](https://github.com/GetStream/stream-chat-java/compare/1.16.1...1.16.2) (2023-09-20)


### Features

* allow search with offset and sort ([#118](https://github.com/GetStream/stream-chat-java/issues/118)) ([6d8a6ca](https://github.com/GetStream/stream-chat-java/commit/6d8a6ca46d651655b08d85c3df2f91bdd637fdbe))

### [1.16.1](https://github.com/GetStream/stream-chat-java/compare/1.16.0...1.16.1) (2023-09-19)

## [1.16.0](https://github.com/GetStream/stream-chat-java/compare/1.15.2...1.16.0) (2023-09-18)


### Bug Fixes

* added quotes ([#114](https://github.com/GetStream/stream-chat-java/issues/114)) ([064931e](https://github.com/GetStream/stream-chat-java/commit/064931e7c29effe73b2d248ce4b858946248d2f0))

### [1.15.2](https://github.com/GetStream/stream-chat-java/compare/1.15.1...1.15.2) (2023-09-11)


### Features

* add new field to Message ([#112](https://github.com/GetStream/stream-chat-java/issues/112)) ([33c0803](https://github.com/GetStream/stream-chat-java/commit/33c0803c0ad1221f3b481c9816e8a960a56d6537))

### [1.15.1](https://github.com/GetStream/stream-chat-java/compare/1.15.0...1.15.1) (2023-08-02)


### Bug Fixes

* make created_by_id nullable in UserDeactivateRequest ([#110](https://github.com/GetStream/stream-chat-java/issues/110)) ([200c2f0](https://github.com/GetStream/stream-chat-java/commit/200c2f0b23e3dd43c40701d6d9f12c20e5caad34))

## [1.15.0](https://github.com/GetStream/stream-chat-java/compare/1.14.0...1.15.0) (2023-07-26)


### Features

* added support for force_moderation on message ([#108](https://github.com/GetStream/stream-chat-java/issues/108)) ([d71ccba](https://github.com/GetStream/stream-chat-java/commit/d71ccba984111604dcb96c68671c6394c525a4db))

## [1.14.0](https://github.com/GetStream/stream-chat-java/compare/1.13.1...1.14.0) (2023-07-18)


### Features

* added `pending` property to message ([#104](https://github.com/GetStream/stream-chat-java/issues/104)) ([46eef4f](https://github.com/GetStream/stream-chat-java/commit/46eef4f3debfe7807bb7fd1effa2f5c1c968f8f5))
* added mark_messages_pending property to channel config ([#105](https://github.com/GetStream/stream-chat-java/issues/105)) ([182835e](https://github.com/GetStream/stream-chat-java/commit/182835ed1a852135755f1bbc8764aa2c1ed3b581))
* added support for show_deleted_messages in getMessage endpoint ([#106](https://github.com/GetStream/stream-chat-java/issues/106)) ([40aa482](https://github.com/GetStream/stream-chat-java/commit/40aa482adde4e61f56855328b69f9e6cb5e20bcc))

### [1.13.1](https://github.com/GetStream/stream-chat-java/compare/1.13.0...1.13.1) (2023-06-09)

## [1.13.0](https://github.com/GetStream/stream-chat-java/compare/1.12.6...1.13.0) (2023-05-25)


### Features

* added support for pending messages ([#100](https://github.com/GetStream/stream-chat-java/issues/100)) ([7105068](https://github.com/GetStream/stream-chat-java/commit/7105068d7060fb9a32b87ee184e5faca1c4b895e))

### [1.12.6](https://github.com/GetStream/stream-chat-java/compare/1.12.5...1.12.6) (2022-11-04)


### Bug Fixes

* when owner, nullable is fine ([#96](https://github.com/GetStream/stream-chat-java/issues/96)) ([8fba4a1](https://github.com/GetStream/stream-chat-java/commit/8fba4a14a74970eb37bb971a66fab31ecf046144))

### [1.12.5](https://github.com/GetStream/stream-chat-java/compare/1.12.4...1.12.5) (2022-11-02)


### Bug Fixes

* handle missing languages gracefully ([#92](https://github.com/GetStream/stream-chat-java/issues/92)) ([1d02a90](https://github.com/GetStream/stream-chat-java/commit/1d02a90ed8c72c9f56654f8e43243436b5342f84))

### [1.12.4](https://github.com/GetStream/stream-chat-java/compare/1.12.3...1.12.4) (2022-10-24)


### Bug Fixes

* null annotation of channel model ([#90](https://github.com/GetStream/stream-chat-java/issues/90)) ([1c790c4](https://github.com/GetStream/stream-chat-java/commit/1c790c4faa05f7e28a379cb7a9d3eb73eea2f4c4))

### [1.12.3](https://github.com/GetStream/stream-chat-java/compare/1.12.2...1.12.3) (2022-10-14)


### Bug Fixes

* lt filter ([#87](https://github.com/GetStream/stream-chat-java/issues/87)) ([6187cc5](https://github.com/GetStream/stream-chat-java/commit/6187cc5c8d212bd5976b46bdea3896039fa06483))

### [1.12.2](https://github.com/GetStream/stream-chat-java/compare/1.12.1...1.12.2) (2022-10-07)


### Bug Fixes

* add "lt" to language model ([#84](https://github.com/GetStream/stream-chat-java/issues/84)) ([f76b649](https://github.com/GetStream/stream-chat-java/commit/f76b6496c9f7f0a370682e338c30362417bae839))

### [1.12.1](https://github.com/GetStream/stream-chat-java/compare/1.12.0...1.12.1) (2022-09-07)


### Bug Fixes

* user language parsing ([#79](https://github.com/GetStream/stream-chat-java/issues/79)) ([ce08976](https://github.com/GetStream/stream-chat-java/commit/ce08976343961fb8c8b163ef0811f891c4ef63e3))

## [1.12.0](https://github.com/GetStream/stream-chat-java/compare/v1.11.0...v1.12.0) (2022-05-30)


### Features

* **import:** add import urls ([#74](https://github.com/GetStream/stream-chat-java/issues/74)) ([152627a](https://github.com/GetStream/stream-chat-java/commit/152627a6f3beda6ea57d0347942eb701144bc725))
* **timeout:** add overload for setting timeout ([#75](https://github.com/GetStream/stream-chat-java/issues/75)) ([a82766c](https://github.com/GetStream/stream-chat-java/commit/a82766c0de574d1b0d84ef2fd2535f2e07090df7))

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
