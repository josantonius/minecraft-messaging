package dev.josantonius.minecraft.messaging

import java.io.File
import java.io.FileReader
import java.io.IOException
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration

/**
 * MessageBase is a base class that provides methods for handling messages.
 *
 * @property messages The FileConfiguration object containing the messages.
 */
abstract class MessageBase(file: File) {
    private val miniMessage = MiniMessage.miniMessage()
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
     * Retrieves associated with the given key, replaces placeholders and returns a string.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun getString(key: String, vararg params: String): String {
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
     * Retrieves associated with the given key, replaces placeholders and returns a Component.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun getComponent(key: String, vararg params: String): Component {
        var message = getString(key, *params)
        return miniMessage.deserialize(message)
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
}
