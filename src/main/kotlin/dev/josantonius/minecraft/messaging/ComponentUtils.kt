package dev.josantonius.minecraft.messaging

import java.util.regex.Pattern
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor

object ComponentUtils {

    /**
     * Creates a Component based on the input string. Supports the following input formats for
     * creating clickable components:
     * - <link>http://example.com</link>
     * - <link=http://example.com>Visit our website.</link>
     * - <command>/example</command>
     * - <command=/example>Click here</command>
     *
     * For input strings without any of these formats, the function returns a non-clickable text
     * component.
     *
     * @param input The input string containing the link or command.
     * @return A Component that may be clickable, depending on the input string.
     */
    fun parseClickableComponents(
            input: String,
            hoverMessages: Map<String, String> = mapOf(),
    ): Component {
        if (!input.contains("<link") && !input.contains("<command")) {
            return Component.text(input)
        }

        val colorCodePattern = Pattern.compile("§([0-9a-fA-F])")
        var coloredInput = input
        var currentColor: NamedTextColor? = null

        val colorCodeMatcher = colorCodePattern.matcher(coloredInput)
        while (colorCodeMatcher.find()) {
            currentColor = minecraftColorCodeToAdventureColor(colorCodeMatcher.group(1)[0])
            coloredInput = colorCodeMatcher.replaceFirst("")
            colorCodeMatcher.reset(coloredInput)
        }

        val linkPattern = Pattern.compile("<link(?:=(.+?))*>(.+?)</link>")
        val commandPattern = Pattern.compile("<command(?:=(.+?))*>(.+?)</command>")
        val components = mutableListOf<Component>()

        var lastIndex = 0
        while (true) {
            val linkMatcher = linkPattern.matcher(input)
            val commandMatcher = commandPattern.matcher(input)

            val linkFound = linkMatcher.find(lastIndex)
            val commandFound = commandMatcher.find(lastIndex)

            var nextIndex: Int
            if (linkFound && (!commandFound || linkMatcher.start() < commandMatcher.start())) {
                nextIndex = linkMatcher.start()

                val prefix = input.substring(lastIndex, nextIndex)
                if (prefix.isNotEmpty()) {
                    components.add(Component.text(prefix))
                }

                val url = linkMatcher.group(1) ?: linkMatcher.group(2)
                val text = linkMatcher.group(2)
                val linkHoverText = (hoverMessages["link"] ?: "Click to open") + " $url"
                val linkComponent =
                        Component.text(text)
                                .clickEvent(ClickEvent.openUrl(url))
                                .hoverEvent(HoverEvent.showText(Component.text(linkHoverText)))
                components.add(linkComponent)
                lastIndex = linkMatcher.end()
            } else if (commandFound && (!linkFound || commandMatcher.start() < linkMatcher.start())
            ) {
                nextIndex = commandMatcher.start()

                val prefix = input.substring(lastIndex, nextIndex)
                if (prefix.isNotEmpty()) {
                    components.add(Component.text(prefix))
                }

                val command = commandMatcher.group(1) ?: commandMatcher.group(2)
                val text = commandMatcher.group(2)
                val commandHoverText = (hoverMessages["command"] ?: "Click to run") + " $command"
                val commandComponent =
                        Component.text(text)
                                .clickEvent(ClickEvent.runCommand(command))
                                .hoverEvent(HoverEvent.showText(Component.text(commandHoverText)))
                components.add(commandComponent)
                lastIndex = commandMatcher.end()
            } else {
                break
            }
        }

        if (lastIndex < input.length) {
            val suffix = input.substring(lastIndex)
            components.add(Component.text(suffix))
        }
        val updatedComponents =
                components.map {
                    if (currentColor != null) {
                        it.color(currentColor)
                    } else {
                        it
                    }
                }
        var result = Component.empty()
        for (component in updatedComponents) {
            result = result.append(component)
        }

        return result
    }

    private fun minecraftColorCodeToAdventureColor(code: Char): NamedTextColor? {
        return when (code) {
            '0' -> NamedTextColor.BLACK
            '1' -> NamedTextColor.DARK_BLUE
            '2' -> NamedTextColor.DARK_GREEN
            '3' -> NamedTextColor.DARK_AQUA
            '4' -> NamedTextColor.DARK_RED
            '5' -> NamedTextColor.DARK_PURPLE
            '6' -> NamedTextColor.GOLD
            '7' -> NamedTextColor.GRAY
            '8' -> NamedTextColor.DARK_GRAY
            '9' -> NamedTextColor.BLUE
            'a' -> NamedTextColor.GREEN
            'b' -> NamedTextColor.AQUA
            'c' -> NamedTextColor.RED
            'd' -> NamedTextColor.LIGHT_PURPLE
            'e' -> NamedTextColor.YELLOW
            'f' -> NamedTextColor.WHITE
            else -> null
        }
    }
}
