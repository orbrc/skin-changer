package io.smcode.skinChanger.service;

import com.destroystokyo.paper.profile.ProfileProperty;

import java.util.Collection;

public interface SkinServiceAPI {
    Collection<ProfileProperty> getTextureProperty(String targetSkin);
}
