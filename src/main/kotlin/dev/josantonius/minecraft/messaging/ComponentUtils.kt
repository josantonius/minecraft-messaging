package dev.josantonius.minecraft.messaging

import java.util.regex.Pattern
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent

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
     * @param hoverMessages A map with custom hover messages for link and command components.
     *
     * @return A Component that may be clickable, depending on the input string.
     */
    fun parseClickableComponents(
            input: String,
            hoverMessages: Map<String, String> = mapOf(),
    ): Component {
        if (!input.contains("<link") && !input.contains("<command")) {
            return Component.text(input)
        }
        val linkPattern =
                Pattern.compile("((?:§[0-9a-fA-Fk-oK-OrR])+)<link(?:=(.+?))*>(.+?)</link>")
        val commandPattern =
                Pattern.compile("((?:§[0-9a-fA-Fk-oK-OrR])+)<command(?:=(.+?))*>(.+?)</command>")
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

                val colorCode = linkMatcher.group(1)
                val url = linkMatcher.group(2) ?: linkMatcher.group(3)
                val text = "$colorCode${linkMatcher.group(3)}"
                val linkHoverText = (hoverMessages["link"] ?: "Click to open") + " $url"
                val linkComponent = Component.text(text)
                components.add(
                        linkComponent
                                .clickEvent(ClickEvent.openUrl(url))
                                .hoverEvent(HoverEvent.showText(Component.text(linkHoverText)))
                )
                lastIndex = linkMatcher.end()
            } else if (commandFound && (!linkFound || commandMatcher.start() < linkMatcher.start())
            ) {
                nextIndex = commandMatcher.start()

                val prefix = input.substring(lastIndex, nextIndex)
                if (prefix.isNotEmpty()) {
                    components.add(Component.text(prefix))
                }

                val colorCode = commandMatcher.group(1)
                val command = commandMatcher.group(2) ?: commandMatcher.group(3)
                val text = "$colorCode${commandMatcher.group(3)}"
                val commandHoverText = (hoverMessages["command"] ?: "Click to run") + " $command"
                val commandComponent = Component.text(text)
                components.add(
                        commandComponent
                                .clickEvent(ClickEvent.runCommand(command))
                                .hoverEvent(HoverEvent.showText(Component.text(commandHoverText)))
                )
                lastIndex = commandMatcher.end()
            } else {
                break
            }
        }

        if (lastIndex < input.length) {
            val suffix = input.substring(lastIndex)
            components.add(Component.text(suffix))
        }
        val updatedComponents = components.map { it.asComponent() }
        var result = Component.empty()
        for (component in updatedComponents) {
            result = result.append(component)
        }

        return result
    }
}
