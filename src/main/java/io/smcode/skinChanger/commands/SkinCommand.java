package io.smcode.skinChanger.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.smcode.skinChanger.service.SkinService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.chipsonsky.cache.api.SkinCacheAPI;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkinCommand implements CommandExecutor {

    private final SkinService skinService;
    private final SkinCacheAPI skinCacheAPI;

    public SkinCommand(SkinService skinService, SkinCacheAPI skinCacheAPI) {
        this.skinService = skinService;
        this.skinCacheAPI = skinCacheAPI;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof final Player player)) {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cUsage: /skin <player>");
            return true;
        }

        final String targetSkin = args[0];
        final PlayerProfile playerProfile = player.getPlayerProfile();

        final Collection<ProfileProperty> property = skinService.getTextureProperty(targetSkin);
        if (property == null) {
            player.sendMessage("Skin not found");
            return false;
        }

        playerProfile.setProperties(property);
        skinCacheAPI.put(player.getUniqueId(), targetSkin);

        player.setPlayerProfile(playerProfile);

        player.sendMessage("§aYour skin has been changed!");
        return true;
    }
}
