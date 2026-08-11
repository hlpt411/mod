package com.axiom.client.gui.component;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import com.axiom.client.gui.theme.Theme;
import com.axiom.client.module.settings.ModeSetting;
import com.axiom.client.module.settings.Setting;

/**
 * ClickGUI button that cycles through the modes of a ModeSetting.
 */
public class ModeSettingComponent extends SettingComponent {
    private final ModeSetting modeSetting;

    public ModeSettingComponent(Setting<?> setting, int x, int y, int width) {
        super(setting, x, y, width);
        this.modeSetting = (ModeSetting) setting;
    }

    @Override
    public void render(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY) {
        context.fill(x, y, x + width, y + Theme.SETTING_HEIGHT, Theme.SETTING_BG);
        context.drawTextWithShadow(textRenderer, setting.getName() + ":", x + 2, y + 1, Theme.TEXT);

        String value = modeSetting.getValue();
        int valueWidth = textRenderer.getWidth(value);
        context.drawTextWithShadow(textRenderer, value, x + width - valueWidth - 2, y + 1, Theme.ACCENT);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + Theme.SETTING_HEIGHT) {
            modeSetting.cycle();
            return true;
        }
        return false;
    }
}
