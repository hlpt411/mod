package com.axiom.client.event;

import net.minecraft.client.MinecraftClient;

/**
 * Fired once per client tick after the world/player exist.
 * Modules use this for logic that needs to run every frame.
 */
public class TickEvent extends Event {
    public final MinecraftClient client;

    public TickEvent(MinecraftClient client) {
        this.client = client;
    }
}
