package com.nectavox.nxcore.interfaces;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

public interface AudienceProvider {
    void sendMessage(Player player, Component component);

    void sendActionBar(Player player, Component component);

    void showTitle(Player player, Title title);

}