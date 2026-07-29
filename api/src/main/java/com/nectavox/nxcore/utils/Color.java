package com.nectavox.nxcore.utils;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Color {

    public static boolean colorReplica = true;

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private static final Pattern HEX_PATTERN =
            Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String colorLegacy(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        if (colorReplica) {
            text = replaceLegacyColorsToHex(text);
        } else {
            text = replaceLegacyColorsToMiniMessage(text);
        }

        Component comp = LegacyComponentSerializer.legacyAmpersand().deserialize(text);
        return LegacyComponentSerializer.legacySection().serialize(comp);
    }

    public static Component colorComponent(String text,
                                           TagResolver... resolvers) {

        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        if (colorReplica) {
            text = replaceLegacyColorsToHex(text);
        } else {
            text = replaceLegacyColorsToMiniMessage(text);
        }

        text = replaceHex(text);

        return MINI_MESSAGE.deserialize(text, resolvers)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static Component parseLegacy(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        if (colorReplica) {
            text = replaceLegacyColorsToHex(text);
        } else {
            text = replaceLegacyColorsToMiniMessage(text);
        }

        text = replaceHex(text);

        return Component.text(text);
    }

    public static Component parseMiniMessage(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        return MINI_MESSAGE.deserialize(text)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    private static String replaceHex(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(buffer, "<#" + matcher.group(1) + ">");
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String replaceLegacyColorsToHex(String text) {
        return text
                .replace("&0", "&#000000")
                .replace("&1", "&#1f00dc")
                .replace("&2", "&#018c00")
                .replace("&3", "&#2d7396")
                .replace("&4", "&#a40000")
                .replace("&5", "&#a400d4")
                .replace("&6", "&#fc8500")
                .replace("&7", "&#aaaaaa")
                .replace("&8", "&#6a6a6a")
                .replace("&9", "&#006aff")
                .replace("&a", "&#00ec0b")
                .replace("&b", "&#00feff")
                .replace("&c", "&#ff1919")
                .replace("&d", "&#ff19ee")
                .replace("&e", "&#e3ff00")
                .replace("&f", "<white>")

                .replace("&k", "<obfuscated>")
                .replace("&l", "<bold>")
                .replace("&m", "<strikethrough>")
                .replace("&n", "<underlined>")
                .replace("&o", "<italic>")
                .replace("&r", "<reset>");
    }

    private static String replaceLegacyColorsToMiniMessage(String text) {
        return text
                .replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<gray>")
                .replace("&8", "<dark_gray>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")

                .replace("&k", "<obfuscated>")
                .replace("&l", "<bold>")
                .replace("&m", "<strikethrough>")
                .replace("&n", "<underlined>")
                .replace("&o", "<italic>")
                .replace("&r", "<reset>");
    }
}