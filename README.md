# Minecraft Messaging Library

[![Release](https://jitpack.io/v/dev.josantonius/minecraft-messaging.svg)](https://jitpack.io/#dev.josantonius/minecraft-messaging)
[![License](https://img.shields.io/github/license/josantonius/minecraft-messaging)](LICENSE)

Easily send messages to players on a Minecraft server running PaperMC, Bukkit, or Spigot.

It supports sending messages to individual players, all players, players with specific permissions,
players within a certain radius and players with specific permissions within a certain radius.

The messages are stored in a YAML file and can be easily formatted with placeholders.

---

- [Requirements](#requirements)
- [Installation](#installation)
- [Available Classes](#available-classes)
  - [Message Class](#message-class)
- [Usage](#usage)
- [TODO](#todo)
- [Changelog](#changelog)
- [Contribution](#contribution)
- [Sponsor](#sponsor)
- [License](#license)

---

## Requirements

- Java 17
- PaperMC server 1.19.3 or Bukkit/Spigot server compatible with the PaperMC API version used.

## Installation

First, add the repository containing the minecraft-messaging library to your project's build.gradle file:

```groovy
repositories {
    maven {
        url = uri("https://github.com/josantonius/minecraft-messaging/")
    }
}
```

Then, add the minecraft-messaging library as a dependency to your project's build.gradle file:

```groovy
dependencies {
    compileOnly("dev.josantonius.minecraft.messaging:minecraft-messaging:1.0.0")
}
```

## Available Classes

### Message Class

`dev.josantonius.minecraft.messaging.Message`

Creates a new Message object with the given message file:

```kotlin
/**
 * @param file The File object representing the message file.
 * 
 * @throws IllegalArgumentException if there is an error loading the file.
 */
class Message(private val file: File)
```

Sends a message with the given key and optional parameters to all online players on the server:

```kotlin
/**
 * @param key    The key associated with the message in the message file.
 * @param params Optional parameters to replace placeholders in the message.
 */
fun sendToAll(key: String, vararg params: String)
```

Sends a message with the given key and optional parameters to a specific player:

```kotlin
/**
 * @param player The Player object to send the message to.
 * @param key    The key associated with the message in the message file.
 * @param params Optional parameters to replace placeholders in the message.
 */
fun sendToPlayer(player: Player, key: String, vararg params: String)
```

Sends a message with the given key and optional parameters
to all online players with the specified permission:

```kotlin
/**
 * @param permission The permission string to filter players by.
 * @param key        The key associated with the message in the message file.
 * @param params     Optional parameters to replace placeholders in the message.
 */
fun sendToPlayersWithPermission(permission: String, key: String, vararg params: String)
```

Sends message to nearby players with given key and optional parameters:

```kotlin
/**
 * @param center The Location object representing the center of the radius.
 * @param radius The radius in which to send the message to players.
 * @param key    The key associated with the message in the message file.
 * @param params Optional parameters to replace placeholders in the message.
 */
fun sendToPlayersWithinRadius(
  center: Location,
  radius: Double,
  key: String,
  vararg params: String
)
```

Sends message to players with specific permission within a radius
of a center point using a key and optional parameters:

```kotlin
/**
 * @param permission The permission string to filter players by.
 * @param center     The Location object representing the center of the radius.
 * @param radius     The radius in which to send the message to players.
 * @param key        The key associated with the message in the message file.
 * @param params     Optional parameters to replace placeholders in the message.
 */


```

## Usage

### Send message to all online players on the server

**`messages.yml`**

```yml
welcome:
  message: "Welcome to this server!"
```

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))

message.sendToAll("welcome.message")
```

### Send message to all online players on the server with parameters

**`messages.yml`**

```yml
game:
  start: "Game '{1}' started. Good luck, {2}!"
```

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))

message.sendToAll(
    key = "game.start",
    params = arrayOf("CaptureTheFlag", "everyone")
)
```

### Send message to a specific player

**`messages.yml`**

```yml
player:
  teleport: "You have been teleported to The End."
```

**`Main.kt`**

```kotlin
import java.io.File
import org.bukkit.entity.Player
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))
val targetPlayer: Player = ...

message.sendToPlayer(
    player = targetPlayer, 
    key = "player.teleport"
)
```

### Send message to a specific player with parameters

**`messages.yml`**

```yml
player:
  points: "You have earned {1} points, {2}!"
```

**`Main.kt`**

```kotlin
import java.io.File
import org.bukkit.entity.Player
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))
val targetPlayer: Player = ...

message.sendToPlayer(
    player = targetPlayer,
    key = "player.points",
    params = arrayOf("100", targetPlayer.name)
)
```

### Send message to all online players with a specific permission

**`messages.yml`**

```yml
admin:
  broadcast: "Attention: Server maintenance will start in 10 minutes."
```

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))

message.sendToPlayersWithPermission(
    permission = "admin.notify",
    key = "admin.broadcast"
)
```

### Send message to all online players with a specific permission and parameters

**`messages.yml`**

```yml
vip:
  notify: "Hello {1}, {2} joined the VIP club!"
```

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))

message.sendToPlayersWithPermission(
    permission = "admin.notify",
    key = "admin.broadcast",
    params = arrayOf("PlayerName", "NewVIP")
)
```

### Send message to all online players within a specific radius

**`messages.yml`**

```yml
local:
  announcement: "Event starting nearby in {1} minutes."
```

**`Main.kt`**

```kotlin
import java.io.File
import org.bukkit.Location
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))
val center: Location = ...

message.sendToPlayersWithinRadius(
    center = center,
    radius = 100.0,
    key = "local.announcement",
    params = arrayOf("5")
)
```

### Send message to all online players within a specific radius and parameters

**`messages.yml`**

```yml
treasure:
  hint: "A treasure chest has been discovered at {1}x, {2}y, {3}z!"
```

**`Main.kt`**

```kotlin
import java.io.File
import org.bukkit.Location
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))
val center: Location = ...

message.sendToPlayersWithinRadius(
    center = center,
    radius = 50.0,
    key = "treasure.hint",
    params = arrayOf("100", "200", "300")
)
```

### Send message to all online players with permission within a specific radius

**`messages.yml`**

```yml
local:
  announcement: "Event starting nearby in 4 minutes."
```

**`Main.kt`**

```kotlin
import java.io.File
import org.bukkit.Location
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))
val center: Location = ...

message.sendToPlayersWithPermissionWithinRadius(
  permission = "announcement.use",
  center = center,
  radius = 100.0,
  key = "local.announcement"
)
```

### Send message to all players with permission within a specific radius and params

**`messages.yml`**

```yml
treasure:
  hint: "A treasure chest has been discovered at {1}x, {2}y, {3}z!"
```

**`Main.kt`**

```kotlin
import java.io.File
import org.bukkit.Location
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))
val center: Location = ...

message.sendToPlayersWithPermissionWithinRadius(
    permission = "treasure.use",
    center = center,
    radius = 50.0,
    key = "treasure.hint",
    params = arrayOf("100", "200", "300")
)
```

## TODO

- [ ] Add new feature
- [ ] Add tests
- [ ] Improve documentation

## Changelog

Detailed changes for each release are documented in the
[release notes](https://github.com/josantonius/minecraft-messaging/releases).

## Contribution

Please make sure to read the [Contributing Guide](.github/CONTRIBUTING.md), before making a pull
request, start a discussion or report a issue.

Thanks to all [contributors](https://github.com/josantonius/minecraft-messaging/graphs/contributors)! :heart:

## Sponsor

If this project helps you to reduce your development time,
[you can sponsor me](https://github.com/josantonius#sponsor) to support my open source work :blush:

## License

This repository is licensed under the [MIT License](LICENSE).

Copyright © 2023-present, [Josantonius](https://github.com/josantonius#contact)
