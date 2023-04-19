package dev.josantonius.minecraft.messaging

import java.util.regex.Pattern
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent

object ComponentUtils {
    private val linkPattern =
            Pattern.compile("((?:§[0-9a-fA-Fk-oK-OrR])+)<link(?:=(.+?))*>(.+?)</link>")
    private val commandPattern =
            Pattern.compile("((?:§[0-9a-fA-Fk-oK-OrR])+)<command(?:=(.+?))*>(.+?)</command>")

    fun parseClickableComponents(
            input: String,
            hoverMessages: Map<String, String> = mapOf(),
    ): Component {
        if (!input.contains("<link") && !input.contains("<command")) {
            return Component.text(input)
        }

        val components = mutableListOf<Component>()
        var lastIndex = 0

        while (true) {
            val (nextIndex, component) = findNextClickableComponent(input, lastIndex, hoverMessages)
            if (nextIndex == -1) break

            val prefix = input.substring(lastIndex, nextIndex)
            if (prefix.isNotEmpty()) components.add(Component.text(prefix))

            components.add(component)
            lastIndex = nextIndex
        }

        if (lastIndex < input.length) {
            val suffix = input.substring(lastIndex)
            components.add(Component.text(suffix))
        }

        return components.joinToComponent()
    }

    private fun findNextClickableComponent(
            input: String,
            lastIndex: Int,
            hoverMessages: Map<String, String>
    ): Pair<Int, Component> {
        val linkMatcher = linkPattern.matcher(input)
        val commandMatcher = commandPattern.matcher(input)

        val linkFound = linkMatcher.find(lastIndex)
        val commandFound = commandMatcher.find(lastIndex)

        return when {
            linkFound && (!commandFound || linkMatcher.start() < commandMatcher.start()) -> {
                val colorCode = linkMatcher.group(1)
                val url = linkMatcher.group(2) ?: linkMatcher.group(3)
                val text = "$colorCode${linkMatcher.group(3)}"
                val linkHoverText = (hoverMessages["link"] ?: "Click to open") + " $url"
                val linkComponent =
                        Component.text(text)
                                .clickEvent(ClickEvent.openUrl(url))
                                .hoverEvent(HoverEvent.showText(Component.text(linkHoverText)))
                linkMatcher.end() to linkComponent
            }
            commandFound && (!linkFound || commandMatcher.start() < linkMatcher.start()) -> {
                val colorCode = commandMatcher.group(1)
                val command = commandMatcher.group(2) ?: commandMatcher.group(3)
                val text = "$colorCode${commandMatcher.group(3)}"
                val commandHoverText = (hoverMessages["command"] ?: "Click to run") + " $command"
                val commandComponent =
                        Component.text(text)
                                .clickEvent(ClickEvent.runCommand(command))
                                .hoverEvent(HoverEvent.showText(Component.text(commandHoverText)))
                commandMatcher.end() to commandComponent
            }
            else -> -1 to Component.empty()
        }
    }

    private fun List<Component>.joinToComponent(): Component {
        var result = Component.empty()
        for (component in this) {
            result = result.append(component)
        }
        return result
    }
}
