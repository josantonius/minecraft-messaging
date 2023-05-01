package dev.josantonius.minecraft.messaging

import java.io.File
import java.io.FileReader
import java.io.IOException
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * Represents a message with optional custom hover messages for link and command components.
 *
 * @property file The file from which the message is read.
 * @property plugin The JavaPlugin object representing your plugin instance.
 *
 * @throws IllegalArgumentException if there is an error loading the file.
 */
class Message(private val file: File, private val plugin: JavaPlugin) {
    private val miniMessage = MiniMessage.miniMessage()
    private val messages: FileConfiguration
    private var consolePrefix: String = ""
    private var chatPrefix: String = ""

    /**
     * Loads the message file and stores the messages in the FileConfiguration.
     *
     * @throws IllegalArgumentException if there is an error loading the file.
     */
    init {
        try {
            FileReader(file).use { reader ->
                messages = YamlConfiguration.loadConfiguration(reader)
            }
        } catch (e: IOException) {
            throw IllegalArgumentException("Error loading message file", e)
        }
    }

    /**
     * Sends a message with the given key and optional parameters to a specific player.
     *
     * @param player The Player object to send the message to.
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToPlayer(player: Player, key: String, vararg params: String) {
        var message = chatPrefix + getString(key, *params)
        player.sendMessage(miniMessage.deserialize(message))
    }

    /**
     * Sends a message with the given key and optional parameters to all online players.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToPlayers(key: String, vararg params: String) {
        Bukkit.getServer().getOnlinePlayers().forEach { player ->
            sendToPlayer(player, key, *params)
        }
    }

    /**
     * Sends a system message to the console.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToConsole(key: String, vararg params: String) {
        var message = consolePrefix + getString(key, *params)
        val adventure = BukkitAudiences.create(plugin)
        adventure.console().sendMessage(miniMessage.deserialize(message))
    }

    /**
     * Sends a message with the given key and optional parameters to online players and the console.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun broadcast(key: String, vararg params: String) {
        sendToConsole(key, *params)
        sendToPlayers(key, *params)
    }

    /**
     * Set the prefix for console messages.
     *
     * @param prefix The prefix to use for console messages.
     */
    fun setConsolePrefix(prefix: String) {
        consolePrefix = prefix
    }

    /**
     * Set the prefix for chat messages.
     *
     * @param prefix The prefix to use for chat messages.
     */
    fun setChatPrefix(prefix: String) {
        chatPrefix = prefix
    }

    /**
     * Retrieves associated with the given key, replaces placeholders and returns a string.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun getString(key: String, vararg params: String): String {
        var message = messages.getString(key)
        if (message != null) {
            for (i in params.indices) {
                message = message?.replace("{" + (i + 1) + "}", params[i])
            }
        }
        return message ?: key
    }

    /**
     * Retrieves associated with the given key, replaces placeholders and returns a Component.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun getComponent(key: String, vararg params: String): Component {
        var message = getString(key, *params)
        return miniMessage.deserialize(message)
    }
}
