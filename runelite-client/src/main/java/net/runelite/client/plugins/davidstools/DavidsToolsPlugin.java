package net.runelite.client.plugins.davidstools;

import javax.inject.Inject;

import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.Notifier;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

import java.time.Duration;
import java.time.Instant;

import static net.runelite.api.AnimationID.IDLE;

@PluginDescriptor(
        name = "Davids Tools",
        description = "Various tools",
        tags = {"misc"},
        loadWhenOutdated = true,
        enabledByDefault = false
)
public class DavidsToolsPlugin extends Plugin
{
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private DavidOverlay overlay;
    @Inject
    private DavidsToolsConfig config;
    @Inject
    private Notifier notifier;
    @Inject
    private Client client;

    // Agility
    private WorldPoint lastPosition;
    private Instant lastMoving;


    @Provides
    DavidsToolsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(DavidsToolsConfig.class);
    }

    @Override
    public void startUp() throws Exception
    {
        System.out.println("DavidsTools: Running");
        overlayManager.add(overlay);
    }

    @Override
    public void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event)
    {
        if (client.getGameState() != GameState.LOGGED_IN)
        {
            return;
        }
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer != event.getActor())
        {
            return;
        }
        overlayManager.remove(overlay);
        overlayManager.add(overlay);

    }

    private boolean checkMovementIdle(Duration waitDuration, Player local)
    {
        if (lastPosition == null)
        {
            lastPosition = local.getWorldLocation();
            return false;
        }

        WorldPoint position = local.getWorldLocation();

        if (lastPosition.equals(position))
        {
            if (local.getAnimation() == IDLE
                    && Instant.now().compareTo(lastMoving.plus(waitDuration)) >= 0)
            {
                return true;
            }
        }
        else
        {
            lastPosition = position;
            lastMoving = Instant.now();
        }

        return false;
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged)
    {

    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (client.getGameState() != GameState.LOGGED_IN)
        {
            return;
        }
        Player localPlayer = client.getLocalPlayer();

        if (checkMovementIdle(Duration.ofMillis(200), localPlayer))
            overlay.isMoving = false;
        else
            overlay.isMoving = true;
        overlayManager.remove(overlay);
        overlayManager.add(overlay);
    }

}
