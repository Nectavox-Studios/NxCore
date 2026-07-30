package com.nectavox.nxcore.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.nectavox.nxcore.models.GuiItemData;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public final class GuiUtil {

    private GuiUtil() {
    }

    public static GuiItem createGuiItem(@Nullable Player parsedPlayer, GuiItemData data, String key, Object... replacements) {
        if (data == null) {
            return ItemBuilder.from(Material.BARRIER)
                    .name(Color.colorComponent("&cMissing: " + key))
                    .asGuiItem();
        }

        return ItemBuilder.from(ItemSerializer.build(parsedPlayer, data, replacements)).asGuiItem();
    }

    public static GuiItem createSkullGuiItem(OfflinePlayer player, @Nullable Player parsedPlayer, GuiItemData data, String key, Object... replacements) {
        if (data == null) {
            return ItemBuilder.from(Material.BARRIER)
                    .name(Color.colorComponent("&cMissing: " + key))
                    .asGuiItem();
        }

        data.setMaterial(Material.PLAYER_HEAD);
        ItemStack item =ItemSerializer.build(parsedPlayer, data, replacements);

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(player);
        item.setItemMeta(meta);

        return ItemBuilder.from(item).asGuiItem();
    }
}