package com.nectavox.nxcore.managers;

import com.nectavox.nxcore.NxPlugin;
import com.nectavox.nxcore.utils.Color;
import com.nectavox.nxcore.utils.Placeholders;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@RequiredArgsConstructor
public class LangManager {

    private static final String NOT_DEFINED = "&cNOT_DEFINED";
    private static final String PREFIX_KEY = "PREFIX";
    private static final String FILE_NAME = "lang.yml";

    private final NxPlugin plugin;

    private final Map<String, String> languages = new ConcurrentHashMap<>();
    private final Map<String, List<String>> lores = new ConcurrentHashMap<>();

    public String get(String lang, boolean color, boolean prefix, Object... replacements) {
        String text = resolveText(lang, prefix, replacements);
        return color ? Color.colorLegacy(text) : text;
    }

    public Component getComponent(String lang, boolean color, boolean prefix, Object... replacements) {
        String text = resolveText(lang, prefix, replacements);
        return color ? Color.colorComponent(text) : Component.text(text);
    }

    public void sendMessage(Player player, String lang, boolean color, boolean prefix, Object... replacements) {
        String[] split = resolveText(lang, prefix, replacements).split("\n");

        for (String string : split) {
            plugin.getAudience().sendMessage(player, color ? Color.colorComponent(string) : Component.text(string));
        }
    }

    private String resolveText(String lang, boolean prefix, Object... replacements) {
        if (!languages.containsKey(lang)) {
            plugin.getLogger().log(Level.WARNING, "Missing language key: " + lang);
            return NOT_DEFINED;
        }

        String text = languages.get(lang);

        text = Placeholders.apply(text, replacements);

        if (prefix) {
            text = languages.getOrDefault(PREFIX_KEY, "") + text;
        }

        return text;
    }

    public List<String> getLore(String lang, boolean color, Object... replacements) {
        List<String> lore = resolveLore(lang, replacements);
        if (!color) return lore;

        List<String> colored = new ArrayList<>(lore.size());
        for (String line : lore) {
            colored.add(Color.colorLegacy(line));
        }
        return colored;
    }

    public List<Component> getLoreComponent(String lang, boolean color, Object... replacements) {
        List<String> lore = resolveLore(lang, replacements);

        List<Component> components = new ArrayList<>(lore.size());
        for (String line : lore) {
            components.add(color ? Color.colorComponent(line) : Component.text(line));
        }
        return components;
    }

    private List<String> resolveLore(String lang, Object... replacements) {
        if (!lores.containsKey(lang)) {
            plugin.getLogger().log(Level.WARNING, "Missing lore key: " + lang);
            return List.of(NOT_DEFINED);
        }

        return Placeholders.apply(lores.get(lang), replacements);
    }

    public void load(boolean update) {
        File file = new File(plugin.getDataFolder(), FILE_NAME);

        if (!file.exists()) {
            plugin.saveResource(FILE_NAME, false);
        }

        if (update) {
            plugin.getConfigUtil().updateFile(FILE_NAME);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        Map<String, String> newLanguages = new ConcurrentHashMap<>();
        Map<String, List<String>> newLores = new ConcurrentHashMap<>();

        for (Map.Entry<String, Object> entry : config.getValues(true).entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String text) {
                newLanguages.put(key, text);
            } else if (value instanceof List<?> list) {
                List<String> lore = new ArrayList<>();
                for (Object line : list) {
                    lore.add(Color.colorLegacy(String.valueOf(line)));
                }
                newLores.put(key, lore);
            }
        }

        languages.clear();
        languages.putAll(newLanguages);

        lores.clear();
        lores.putAll(newLores);
    }
}