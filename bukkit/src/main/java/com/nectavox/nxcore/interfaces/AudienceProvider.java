package com.nectavox.nxcore.interfaces;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface AudienceProvider {

    void sendMessage(CommandSender sender, Component component);

    void sendMessage(Player player, Component component);

    void sendActionBar(Player player, Component component);

    void showTitle(Player player, Title title);

    default boolean isEmpty(Component component) {
        if (component == null) {
            return true;
        }

        String text = PlainTextComponentSerializer.plainText().serialize(component);
        return text.isBlank();
    }

}