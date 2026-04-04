package ru.chipsonsky.event;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.smcode.skinChanger.service.SkinService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.chipsonsky.cache.api.SkinCacheAPI;

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

        final String skin = skinCacheAPI.get(player.getUniqueId());

        if (skin == null)
            return;

        final PlayerProfile playerProfile = player.getPlayerProfile();
        playerProfile.setProperties(skinService.getTextureProperty(skin));
        player.setPlayerProfile(playerProfile);

        player.sendMessage("§aYour skin has been changed!");
    }
}
