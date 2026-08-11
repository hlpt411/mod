package com.axiom.client.gui.component;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import com.axiom.client.module.settings.Setting;

/**
 * Base class for ClickGUI setting widgets.
 */
public abstract class SettingComponent {
    protected final Setting<?> setting;
    protected int x, y, width;

    public SettingComponent(Setting<?> setting, int x, int y, int width) {
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract void render(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY);

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button) {
        return false;
    }
}
