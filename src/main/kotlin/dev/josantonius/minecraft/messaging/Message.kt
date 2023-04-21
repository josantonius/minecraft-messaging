package dev.josantonius.minecraft.messaging

import java.io.File
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
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
class Message(private val file: File, private val plugin: JavaPlugin) : MessageBase(file) {
    /**
     * Sends a message with the given key and optional parameters to all online players.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToAll(key: String, vararg params: String) {
        Bukkit.getServer().sendMessage(getComponent(key, *params))
    }

    /**
     * Sends a message with the given key and optional parameters to a specific player.
     *
     * @param player The Player object to send the message to.
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToPlayer(player: Player, key: String, vararg params: String) {
        player.sendMessage(getComponent(key, *params))
    }

    /**
     * Sends a system message with a level to the console.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    fun sendToConsole(key: String, vararg params: String) {
        val component = getComponent(key, *params)
        val adventure = BukkitAudiences.create(plugin)
        adventure.console().sendMessage(component)
    }
}
