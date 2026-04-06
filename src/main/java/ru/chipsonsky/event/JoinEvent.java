package ru.chipsonsky.event;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.smcode.skinChanger.service.SkinService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.chipsonsky.cache.api.SkinCacheAPI;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class JoinEvent implements Listener {

    private final SkinCacheAPI skinCacheAPI;
    private final SkinService skinService;

    public JoinEvent(SkinCacheAPI skinCacheAPI, SkinService skinService) {
        this.skinCacheAPI = skinCacheAPI;
        this.skinService = skinService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        String skin = skinCacheAPI.get(player.getUniqueId());

        if (skin == null)
            skin = player.getName();

        final PlayerProfile playerProfile = player.getPlayerProfile();
        final Collection<ProfileProperty> properties = skinService.getTextureProperty(skin);

        if (properties == null)
            return;

        playerProfile.setProperties(properties);
        player.setPlayerProfile(playerProfile);
    }
}