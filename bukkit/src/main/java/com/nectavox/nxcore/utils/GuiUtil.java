package com.nectavox.nxcore.utils;

import com.nectavox.nxcore.models.gui.GuiData;
import com.nectavox.nxcore.models.gui.GuiItemData;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class GuiUtil {

    public static void setItem(Player viewer, BaseGui gui, GuiData guiData, String key, Consumer<GuiItemData> action, Object... replacements) {
        GuiItemData itemData = guiData.getItem(key);
        if (itemData != null && itemData.getSlot() >= 0) {

            GuiItem guiItem = GuiUtil.createGuiItem(viewer, itemData, key, replacements);
            guiItem.setAction(e -> {
                if (itemData.getSound() != null) {
                    viewer.playSound(viewer, itemData.getSound(), 1f, 1f);
                }
                action.accept(itemData);
            });
            gui.setItem(itemData.getSlot(), guiItem);
        }
    }

    public static void setSkullItem(OfflinePlayer skullOfPlayer, Player viewer, BaseGui gui, GuiData guiData, String key, Consumer<GuiItemData> action, Object... replacements) {
        GuiItemData itemData = guiData.getItem(key);
        if (itemData != null && itemData.getSlot() >= 0) {

            GuiItem guiItem = GuiUtil.createSkullGuiItem(skullOfPlayer, viewer, itemData, key, replacements);
            guiItem.setAction(e -> {
                if (itemData.getSound() != null) {
                    viewer.playSound(viewer, itemData.getSound(), 1f, 1f);
                }
                action.accept(itemData);
            });
            gui.setItem(itemData.getSlot(), guiItem);
        }
    }


    public static void addItem(Player viewer, BaseGui gui, GuiData guiData, String key, Consumer<GuiItemData> action, Object... replacements) {
        GuiItemData itemData = guiData.getItem(key);

        GuiItem guiItem = GuiUtil.createGuiItem(viewer, itemData, key, replacements);
        guiItem.setAction(e -> {
            if (itemData.getSound() != null) {
                viewer.playSound(viewer, itemData.getSound(), 1f, 1f);
            }
            action.accept(itemData);
        });
        gui.addItem(guiItem);
    }

    public static void addSkullItem(OfflinePlayer skullOfPlayer, Player viewer, BaseGui gui, GuiData guiData, String key, Consumer<GuiItemData> action, Object... replacements) {
        GuiItemData itemData = guiData.getItem(key);

        GuiItem guiItem = GuiUtil.createSkullGuiItem(skullOfPlayer, viewer, itemData, key, replacements);
        guiItem.setAction(e -> {
            if (itemData.getSound() != null) {
                viewer.playSound(viewer, itemData.getSound(), 1f, 1f);
            }
            action.accept(itemData);
        });
        gui.addItem(guiItem);
    }

    private static GuiItem createGuiItem(@Nullable Player parsedPlayer, GuiItemData data, String key, Object... replacements) {
        if (data == null) {
            return ItemBuilder.from(Material.BARRIER)
                    .name(Color.colorComponent("&cMissing: " + key))
                    .asGuiItem();
        }

        return ItemBuilder.from(ItemSerializer.build(parsedPlayer, data, replacements)).asGuiItem();
    }

    private static GuiItem createSkullGuiItem(OfflinePlayer player, @Nullable Player parsedPlayer, GuiItemData data, String key, Object... replacements) {
        if (data == null) {
            return ItemBuilder.from(Material.BARRIER)
                    .name(Color.colorComponent("&cMissing: " + key))
                    .asGuiItem();
        }

        data.setMaterial(Material.PLAYER_HEAD);
        ItemStack item = ItemSerializer.build(parsedPlayer, data, replacements);

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(player);
        item.setItemMeta(meta);

        return ItemBuilder.from(item).asGuiItem();
    }
}