package com.axiom.client.module.settings;

import java.util.Arrays;
import java.util.List;

/**
 * Cycling string setting with a fixed list of modes.
 */
public class ModeSetting extends Setting<String> {
    private final List<String> modes;

    public ModeSetting(String name, String value, String... modes) {
        super(name, value);
        this.modes = Arrays.asList(modes);
        if (!this.modes.contains(value) && !this.modes.isEmpty()) {
            this.value = this.modes.get(0);
        }
    }

    public List<String> getModes() {
        return modes;
    }

    public void cycle() {
        int idx = modes.indexOf(value);
        value = modes.get((idx + 1) % modes.size());
    }

    @Override
    public Object toConfigValue() {
        return value;
    }

    @Override
    public void fromConfigValue(Object object) {
        if (object instanceof String s && modes.contains(s)) value = s;
    }
}
