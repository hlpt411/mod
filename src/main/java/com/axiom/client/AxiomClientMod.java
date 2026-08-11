package com.axiom.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import com.axiom.client.config.ConfigManager;
import com.axiom.client.event.RenderWorldEvent;
import com.axiom.client.event.TickEvent;
import com.axiom.client.gui.ClickGuiScreen;
import com.axiom.client.input.KeyInputHandler;
import com.axiom.client.module.ModuleManager;

/**
 * Main entry point for the client-side utility mod.
 * Wires up config, modules, input hooks, render hooks and the HUD.
 */
public class AxiomClientMod implements ClientModInitializer {
    public static final String MOD_ID = "axiom-client";
    public static final String NAME = "Axiom Client";

    @Override
    public void onInitializeClient() {
        ModuleManager.init();
        ConfigManager.load();
        KeyInputHandler.register();

        // Tick every frame on the client; posts a TickEvent to active modules.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;
            ModuleManager.onTick(new TickEvent(client));
            KeyInputHandler.checkModuleToggles(client);
            KeyInputHandler.checkGuiToggle(client);
        });

        // World render hook; fires after entities are rendered so tracers/ESP render on top.
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world == null || client.player == null) return;
            ModuleManager.onRenderWorld(new RenderWorldEvent(
                    context.matrixStack(),
                    context.camera(),
                    context.tickCounter(),
                    client
            ));
        });

    }

    public static void openClickGui() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        client.setScreen(new ClickGuiScreen());
    }
}
