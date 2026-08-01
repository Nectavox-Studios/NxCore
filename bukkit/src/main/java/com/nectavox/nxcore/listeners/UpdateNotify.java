package com.nectavox.nxcore.listeners;

import com.nectavox.nxcore.NxPlugin;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class UpdateNotify implements Listener {
    private final NxPlugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String pluginName = plugin.getDescription().getName();
        if (!plugin.isLastVersion && player.hasPermission(pluginName.toLowerCase() + ".update")) {
            sendUpdateMessage(player, pluginName, plugin.getDescription().getVersion(), plugin.getNewVersion());
        }
    }

    private void sendUpdateMessage(Player player, String pluginName, String currentVersion, String newVersion) {

        Component message = (Component) Component.text()
                .append(Component.newline())
                .append(
                        MiniMessage.miniMessage().deserialize(
                                "<gradient:#a120f7:#cc20f7>" + pluginName + "</gradient> <gray>» A new update is available!"
                        )
                )
                .append(Component.newline())
                .append(Component.text("Current Version: ").color(NamedTextColor.GRAY))
                .append(Component.text(currentVersion).color(NamedTextColor.WHITE))
                .append(Component.newline())
                .append(Component.text("New Version: ").color(NamedTextColor.GRAY))
                .append(Component.text(newVersion + " ↑").color(NamedTextColor.GREEN))
                .append(Component.newline())
                .append(Component.newline())
                .append(
                        Component.text("[DOWNLOAD]")
                                .color(NamedTextColor.LIGHT_PURPLE)
                                .clickEvent(ClickEvent.openUrl("https://nectavox.com/resources/" + pluginName.toLowerCase()))
                                .hoverEvent(
                                        HoverEvent.showText(
                                                Component.text("Click to download the latest version")
                                                        .color(NamedTextColor.GREEN)
                                        )
                                )
                )
                .append(Component.newline());

        plugin.getAudience().sendMessage(player, message);
    }

}
