package dev.josantonius.minecraft.messaging

import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.logging.Level
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

/**
 * Represents a message with optional custom hover messages for link and command components.
 *
 * @property file The file from which the message is read.
 * @property hoverMessages A map containing custom hover messages for link and command components.
 * ```
 *                         The keys should be "link" and "command", and the values should be the
 *                         corresponding custom hover messages. If not provided or a specific key
 *                         is not found, default hover messages will be used.
 * ```
 *
 * @throws IllegalArgumentException if there is an error loading the file.
 */
class Message(file: File, val hoverMessages: Map<String, String> = mapOf()) {
    private var messages: FileConfiguration = YamlConfiguration()

    /**
     * Creates a new Message object with the given message file.
     *
     * @param file The File object representing the message file.
     *
     * @throws IllegalArgumentException if there is an error loading the file.
     */
    init {
        this.loadMessages(file)
    }

    /**
     * Retrieves the message associated with the given key. If the message is not found, returns the
     * given string.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun retrieve(key: String, vararg params: String): String {
        var message = messages.getString(key)
        if (message == null) {
            Bukkit.getLogger().warning("Message not found for key:$key")
        } else {
            for (i in params.indices) {
                message = message?.replace("{" + (i + 1) + "}", params[i])
            }
        }
        return message ?: key
    }

    /**
     * Sends a message with the given key and optional parameters to all online players.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToAll(key: String, vararg params: String) {
        sendMessageToAll(key, *params)
    }

    /**
     * Sends a message with the given key and optional parameters to a specific player.
     *
     * @param player The Player object to send the message to.
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToPlayer(player: Player, key: String, vararg params: String) {
        sendMessageToPlayer(player, key, *params)
    }

    /**
     * Sends a message with the given key and optional parameters to all online players with the
     * specified permission.
     *
     * @param permission The permission string to filter players by.
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToPlayersWithPermission(permission: String, key: String, vararg params: String) {
        sendMessageToPlayersWithPermission(permission, key, *params)
    }

    /**
     * Sends message to nearby players with given key and optional parameters.
     *
     * @param center The Location object representing the center of the radius.
     * @param radius The radius in which to send the message to players.
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToPlayersWithinRadius(
            center: Location,
            radius: Double,
            key: String,
            vararg params: String
    ) {
        sendMessageToPlayersWithinRadius(center, radius, key, *params)
    }

    /**
     * Send message to players with specific permission within a radius of a center point using a
     * key and optional parameters.
     *
     * @param permission The permission string to filter players by.
     * @param center The Location object representing the center of the radius.
     * @param radius The radius in which to send the message to players.
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToPlayersWithPermissionWithinRadius(
            permission: String,
            center: Location,
            radius: Double,
            key: String,
            vararg params: String
    ) {
        sendMessageToPlayersWithPermissionWithinRadius(permission, center, radius, key, *params)
    }

    /**
     * Sends a system message with a level to the console.
     *
     * @param level The level of the system message for the console.
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToSystem(level: Level, key: String, vararg params: String) {
        Bukkit.getLogger().log(level, retrieve(key, *params))
    }

    /**
     * Loads the message file and stores the messages in the FileConfiguration.
     *
     * @param file The File object representing the message file.
     *
     * @throws IllegalArgumentException if there is an error loading the file.
     */
    private fun loadMessages(file: File) {
        try {
            FileReader(file).use { reader ->
                messages = YamlConfiguration.loadConfiguration(reader)
            }
        } catch (e: IOException) {
            throw IllegalArgumentException("Error loading message file", e)
        }
    }

    /**
     * Creates a Component with the message retrieved from the 'messages' object using the key and
     * optional parameters, then sends it to all online players on the server.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    private fun sendMessageToAll(key: String, vararg params: String) {
        val message = retrieve(key, *params)
        val component = ComponentUtils.parseClickableComponents(message, hoverMessages)
        Bukkit.getServer().sendMessage(component)
    }

    /**
     * Creates a Component with the message retrieved from the 'messages' object using the key and
     * optional parameters, then sends it to the specified player.
     *
     * @param player The Player object to send the message to.
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    private fun sendMessageToPlayer(player: Player, key: String, vararg params: String) {
        val message = retrieve(key, *params)
        val component = ComponentUtils.parseClickableComponents(message, hoverMessages)
        player.sendMessage(component)
    }

    /**
     * Creates a Component with the message retrieved from the 'messages' object using the key and
     * optional parameters, then sends it to all online players with the specified permission.
     *
     * @param permission The permission string to filter players by.
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    private fun sendMessageToPlayersWithPermission(
            permission: String,
            key: String,
            vararg params: String
    ) {
        val message = retrieve(key, *params)
        val component = ComponentUtils.parseClickableComponents(message, hoverMessages)
        val playersWithPermission =
                Bukkit.getServer().onlinePlayers.filter { it.hasPermission(permission) }
        playersWithPermission.forEach { it.sendMessage(component) }
    }

    /**
     * Creates a Component with the message retrieved from the 'messages' object using the key and
     * optional parameters, then sends it to all online players within the specified radius around
     * the center location.
     *
     * @param center The Location object representing the center of the radius.
     * @param radius The radius in which to send the message to players.
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    private fun sendMessageToPlayersWithinRadius(
            center: Location,
            radius: Double,
            key: String,
            vararg params: String
    ) {
        val message = retrieve(key, *params)
        val component = ComponentUtils.parseClickableComponents(message, hoverMessages)
        val playersWithinRadius =
                Bukkit.getServer().onlinePlayers.filter { it.location.distance(center) <= radius }
        playersWithinRadius.forEach { it.sendMessage(component) }
    }

    /**
     * Creates a Component with the message retrieved from the 'messages' object using the key and
     * optional parameters, then sends it to all online players with the specified permission and
     * within the specified radius around the center location.
     *
     * @param permission The permission string to filter players by.
     * @param center The Location object representing the center of the radius.
     * @param radius The radius in which to send the message to players.
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    private fun sendMessageToPlayersWithPermissionWithinRadius(
            permission: String,
            center: Location,
            radius: Double,
            key: String,
            vararg params: String
    ) {
        val message = retrieve(key, *params)
        val component = ComponentUtils.parseClickableComponents(message, hoverMessages)
        val playersWithPermissionAndWithinRadius =
                Bukkit.getServer().onlinePlayers.filter {
                    it.hasPermission(permission) && it.location.distance(center) <= radius
                }
        playersWithPermissionAndWithinRadius.forEach { it.sendMessage(component) }
    }
}
