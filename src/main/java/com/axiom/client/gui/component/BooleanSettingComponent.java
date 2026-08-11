package com.axiom.client.gui.component;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import com.axiom.client.gui.theme.Theme;
import com.axiom.client.module.settings.BooleanSetting;
import com.axiom.client.module.settings.Setting;

/**
 * ClickGUI toggle for boolean settings.
 */
public class BooleanSettingComponent extends SettingComponent {
    private final BooleanSetting booleanSetting;

    public BooleanSettingComponent(Setting<?> setting, int x, int y, int width) {
        super(setting, x, y, width);
        this.booleanSetting = (BooleanSetting) setting;
    }

    @Override
    public void render(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY) {
        context.fill(x, y, x + width, y + Theme.SETTING_HEIGHT, Theme.SETTING_BG);
        String label = setting.getName();
        context.drawTextWithShadow(textRenderer, label, x + 2, y + 1, Theme.TEXT);

        int boxSize = 6;
        int bx = x + width - boxSize - 3;
        int by = y + 2;
        context.fill(bx, by, bx + boxSize, by + boxSize, booleanSetting.getValue() ? Theme.ENABLED : Theme.DISABLED);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + Theme.SETTING_HEIGHT) {
            booleanSetting.toggle();
            return true;
        }
        return false;
    }
}
