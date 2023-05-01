package dev.josantonius.minecraft.messaging

import java.io.File
import java.io.FileReader
import java.io.IOException
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.title.Title as AdventureTitle
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

/**
 * Represents a title message with optional custom times.
 *
 * @property file The file from which the message is read.
 * @property times The Title.Times object containing fadeIn, stay, and fadeOut durations.
 *
 * @throws IllegalArgumentException if there is an error loading the file.
 */
class Title(private val file: File, private val times: AdventureTitle.Times? = null) {
    private val miniMessage = MiniMessage.miniMessage()
    private val titles: FileConfiguration

    /**
     * Loads the message file and stores the title in the FileConfiguration.
     *
     * @throws IllegalArgumentException if there is an error loading the file.
     */
    init {
        try {
            FileReader(file).use { reader -> titles = YamlConfiguration.loadConfiguration(reader) }
        } catch (e: IOException) {
            throw IllegalArgumentException("Error loading message file", e)
        }
    }

    /**
     * Shows a title and subtitle with the given keys and optional parameters to a specific player.
     *
     * @param player The Player object to show the title to.
     * @param titleKey The key associated with the title message in the message file.
     * @param subtitleKey The key associated with the subtitle message in the message file, or an
     * empty string for no subtitle.
     * @param params Optional parameters to replace placeholders in the messages.
     */
    fun showToPlayer(player: Player, titleKey: String, subtitleKey: String, vararg params: String) {
        val titleComponent = getComponent(titleKey, *params)
        val subtitleComponent =
                if (subtitleKey.isNotEmpty()) getComponent(subtitleKey, *params)
                else Component.empty()
        player.showTitle(AdventureTitle.title(titleComponent, subtitleComponent, times))
    }

    /**
     * Shows a title and subtitle with the given keys and optional parameters to all online players.
     *
     * @param titleKey The key associated with the title message in the message file.
     * @param subtitleKey The key associated with the subtitle message in the message file, or an
     * empty string for no subtitle.
     * @param params Optional parameters to replace placeholders in the messages.
     */
    fun showToPlayers(titleKey: String, subtitleKey: String, vararg params: String) {
        val titleComponent = getComponent(titleKey, *params)
        val subtitleComponent =
                if (subtitleKey.isNotEmpty()) getComponent(subtitleKey, *params)
                else Component.empty()
        for (player in Bukkit.getOnlinePlayers()) {
            player.showTitle(AdventureTitle.title(titleComponent, subtitleComponent, times))
        }
    }

    /**
     * Retrieves associated with the given key, replaces placeholders and returns a string.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    private fun getString(key: String, vararg params: String): String {
        var title = titles.getString(key)
        if (title != null) {
            for (i in params.indices) {
                title = title?.replace("{" + (i + 1) + "}", params[i])
            }
        }
        return title ?: key
    }

    /**
     * Retrieves associated with the given key, replaces placeholders and returns a Component.
     *
     * @param key The key associated with the message in the message file.
     * @param params Optional parameters to replace placeholders in the message.
     */
    private fun getComponent(key: String, vararg params: String): Component {
        var message = getString(key, *params)
        return miniMessage.deserialize(message)
    }
}
