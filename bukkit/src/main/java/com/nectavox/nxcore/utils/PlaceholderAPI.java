package com.nectavox.nxcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public final class PlaceholderAPI {
    public static String parse(OfflinePlayer player, String text) {
        if (player == null) return text;

        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return text;
        }

        return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, text);
    }
}