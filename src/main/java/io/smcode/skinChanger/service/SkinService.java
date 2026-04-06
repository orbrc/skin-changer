package io.smcode.skinChanger.service;

import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkinService implements SkinServiceAPI {

    private static final String PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String SKIN_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    private static final Map<String, Collection<ProfileProperty>> cache = new HashMap<>();

    @Override
    @Nullable
    public Collection<ProfileProperty> getTextureProperty(String targetSkin) {
        if (cache.containsKey(targetSkin))
            return cache.get(targetSkin);

        try {
            final String profileResponse = makeRequest(PROFILE_URL + targetSkin);
            final JsonObject profileObject = JsonParser.parseString(profileResponse).getAsJsonObject();
            final String uuid = profileObject.get("id").getAsString();

            final String skinResponse = makeRequest(SKIN_URL.formatted(uuid));

            final JsonObject skinObject = JsonParser.parseString(skinResponse).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            final String value = skinObject.get("value").getAsString();
            final String signature = skinObject.get("signature").getAsString();

            final ProfileProperty profileProperty = new ProfileProperty("textures", value, signature);

            cache.put(targetSkin, List.of(profileProperty));

            return List.of(profileProperty);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private String makeRequest(String url) {

        try (final HttpClient client = HttpClient.newBuilder().build()) {
            final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
