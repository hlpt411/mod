package com.axiom.client.gui.component;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import com.axiom.client.config.ConfigManager;
import com.axiom.client.gui.theme.Theme;
import com.axiom.client.module.Category;
import com.axiom.client.module.Module;
import com.axiom.client.module.ModuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A single draggable panel inside the ClickGUI.
 * Holds all modules belonging to one category.
 */
public class CategoryPanel {
    private final Category category;
    private int x, y, width;
    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;
    private final List<ModuleButton> buttons = new ArrayList<>();
    private double scroll = 0;

    public CategoryPanel(Category category, int x, int y, int width) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        rebuildButtons();
    }

    public void rebuildButtons() {
        buttons.clear();
        for (Module m : ModuleManager.getModules()) {
            if (m.getCategory() == category) {
                buttons.add(new ModuleButton(m, this));
            }
        }
    }

    public void render(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY) {
        if (dragging) {
            x = mouseX - dragOffsetX;
            y = mouseY - dragOffsetY;
        }

        // Header
        context.fill(x, y, x + width, y + Theme.HEADER_HEIGHT, Theme.PANEL_HEADER);
        context.drawTextWithShadow(textRenderer, category.display, x + 4, y + 3, Theme.TEXT);

        // Body background
        int contentY = y + Theme.HEADER_HEIGHT;
        int contentHeight = Math.min(200, buttons.size() * Theme.MODULE_HEIGHT + 4);
        context.fill(x, contentY, x + width, contentY + contentHeight, Theme.PANEL_BACKGROUND);

        // Buttons
        int by = contentY + 2 - (int) scroll;
        for (ModuleButton button : buttons) {
            button.setY(by);
            button.render(context, textRenderer, mouseX, mouseY);
            by += Theme.MODULE_HEIGHT;
            if (button.isExpanded()) {
                by += button.getExpandedHeight();
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isOverHeader(mouseX, mouseY) && button == 0) {
            dragging = true;
            dragOffsetX = (int) (mouseX - x);
            dragOffsetY = (int) (mouseY - y);
            return true;
        }
        if (!isOverBody(mouseX, mouseY)) return false;
        for (ModuleButton b : buttons) {
            if (b.mouseClicked(mouseX, mouseY, button)) return true;
        }
        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        for (ModuleButton b : buttons) {
            b.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button) {
        for (ModuleButton b : buttons) {
            if (b.mouseDragged(mouseX, mouseY, button)) return true;
        }
        return dragging;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!isOverBody(mouseX, mouseY)) return false;
        scroll -= amount * 10;
        int maxScroll = Math.max(0, getTotalHeight() - 200);
        scroll = Math.max(0, Math.min(scroll, maxScroll));
        return true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ModuleButton b : buttons) {
            if (b.keyPressed(keyCode, scanCode, modifiers)) return true;
        }
        return false;
    }

    private int getTotalHeight() {
        int h = buttons.size() * Theme.MODULE_HEIGHT + 4;
        for (ModuleButton b : buttons) {
            if (b.isExpanded()) h += b.getExpandedHeight();
        }
        return h;
    }

    private boolean isOverHeader(double mx, double my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + Theme.HEADER_HEIGHT;
    }

    private boolean isOverBody(double mx, double my) {
        int contentY = y + Theme.HEADER_HEIGHT;
        return mx >= x && mx <= x + width && my >= contentY && my <= contentY + 200;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }
}
