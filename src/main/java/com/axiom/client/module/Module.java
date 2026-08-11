package com.axiom.client.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import com.axiom.client.event.PacketEvent;
import com.axiom.client.event.RenderWorldEvent;
import com.axiom.client.event.TickEvent;
import com.axiom.client.module.settings.Setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for every feature in the client.
 * Handles enable/disable lifecycle, settings, keybind index and render callbacks.
 */
public abstract class Module {
    protected final String name;
    protected final String description;
    protected final Category category;
    protected boolean enabled;
    protected final List<Setting<?>> settings = new ArrayList<>();
    protected int key = -1; // GLFW key code, -1 means unbound

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public List<Setting<?>> getSettings() {
        return Collections.unmodifiableList(settings);
    }

    protected <T extends Setting<?>> T addSetting(T setting) {
        settings.add(setting);
        return setting;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onTick(TickEvent event) {
    }

    public void onRenderWorld(RenderWorldEvent event) {
    }

    public void onRenderHud(DrawContext context, RenderTickCounter tickCounter, int screenWidth, int screenHeight) {
    }

    public void onPacket(PacketEvent event) {
    }
}
