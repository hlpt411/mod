package com.axiom.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import com.axiom.client.config.ConfigManager;
import com.axiom.client.gui.component.CategoryPanel;
import com.axiom.client.gui.theme.Theme;
import com.axiom.client.module.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * The main ClickGUI screen.
 * Dark mode panels with draggable categories, module toggles and live settings.
 */
public class ClickGuiScreen extends Screen {
    private final List<CategoryPanel> panels = new ArrayList<>();

    public ClickGuiScreen() {
        super(Text.literal("Axiom Client"));
    }

    @Override
    protected void init() {
        panels.clear();
        int x = 20;
        int y = 20;
        for (Category category : Category.values()) {
            panels.add(new CategoryPanel(category, x, y, Theme.PANEL_WIDTH));
            x += Theme.PANEL_WIDTH + Theme.PANEL_SPACING;
            if (x + Theme.PANEL_WIDTH > width) {
                x = 20;
                y += 160;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Dim the game world slightly so the GUI pops.
        context.fill(0, 0, width, height, Theme.BACKGROUND_DIM);
        for (CategoryPanel panel : panels) {
            panel.render(context, textRenderer, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (HudOverlay.INSTANCE.mouseClicked(mouseX, mouseY, button)) return true;
        for (CategoryPanel panel : panels) {
            if (panel.mouseClicked(mouseX, mouseY, button)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        HudOverlay.INSTANCE.mouseReleased();
        for (CategoryPanel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (HudOverlay.INSTANCE.mouseDragged(mouseX, mouseY)) return true;
        for (CategoryPanel panel : panels) {
            if (panel.mouseDragged(mouseX, mouseY, button)) return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (CategoryPanel panel : panels) {
            if (panel.mouseScrolled(mouseX, mouseY, verticalAmount)) return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Pass key events to panels for keybind capture.
        for (CategoryPanel panel : panels) {
            if (panel.keyPressed(keyCode, scanCode, modifiers)) return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        ConfigManager.save();
        super.close();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
