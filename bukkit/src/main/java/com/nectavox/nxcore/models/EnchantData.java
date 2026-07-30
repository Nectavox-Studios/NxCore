package com.nectavox.nxcore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.enchantments.Enchantment;

@Data
@AllArgsConstructor
public class EnchantData {
    Enchantment enchantment;
    int level;
}