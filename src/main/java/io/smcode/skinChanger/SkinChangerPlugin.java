package io.smcode.skinChanger;

import io.smcode.skinChanger.commands.SkinCommand;
import io.smcode.skinChanger.service.SkinService;
import org.bukkit.plugin.java.JavaPlugin;
import ru.chipsonsky.cache.api.SkinCacheAPI;
import ru.chipsonsky.cache.impl.YamlSkinCache;
import ru.chipsonsky.event.JoinEvent;

import java.io.File;

public final class SkinChangerPlugin extends JavaPlugin {

    private SkinCacheAPI skinCacheAPI;
    private SkinService skinService;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        skinCacheAPI = new YamlSkinCache(getConfig(), new File(getDataFolder(), "config.yml"), this);
        skinService = new SkinService();

        skinCacheAPI.initCache();

        getCommand("skin").setExecutor(new SkinCommand(skinService, skinCacheAPI));
        getServer().getPluginManager().registerEvents(new JoinEvent(skinCacheAPI, skinService), this);

    }
}
