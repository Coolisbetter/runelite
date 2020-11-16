package net.runelite.client.plugins.davidstools;

import java.awt.*;

import lombok.Getter;
import lombok.NonNull;
import net.runelite.api.Client;
import net.runelite.api.*;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;

import static net.runelite.api.AnimationID.IDLE;

public class DavidOverlay extends Overlay
{
    private final Client client;
    private final DavidsToolsConfig config;
    private final PanelComponent panelComponent = new PanelComponent();
    public boolean isMoving = false;

    @Inject
    private DavidOverlay(Client client, DavidsToolsConfig config)
    {
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        this.client = client;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        Player localPlayer = client.getLocalPlayer();
        int animation = localPlayer.getAnimation();
        WorldPoint position = localPlayer.getWorldLocation();
        panelComponent.getChildren().clear();


        String title = "Agility Assistant";
        panelComponent.getChildren().add(TitleComponent.builder()
                .text(title)
                .color(Color.GREEN)
                .build());

        // Set the size of the overlay (width)
        panelComponent.setPreferredSize(new Dimension(
                graphics.getFontMetrics().stringWidth(title) + 30,
                0));

        // Animating
        Color animatingColor = Color.RED;
        if (animation == IDLE)
            animatingColor = Color.RED;
        else
            animatingColor = Color.GREEN;
        panelComponent.getChildren().add(LineComponent.builder()
            .left("Animating:")
            .right("■")
            .rightColor(animatingColor)
            .build());


        // Moving
        Color movingColor = Color.RED;
        if (isMoving)
            movingColor = Color.GREEN;
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Moving:")
                .right("■")
                .rightColor(movingColor)
                .build());

        // Z Level
        Color groundColor;
        if (position.getPlane() == 0)
            groundColor = Color.GREEN;
        else
            groundColor = Color.RED;
        panelComponent.getChildren().add(LineComponent.builder()
                .left("On Ground:")
                .right("■")
                .rightColor(groundColor)
                .build());

        return panelComponent.render(graphics);
    }
}