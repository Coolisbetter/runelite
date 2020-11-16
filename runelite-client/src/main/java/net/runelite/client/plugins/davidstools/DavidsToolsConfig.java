package net.runelite.client.plugins.davidstools;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("davidstools")
public interface DavidsToolsConfig extends Config
{
    @ConfigItem(
            position = 1,
            keyName = "AgilityTools",
            name = "UI Addins for Agility",
            description = "UI Addins for Agility"
    )
    default boolean AgilityTools()
    {
        return false;
    }
}
