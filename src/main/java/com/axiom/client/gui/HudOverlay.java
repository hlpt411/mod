package com.axiom.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import com.axiom.client.config.Config;
import com.axiom.client.config.ConfigManager;
import com.axiom.client.module.Module;
import com.axiom.client.module.ModuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Draggable enabled-module list drawn on the in-game HUD.
 * Position is persisted through the config.
 */
public class HudOverlay {
    public static final HudOverlay INSTANCE = new HudOverlay();

    private boolean dragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        Config cfg = ConfigManager.getCurrent();
        List<String> lines = new ArrayList<>();
        lines.add("§7Axiom Client");
        for (Module m : ModuleManager.getModules()) {
            if (m.isEnabled()) {
                lines.add("§f" + m.getName());
            }
        }

        int width = 0;
        for (String line : lines) {
            width = Math.max(width, client.textRenderer.getWidth(line));
        }
        int height = lines.size() * (client.textRenderer.fontHeight + 1);

        context.fill(cfg.hudX, cfg.hudY, cfg.hudX + width + 6, cfg.hudY + height + 4, 0xAA111111);
        int y = cfg.hudY + 2;
        for (String line : lines) {
            context.drawTextWithShadow(client.textRenderer, line, cfg.hudX + 3, y, 0xFFFFFFFF);
            y += client.textRenderer.fontHeight + 1;
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        Config cfg = ConfigManager.getCurrent();
        if (mouseX >= cfg.hudX && mouseX <= cfg.hudX + 80 && mouseY >= cfg.hudY && mouseY <= cfg.hudY + 12) {
            dragging = true;
            dragOffsetX = (int) (mouseX - cfg.hudX);
            dragOffsetY = (int) (mouseY - cfg.hudY);
            return true;
        }
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY) {
        if (!dragging) return false;
        Config cfg = ConfigManager.getCurrent();
        cfg.hudX = (int) mouseX - dragOffsetX;
        cfg.hudY = (int) mouseY - dragOffsetY;
        return true;
    }

    public void mouseReleased() {
        if (dragging) {
            dragging = false;
            ConfigManager.save();
        }
    }
}
