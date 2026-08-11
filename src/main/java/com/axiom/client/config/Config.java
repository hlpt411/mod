package com.axiom.client.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Serializable config root.
 * Stores per-module enabled state, keybinds and setting values.
 */
public class Config {
    public int hudX = 4;
    public int hudY = 4;
    public Map<String, ModuleConfig> modules = new HashMap<>();

    public static class ModuleConfig {
        public boolean enabled = false;
        public int key = -1;
        public Map<String, Object> settings = new HashMap<>();
    }
}
