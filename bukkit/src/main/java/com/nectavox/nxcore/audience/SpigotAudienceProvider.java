package com.nectavox.nxcore.audience;

import com.nectavox.nxcore.interfaces.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotAudienceProvider implements AudienceProvider {
    private final BukkitAudiences audiences;

    public SpigotAudienceProvider(JavaPlugin plugin) {
        this.audiences = BukkitAudiences.create(plugin);
    }

    @Override
    public void sendMessage(Player player, Component component) {
        audiences.player(player).sendMessage(component);
    }

    @Override
    public void sendActionBar(Player player, Component component) {
        audiences.player(player).sendActionBar(component);
    }

    @Override
    public void showTitle(Player player, Title title) {
        audiences.player(player).showTitle(title);
    }
}
