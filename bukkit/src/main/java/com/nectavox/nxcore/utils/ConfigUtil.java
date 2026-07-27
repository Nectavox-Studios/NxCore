package com.nectavox.nxcore.utils;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class ConfigUtil {
    final JavaPlugin plugin;

    public void updateFile(String path) {
        File configFile = new File(plugin.getDataFolder(), path);
        YamlConfiguration currentConfig = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultStream = plugin.getResource(path);
        assert defaultStream != null;
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(defaultStream, StandardCharsets.UTF_8)
        );

        boolean changed = false;
        for (String key : defaultConfig.getKeys(true)) {
            if (!currentConfig.contains(key)) {
                currentConfig.set(key, defaultConfig.get(key));
                changed = true;
            }
        }

        if (changed) {
            try {
                currentConfig.save(configFile);
                plugin.getLogger().info("🔄 " + path + " updated with new default keys.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
