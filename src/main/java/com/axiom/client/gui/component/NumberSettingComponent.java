package com.axiom.client.gui.component;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import com.axiom.client.gui.theme.Theme;
import com.axiom.client.module.settings.NumberSetting;
import com.axiom.client.module.settings.Setting;

/**
 * ClickGUI slider for numeric settings.
 */
public class NumberSettingComponent extends SettingComponent {
    private final NumberSetting numberSetting;
    private boolean dragging = false;

    public NumberSettingComponent(Setting<?> setting, int x, int y, int width) {
        super(setting, x, y, width);
        this.numberSetting = (NumberSetting) setting;
    }

    @Override
    public void render(DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY) {
        context.fill(x, y, x + width, y + Theme.SETTING_HEIGHT, Theme.SETTING_BG);
        context.drawTextWithShadow(textRenderer, setting.getName(), x + 2, y + 1, Theme.TEXT);

        int barHeight = 3;
        int barY = y + Theme.SETTING_HEIGHT - barHeight - 1;
        int barX = x + 2;
        int barW = width - 4;
        context.fill(barX, barY, barX + barW, barY + barHeight, Theme.DISABLED);

        double pct = (numberSetting.getValue() - numberSetting.getMin()) / (numberSetting.getMax() - numberSetting.getMin());
        int fill = (int) (barW * pct);
        context.fill(barX, barY, barX + fill, barY + barHeight, Theme.ACCENT);

        String valueText = String.format("%.1f", numberSetting.getValue());
        context.drawTextWithShadow(textRenderer, valueText, x + width - textRenderer.getWidth(valueText) - 2, y + 1, Theme.TEXT_SECONDARY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + Theme.SETTING_HEIGHT) {
            dragging = true;
            updateValue(mouseX);
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button) {
        if (!dragging) return false;
        updateValue(mouseX);
        return true;
    }

    private void updateValue(double mouseX) {
        int barX = x + 2;
        int barW = width - 4;
        double pct = Math.max(0.0, Math.min(1.0, (mouseX - barX) / barW));
        double value = numberSetting.getMin() + pct * (numberSetting.getMax() - numberSetting.getMin());
        numberSetting.setValueDouble(value);
    }
}
