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
    implementation 'dev.josantonius:minecraft-messaging:v1.0.6'
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
 * @property file   The file from which the message is read.
 * @property plugin The JavaPlugin object representing your plugin instance.
 *
 * @throws IllegalArgumentException if there is an error loading the file.
 */
class Message(private val file: File, private val plugin: JavaPlugin)
```

Retrieves associated with the given key, replaces placeholders and returns a string:

```kotlin
/**
 * @param key    The key associated with the message in the message file.
 * @param params Optional parameters to replace placeholders in the message.
 */
fun getString(key: String, vararg params: String): String
```

Retrieves associated with the given key, replaces placeholders and returns a Component:

```kotlin
/**
 * @param key    The key associated with the message in the message file.
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

Sends a system message to the console:

```kotlin
/**
 * @param plugin The JavaPlugin object representing your plugin instance.
 * @param key    The key associated with the message in the message file.
 * @param params Optional parameters to replace placeholders in the message.
 */
fun sendToConsole(plugin: JavaPlugin, key: String, vararg params: String)
```

## Using Clickable Tags in Messages

With [MiniMessage](https://docs.advntr.dev/minimessage/format.html#click), you can create interactive messages by
adding clickable tags such as hover and click events. Here are some examples of using clickable tags
in your messages:

**Example usage in YAML files**:

```yml
welcome_message: "<click:open_url:https://example.com>Visit our website</click>"
info_message: "<click:run_command:/rules>Click here to view rules</click>"
warning_message: "<click:suggest_command:/report >Report a user</click>"
```

## Using Color Codes in Messages

This library utilizes [MiniMessage](https://docs.advntr.dev/minimessage/format.html#color) to parse and handle color
codes and text formatting in messages. You can use MiniMessage's syntax in your message strings to
apply colors, formatting, and other features. Here are some examples of MiniMessage syntax:

**Example usage in YAML files**:

```yml
welcome_message: "<green>Welcome to our server!</green>"
info_message: "<blue><bold>Info:</bold></blue> Remember to follow the server rules!"
warning_message: "<red><bold>Warning:</bold></red> Fly is not allowed "
```

## Usage

### Creating new instance of Message with default hover messages

**`Main.kt`**

```kotlin
import java.io.File
import org.bukkit.plugin.java.JavaPlugin
import dev.josantonius.minecraft.messaging.Message

Message(File("path/to/messages.yml"), JavaPlugin())
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

### Sends a system message to the console

**`messages.yml`**

```yml
warnings:
  wrong_input: "Warning: it is not a valid input."
```

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))

message.sendToConsole(
    key = "warnings.wrong_input"
)
```

### Sends a system message to the console with parameters

**`messages.yml`**

```yml
info:
  wrong_input: "Info: {} is not a valid input."
```

**`Main.kt`**

```kotlin
import java.io.File
import dev.josantonius.minecraft.messaging.Message

val message = Message(File("path/to/messages.yml"))


message.sendToConsole(
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
