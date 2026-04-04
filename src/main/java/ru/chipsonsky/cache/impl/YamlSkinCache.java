package ru.chipsonsky.cache.impl;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.Nullable;
import ru.chipsonsky.cache.api.SkinCacheAPI;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class YamlSkinCache implements SkinCacheAPI {

    private final FileConfiguration config;
    private final File configFile;
    private final Map<UUID, String> cache;
    private final Plugin plugin;

    private ConfigurationSection cacheSection;

    public YamlSkinCache(FileConfiguration config, File configFile, Plugin plugin) {
        this.config = config;
        this.configFile = configFile;
        this.cache = new HashMap<>();
        this.plugin = plugin;
    }

    @Override
    public void initCache() {

        if (!config.isConfigurationSection("cache")) {
            cacheSection = config.createSection("cache");
            plugin.getLogger().log(Level.INFO, "Created cache section");
        } else {
            cacheSection = config.getConfigurationSection("cache");
        }

        assert cacheSection != null;
        for (String key :  cacheSection.getKeys(false)) {
            try {
                final UUID uuid = UUID.fromString(key);
                final String skin = cacheSection.getString(key);

                if (skin != null) {
                    cache.put(uuid, skin);
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().log(Level.SEVERE, "Invalid argument: " + e);
            }
        }
        plugin.getLogger().info("Loaded " + cache.size() + " entries from YAML cache");
    }

    @Override
    public void put(UUID uuid, String skin) {
        if (uuid == null || skin == null) return;
        cache.put(uuid, skin);
    }

    @Override
    public @Nullable String get(UUID uuid) {
        return cache.computeIfAbsent(uuid, key -> cacheSection.getString(uuid.toString()));
    }

    @Override
    public void updateCache() {

        boolean changed = false;

        for (Map.Entry<UUID, String> entry : cache.entrySet()) {
            final String key = entry.getKey().toString();
            final String newValue = entry.getValue();
            final String oldValue = cacheSection.getString(key);

            if (!newValue.equals(oldValue)) {
                cacheSection.set(key, newValue);
                changed = true;
            }
        }

        if (changed) {
            saveConfig();
        }
    }


    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
