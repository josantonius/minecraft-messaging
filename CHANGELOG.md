# Changelog

## [v1.0.6](https://github.com/josantonius/minecraft-messaging/releases/tag/v1.0.6) (2023-04-21)

### Added

* `net.kyori:adventure-platform-bukkit:4.3.0` dependency.

### Changed

* `dev.josantonius.minecraft.messaging.Message.sendToSystem(level: Level)` to
`dev.josantonius.minecraft.messaging.Message.sendToSystem(plugin: JavaPlugin)` for compatibility
with the new version of the `net.kyori:adventure-platform-bukkit` dependency.

### Removed

* `dev.josantonius.minecraft.messaging.ComponentUtils` class.

## [v1.0.5](https://github.com/josantonius/minecraft-messaging/releases/tag/v1.0.5) (2023-04-20)

### Changed

* `dev.josantonius.minecraft.messaging.Message.retrieve()` to
`dev.josantonius.minecraft.messaging.Message.getString()`.

### Added

* `dev.josantonius.minecraft.messaging.Message.getComponent()` public method.

## [v1.0.4](https://github.com/josantonius/minecraft-messaging/releases/tag/v1.0.4) (2023-04-19)

### Changed

* Changes in documentation.

## [v1.0.3](https://github.com/josantonius/minecraft-messaging/releases/tag/v1.0.3) (2023-04-19)

### Added

* Added option to pass hover messages to the `Message` class instances.
* Fixed a problem with the colors of clickable components.

## [v1.0.2](https://github.com/josantonius/minecraft-messaging/releases/tag/v1.0.2) (2023-04-18)

### Added

* Functionality for clickable message components.

* `dev.josantonius.minecraft.messaging.ComponentUtils` class.
* `dev.josantonius.minecraft.messaging.ComponentUtils.parseClickableComponents()` private method.

## [v1.0.1](https://github.com/josantonius/minecraft-messaging/releases/tag/v1.0.1) (2023-04-18)

### Added

* `dev.josantonius.minecraft.messaging.Message.retrieve()` public method.
* `dev.josantonius.minecraft.messaging.Message.sendToSystem()` public method.

## [v1.0.0](https://github.com/josantonius/minecraft-messaging/releases/tag/v1.0.0) (2023-04-16)

* Initial release.
