package com.nectavox.nxcore.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.nectavox.nxcore.hooks.PlaceholderAPI;
import com.nectavox.nxcore.models.item.EnchantData;
import com.nectavox.nxcore.models.gui.GuiItemData;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class ItemSerializer {

    public static ItemStack build(
            @Nullable Player parsedPlayer,
            GuiItemData data,
            Object... replacements
    ) {

        String name = Placeholders.apply(data.getName(), replacements);

        List<String> lore = Placeholders.apply(data.getLore(), replacements);

        ItemBuilder itemBuilder = ItemBuilder
                .from(data.getMaterial())
                .amount(Math.max(1, data.getAmount()));

        if (name != null && !name.isBlank()) {
            itemBuilder.name(Color.colorComponent(PlaceholderAPI.parse(parsedPlayer, name)));
        }

        if (!lore.isEmpty()) {
            itemBuilder.lore(lore.stream().map(text -> {
                return Color.colorComponent(PlaceholderAPI.parse(parsedPlayer, text));
            }).toList());
        }

        if (data.getCustomModelData() != null) {
            itemBuilder.model(data.getCustomModelData());
        }

        if (data.isGlow()) itemBuilder.glow(true);


        ItemStack item = itemBuilder.build();

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return item;
        }

        VersionSupport.applyItemModel(meta, data.getItemModel());

        VersionSupport.applyCustomModelDataComponent(meta, data.getCustomModelDataComponent());

        meta.setUnbreakable(data.isUnbreakable());

        applyFlags(meta, data.getItemFlags());

        applyEnchantments(meta, data.getEnchants());

        applyColor(meta, data.getColor());

        if (item.getType() == Material.PLAYER_HEAD && data.getHead() != null && !data.getHead().isBlank()) {
            applyHeadTexture(meta, data.getHead());
        }

        item.setItemMeta(meta);

        return item;
    }

    private static void applyFlags(
            ItemMeta meta,
            Set<ItemFlag> flags
    ) {
        for (ItemFlag flag : flags) {
            meta.addItemFlags(flag);
        }
    }

    private static void applyEnchantments(
            ItemMeta meta,
            List<EnchantData> enchants
    ) {

        for (EnchantData enchant : enchants) {
            meta.addEnchant(enchant.getEnchantment(), enchant.getLevel(), true);
        }
    }

    private static void applyColor(
            ItemMeta meta,
            org.bukkit.Color color
    ) {

        if (color == null) {
            return;
        }

        if (meta instanceof LeatherArmorMeta leatherMeta) {
            leatherMeta.setColor(color);
            return;
        }

        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.setColor(color);
        }
    }

    private static void applyHeadTexture(
            ItemMeta meta,
            String texture
    ) {

        if (!(meta instanceof SkullMeta skullMeta)) {
            return;
        }

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());

        profile.setProperty(new ProfileProperty("textures", texture));

        skullMeta.setPlayerProfile(profile);
    }
}