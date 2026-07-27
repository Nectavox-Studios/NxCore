package com.nectavox.nxcore.models;

import lombok.Builder;
import lombok.Value;
import org.bukkit.Material;

import java.util.List;

@Value
@Builder
public class GuiItemData {
    Material material;
    String name;

    @Builder.Default
    List<String> lore = List.of();

    int slot;
    String id;
    String head;

    @Builder.Default
    int amount = 1;

    @Builder.Default
    boolean glow = false;

    Integer customModelData;
}