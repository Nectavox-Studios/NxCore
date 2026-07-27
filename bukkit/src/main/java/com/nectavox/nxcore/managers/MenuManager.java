package com.nectavox.nxcore.managers;

import com.nectavox.nxcore.NxPlugin;
import com.nectavox.nxcore.models.GuiData;
import com.nectavox.nxcore.models.GuiItemData;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

@RequiredArgsConstructor
public class MenuManager {

    private static final String MENU_FOLDER = "menus";
    private static final int MIN_ROWS = 1;
    private static final int MAX_ROWS = 6;

    private final NxPlugin plugin;
    private final Map<String, GuiData> guis = new ConcurrentHashMap<>();

    public void loadMenus(boolean update) {
        File dir = new File(plugin.getDataFolder(), MENU_FOLDER);
        if (!dir.exists() && !dir.mkdirs()) {
            plugin.getLogger().severe("Could not create menus directory: " + dir.getPath());
            return;
        }

        if (update)
            copyMissingDefaultMenus(dir);

        File[] files = dir.listFiles((f, name) -> name.endsWith(".yml"));
        if (files == null || files.length == 0) {
            plugin.getLogger().warning("No menu files found in " + dir.getPath());
            return;
        }

        Map<String, GuiData> loaded = new ConcurrentHashMap<>();

        for (File file : files) {
            try {
                GuiData gui = loadMenu(file, update);
                loaded.put(file.getName().replace(".yml", ""), gui);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load menu: " + file.getName(), e);
            }
        }

        guis.clear();
        guis.putAll(loaded);
    }

    private void copyMissingDefaultMenus(File targetDir) {
        File jarFile;
        try {
            jarFile = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not resolve plugin jar location.", e);
            return;
        }

        Set<String> defaultMenuNames = new HashSet<>();

        try (JarFile jar = new JarFile(jarFile)) {
            var entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (!entry.isDirectory() && name.startsWith(MENU_FOLDER + "/") && name.endsWith(".yml")) {
                    defaultMenuNames.add(name);
                }
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not read plugin jar to discover default menus.", e);
            return;
        }

        for (String resourcePath : defaultMenuNames) {
            String fileName = resourcePath.substring(MENU_FOLDER.length() + 1);
            File target = new File(targetDir, fileName);

            if (!target.exists()) {
                plugin.saveResource(resourcePath, false);
            }
        }
    }

    private GuiData loadMenu(File file, boolean update) {
        if (update) {
            plugin.getConfigUtil().updateFile(MENU_FOLDER + "/" + file.getName());
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String title = config.getString("title");
        if (title == null || title.isBlank()) {
            plugin.getLogger().warning("Menu '" + file.getName() + "' has no title, using filename.");
            title = file.getName().replace(".yml", "");
        }

        int rows = clamp(config.getInt("rows", 6), MIN_ROWS, MAX_ROWS);
        int itemsPerPage = Math.max(0, config.getInt("items-per-page", 0));

        Map<String, GuiItemData> items = new ConcurrentHashMap<>();
        ConfigurationSection itemSec = config.getConfigurationSection("items");

        if (itemSec != null) {
            for (String key : itemSec.getKeys(false)) {
                GuiItemData item = parseItem(itemSec, key, file);
                if (item != null) {
                    items.put(key, item);
                }
            }
        } else {
            plugin.getLogger().warning("Menu '" + file.getName() + "' has no 'items' section.");
        }

        return new GuiData(title, rows, itemsPerPage, items);
    }

    private GuiItemData parseItem(ConfigurationSection itemSec, String key, File file) {
        ConfigurationSection section = itemSec.getConfigurationSection(key);
        if (section == null) {
            plugin.getLogger().warning("Item '" + key + "' in " + file.getName() + " is invalid, skipping.");
            return null;
        }

        Material material = Material.AIR;
        String materialName = section.getString("material");
        if (materialName != null) {
            try {
                material = Material.valueOf(materialName.toUpperCase());
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid material '" + materialName + "' for '" + key + "' in " + file.getName());
            }
        }

        return GuiItemData.builder()
                .material(material)
                .name(section.getString("name", ""))
                .lore(List.copyOf(section.getStringList("lore")))
                .slot(section.getInt("slot", 0))
                .id(key)
                .head(section.getString("head"))
                .amount(Math.max(1, section.getInt("amount", 1)))
                .glow(section.getBoolean("glow", false))
                .customModelData(section.contains("custom-model-data") ? section.getInt("custom-model-data") : null)
                .build();
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public GuiData getGui(String key) {
        return guis.get(key);
    }
}