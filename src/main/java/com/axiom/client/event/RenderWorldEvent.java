package com.axiom.client.event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Fired after entities are rendered in world space.
 * Provides the active matrix stack, camera and tick delta for 3D overlays.
 */
public class RenderWorldEvent extends Event {
    public final MatrixStack matrices;
    public final Camera camera;
    public final RenderTickCounter tickCounter;
    public final MinecraftClient client;

    public RenderWorldEvent(MatrixStack matrices, Camera camera, RenderTickCounter tickCounter, MinecraftClient client) {
        this.matrices = matrices;
        this.camera = camera;
        this.tickCounter = tickCounter;
        this.client = client;
    }
}
