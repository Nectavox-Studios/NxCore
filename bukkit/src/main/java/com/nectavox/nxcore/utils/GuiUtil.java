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

        return ItemBuilder.from(buildItem(parsedPlayer, data, replacements)).asGuiItem();
    }

    public static GuiItem createSkullGuiItem(OfflinePlayer player, @Nullable Player parsedPlayer, GuiItemData data, String key, Object... replacements) {
        if (data == null) {
            return ItemBuilder.from(Material.BARRIER)
                    .name(Color.colorComponent("&cMissing: " + key))
                    .asGuiItem();
        }

        data.setMaterial(Material.PLAYER_HEAD);
        ItemStack item = buildItem(parsedPlayer, data, replacements);

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(player);
        item.setItemMeta(meta);

        return ItemBuilder.from(item).asGuiItem();
    }

    private static ItemStack buildItem(@Nullable Player parsedPlayer, GuiItemData data, Object... replacements) {
        Object[] wrapped = wrapPlaceholders(replacements);
        String name = Placeholders.apply(data.getName(), wrapped);
        List<String> lore = Placeholders.apply(data.getLore(), wrapped);

        ItemBuilder itemBuilder = ItemBuilder.from(new ItemStack(data.getMaterial(), Math.max(1, data.getAmount())));

        if (data.getName() != null && !data.getName().isBlank()) {
            itemBuilder.name(Color.colorComponent(PlaceholderAPI.parse(parsedPlayer, name)));
        }

        if (!data.getLore().isEmpty()) itemBuilder.lore(lore.stream().map(text -> {
            return Color.colorComponent(PlaceholderAPI.parse(parsedPlayer, text));
        }).toList());

        if (data.getCustomModelData() != null) itemBuilder.model(data.getCustomModelData());

        itemBuilder.glow(data.isGlow());

        ItemStack item = itemBuilder.build();

        if (data.getMaterial() == Material.PLAYER_HEAD && data.getHead() != null) {
            applySkullTexture(item, data.getHead());
        }

        return item;
    }

    private static void applySkullTexture(ItemStack item, String texture) {
        if (!(item.getItemMeta() instanceof SkullMeta meta)) {
            return;
        }

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", texture));
        meta.setPlayerProfile(profile);
        item.setItemMeta(meta);
    }

    private static Object[] wrapPlaceholders(Object... replacements) {
        Object[] wrapped = new Object[replacements.length];
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            wrapped[i] = "%" + replacements[i] + "%";
            wrapped[i + 1] = replacements[i + 1];
        }
        return wrapped;
    }
}