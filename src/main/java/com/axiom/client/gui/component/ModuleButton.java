package com.axiom.client.gui.component;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import com.axiom.client.config.ConfigManager;
import com.axiom.client.gui.theme.Theme;
import com.axiom.client.module.Module;
import com.axiom.client.module.settings.BooleanSetting;
import com.axiom.client.module.settings.ModeSetting;
import com.axiom.client.module.settings.NumberSetting;
import com.axiom.client.module.settings.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders one module inside a category panel.
 * Left click toggles the module; right click expands the settings.
 */
public class ModuleButton {
    private final Module module;
    private final CategoryPanel panel;
    private int y;
    private boolean expanded = false;
    private boolean listeningForKey = false;
    private final List<SettingComponent> settings = new ArrayList<>();

    public ModuleButton(Module module, CategoryPanel panel) {
        this.module = module;
        this.panel = panel;
        rebuildSettings();
    }

    private void rebuildSettings() {
        settings.clear();
        int sy = 0;
        for (Setting<?> s : module.getSettings()) {
            SettingComponent comp;
            if (s instanceof BooleanSetting) {
                comp = new BooleanSettingComponent(s, panel.getX() + Theme.MODULE_LEFT_MARGIN, sy, panel.getWidth() - 6);
            } else if (s instanceof NumberSetting) {
                comp = new NumberSettingComponent(s, panel.getX() + Theme.MODULE_LEFT_MARGIN, sy, panel.getWidth() - 6);
            } else if (s instanceof ModeSetting) {
                comp = new ModeSettingComponent(s, panel.getX() + Theme.MODULE_LEFT_MARGIN, sy, panel.getWidth() - 6);
            } else {
                continue;
            }
            settings.add(comp);
            sy += Theme.SETTING_HEIGHT;
        }
    }

    public void setY(int y) {
        this.y = y;
    }

    public void render(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY) {
        int x = panel.getX() + Theme.MODULE_LEFT_MARGIN;
        int width = panel.getWidth() - 6;
        boolean hover = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + Theme.MODULE_HEIGHT;
        int bg = hover ? Theme.MODULE_BG_HOVER : Theme.MODULE_BG;
        context.fill(x, y, x + width, y + Theme.MODULE_HEIGHT, bg);

        int color = module.isEnabled() ? Theme.ENABLED : Theme.TEXT;
        String display = module.getName();
        if (display.length() > 14) display = display.substring(0, 14);
        context.drawTextWithShadow(textRenderer, display, x + 2, y + 2, color);

        // Small expand arrow if the module has settings.
        if (!settings.isEmpty()) {
            String arrow = expanded ? "v" : ">";
            context.drawTextWithShadow(textRenderer, arrow, x + width - 8, y + 2, Theme.TEXT_SECONDARY);
        }

        if (expanded) {
            int sy = y + Theme.MODULE_HEIGHT;
            for (SettingComponent comp : settings) {
                comp.setY(sy);
                comp.render(context, textRenderer, mouseX, mouseY);
                sy += Theme.SETTING_HEIGHT;
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = panel.getX() + Theme.MODULE_LEFT_MARGIN;
        int width = panel.getWidth() - 6;
        if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + Theme.MODULE_HEIGHT) {
            if (expanded) {
                for (SettingComponent comp : settings) {
                    if (comp.mouseClicked(mouseX, mouseY, button)) {
                        ConfigManager.save();
                        return true;
                    }
                }
            }
            return false;
        }

        // Toggle on left click, expand on right click.
        if (button == 0) {
            module.toggle();
            ConfigManager.save();
            return true;
        } else if (button == 1 && !settings.isEmpty()) {
            expanded = !expanded;
            return true;
        }
        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        for (SettingComponent comp : settings) {
            comp.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button) {
        for (SettingComponent comp : settings) {
            if (comp.mouseDragged(mouseX, mouseY, button)) {
                ConfigManager.save();
                return true;
            }
        }
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listeningForKey) {
            module.setKey(keyCode == 256 ? -1 : keyCode); // ESC clears the bind
            listeningForKey = false;
            ConfigManager.save();
            return true;
        }
        return false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public int getExpandedHeight() {
        return settings.size() * Theme.SETTING_HEIGHT;
    }
}
