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
- [Using Clickable Tags in Messages](#using-clickable-tags-in-messages)
- [Using Color Codes in Messages](#using-color-codes-in-messages)
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
    maven { url 'https://jitpack.io' }
}
```

Then, add the minecraft-messaging library as a dependency to your project's build.gradle file:

```groovy
dependencies {
    implementation 'dev.josantonius:minecraft-messaging:v1.0.0'
}
```

## Available Classes

### Message Class

`dev.josantonius.minecraft.messaging.Message`

Creates a new Message object with the given message file:

```kotlin
/**
 * Represents a message with optional custom hover messages for link and command components.
 *
 * @property file The file from which the message is read.
 * @property hoverMessages A map with custom hover messages for link and command components.
 *                         The keys should be "link" and "command", and the values should be
 *                         the corresponding custom hover messages. If not provided or a
 *                         specific key is not found, default hover messages will be used.
 *
 * @throws IllegalArgumentException if there is an error loading the file.
 */
class Message(file: File, val hoverMessages: Map<String, String> = mapOf())
```

Retrieves associated with the given key, replaces placeholders and returns a string:

```kotlin
/**
 * @param key The key associated with the message in the message file.
 * @param params Optional parameters to replace placeholders in the message.
 */
fun getString(key: String, vararg params: String): String
```

Retrieves associated with the given key, replaces placeholders and returns a Component:

```kotlin
/**
 * @param key The key associated with the message in the message file.
 * @param params Optional parameters to replace placeholders in the message.
 */
fun getComponent(key: String, vararg params: String): Component
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
of a center point using a key and optional params:

```kotlin
/**
 * @param permission The permission string to filter players by.
 * @param center     The Location object representing the center of the radius.
 * @param radius     The radius in which to send the message to players.
 * @param key        The key associated with the message in the message file.
 * @param params     Optional parameters to replace placeholders in the message.
 */
fun sendToPlayersWithPermissionWithinRadius(
  permission: String,
  center: Location,
  radius: Double,
  key: String,
  vararg params: String
)
```

Sends a system message with a level to the console:

```kotlin
/**
 * @param level The level of the system message for the console.
 * @param key The key associated with the message in the message file.
 * @param params Optional parameters to replace placeholders in the message.
 */
fun sendToSystem(level: Level, key: String, vararg params: String)
```

## Using Clickable Tags in Messages

You can use the following tags within any message in your YAML files to create clickable links and
commands:

- Displays a clickable link that opens the specified URL when clicked.:

  - `<link>URL</link>`

- Displays a clickable link with custom text that opens the specified URL when clicked:

  - `<link=URL>Custom Text</link>`

- Displays a clickable text that runs the specified command when clicked.:

  - `<command>Command</command>`

- Displays a clickable text with custom text that runs the specified command when clicked:

  - `<command=Command>Custom Text</command>`

**Example usage in YAML files**:

```yml
cancel_challenge: "Run <command>/cancel</command> to cancel the challenge."
challenge_player: "Challenge a player by clicking <command=/challenge>here</command>."
visit_site: "Visit our <link=https://mc.com>rules page</link> before accepting challenges."
see_rules: "See our <link>https://mc.com</link> before accepting challenges."
```

The default hover messages used for clickable tags are:

- `link` - Click to open
- `command` - Click to run

You can override these default messages by passing a map with custom messages to the
[`Message` class constructor](#creating-new-instance-of-message-with-custom-hover-messages).

## Using Color Codes in Messages

You can use color codes within any message in your YAML files to change the color of the text.
To apply a color, use the & symbol followed by a color code. Here are the available color codes:

- `§0` - Black
- `§1` - Dark Blue
- `§2` - Dark Green
- `§3` - Dark Aqua
- `§4` - Dark Red
- `§5` - Dark Purple
- `§6` - Gold
- `§7` - Gray
- `§8` - Dark Gray
- `§9` - Blue
- `§a` - Green
- `§b` - Aqua
- `§c` - Red
- `§d` - Light Purple
- `§e` - Yellow
- `§f` - White

**Example usage in YAML files**:

```yml
welcome_message: "&eWelcome to our server! &aHave fun and enjoy your stay."
info_message: "&bFor more information, visit our &9<link={1}>website</link>&b."
warning_message: "&cBe careful! &6Make sure to follow the rules."
```

## Usage

### Creating new instance of Message with default hover messages

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))
```

### Creating new instance of Message with custom hover messages

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(
    File("path/to/messages.yml"),
    mapOf(
        "link" to "Haz clic para abrir",
        "command" to "Haz clic para ejecutar"
    )
)
```

### Retrieves the message associated with the given key

**`messages.yml`**

```yml
player:
  win: "Congratulations, you won the game!"
```

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))

message.getString(
    key = "player.win"
)
```

### Retrieves the message associated with the given key with parameters

**`messages.yml`**

```yml
command:
  cancel: "Run <command>/cancel</command> to cancel the challenge."
```

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))

message.getComponent(
    key = "command.cancel",
    params = arrayOf("first")
)
```

### Retrieves the component associated with the given key

**`messages.yml`**

```yml
see:
  rules: "See our <link>https://mc.com</link> before accepting challenges."
```

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))

message.getComponent(
    key = "see.rules"
)
```

### Retrieves the component associated with the given key with parameters

**`messages.yml`**

```yml
player:
  win: "Congratulations, you won the {1} game!"
```

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))

message.getString(
    key = "player.win",
    params = arrayOf("first")
)
```

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
val targetPlayer: Player = //...

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
val targetPlayer: Player = //...

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
val center: Location = //...

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
val center: Location = //...

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
val center: Location = //...

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
val center: Location = //...

message.sendToPlayersWithPermissionWithinRadius(
    permission = "treasure.use",
    center = center,
    radius = 50.0,
    key = "treasure.hint",
    params = arrayOf("100", "200", "300")
)
```

### Sends a system message with a level to the console

**`messages.yml`**

```yml
warnings:
  wrong_input: "Warning: it is not a valid input."
```

**`Main.kt`**

```kotlin
import java.io.File
import java.util.logging.Level
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))

message.sendToSystem(
    level = Level.WARNING, 
    key = "warnings.wrong_input"
)
```

### Sends a system message with a level to the console with parameters

**`messages.yml`**

```yml
info:
  wrong_input: "Info: {} is not a valid input."
```

**`Main.kt`**

```kotlin
import java.io.File
import java.util.logging.Level
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))

message.sendToSystem(
    level = Level.INFO, 
    key = "info.wrong_input",
    params = arrayOf("command")
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
