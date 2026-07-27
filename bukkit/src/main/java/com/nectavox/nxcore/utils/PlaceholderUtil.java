package com.nectavox.nxcore.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public final class PlaceholderUtil {
    public static String parse(OfflinePlayer player, String text) {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return text;
        }

        return PlaceholderAPI.setPlaceholders(player, text);
    }
}