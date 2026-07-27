package com.nectavox.nxcore.audience;

import com.nectavox.nxcore.interfaces.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperAudienceProvider implements AudienceProvider {

    @Override
    public void sendMessage(CommandSender sender, Component component) {
        sender.sendMessage(component);
    }

    @Override
    public void sendMessage(Player player, Component component) {
        player.sendMessage(component);
    }

    @Override
    public void sendActionBar(Player player, Component component) {
        player.sendActionBar(component);
    }

    @Override
    public void showTitle(Player player, Title title) {
        player.showTitle(title);
    }
}
