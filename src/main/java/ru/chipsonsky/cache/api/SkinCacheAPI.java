package ru.chipsonsky.cache.api;

import javax.annotation.Nullable;
import java.util.UUID;

public interface SkinCacheAPI {

    void initCache();

    void put(UUID uuid, String skin);

    @Nullable
    String get(UUID uuid);

    void updateCache();
}
