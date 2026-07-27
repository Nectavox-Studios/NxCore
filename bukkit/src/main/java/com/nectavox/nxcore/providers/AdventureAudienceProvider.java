package com.nectavox.nxcore.providers;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AdventureAudienceProvider {
    private final BukkitAudiences audiences;

    public AdventureAudienceProvider(JavaPlugin plugin) {
        this.audiences = BukkitAudiences.create(plugin);
    }

    public void sendMessage(Player player, Component component) {
        audiences.player(player).sendMessage(component);
    }

    public void sendActionBar(Player player, Component component) {
        audiences.player(player).sendActionBar(component);
    }

    public void showTitle(Player player, Title title) {
        audiences.player(player).showTitle(title);
    }
}
