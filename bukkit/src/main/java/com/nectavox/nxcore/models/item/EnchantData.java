package com.nectavox.nxcore.models.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.enchantments.Enchantment;

@Data
@AllArgsConstructor
public class EnchantData {
    Enchantment enchantment;
    int level;
}