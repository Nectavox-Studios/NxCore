package com.nectavox.nxcore.models;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class GuiItemData {

    private Material material;

    private String name;

    @Builder.Default
    private List<String> lore = List.of();

    private int slot;

    private String id;

    private String head;

    @Builder.Default
    private int amount = 1;

    @Builder.Default
    private boolean glow = false;

    private Integer customModelData;

    private CustomModelDataData customModelDataComponent;

    private String itemModel;

    @Builder.Default
    private boolean unbreakable = false;

    private Sound sound;

    @Builder.Default
    private List<EnchantData> enchants = List.of();

    @Builder.Default
    private Set<ItemFlag> itemFlags = Set.of();

    private org.bukkit.Color color;

    @Builder.Default
    private Map<String, Object> data = Map.of();
}