package com.axiom.client.module.settings;

/**
 * On/off toggle setting.
 */
public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, Boolean value) {
        super(name, value);
    }

    public void toggle() {
        value = !value;
    }

    @Override
    public Object toConfigValue() {
        return value;
    }

    @Override
    public void fromConfigValue(Object object) {
        if (object instanceof Boolean b) value = b;
    }
}
