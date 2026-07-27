package com.nectavox.nxcore.utils;

import com.nectavox.nxcore.NxPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportUtil {
    public static void teleport(Player player, Location location) {
        if (NxPlugin.isPaper()) {
            player.teleportAsync(location);
        } else {
            player.teleport(location);
        }
    }
}
