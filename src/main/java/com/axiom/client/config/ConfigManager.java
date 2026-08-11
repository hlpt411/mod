package com.axiom.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import com.axiom.client.module.Module;
import com.axiom.client.module.ModuleManager;
import com.axiom.client.module.settings.BooleanSetting;
import com.axiom.client.module.settings.ModeSetting;
import com.axiom.client.module.settings.NumberSetting;
import com.axiom.client.module.settings.Setting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loads and saves the mod configuration from the standard Fabric config folder.
 */
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("axiom-client.json");
    private static Config current;

    public static Config getCurrent() {
        if (current == null) current = new Config();
        return current;
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            current = new Config();
            save();
            return;
        }
        try {
            String json = Files.readString(CONFIG_PATH);
            current = GSON.fromJson(json, Config.class);
            if (current == null) current = new Config();
        } catch (IOException e) {
            System.err.println("Axiom Client failed to load config: " + e.getMessage());
            current = new Config();
        }
        applyToModules();
    }

    public static void save() {
        readFromModules();
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(current));
        } catch (IOException e) {
            System.err.println("Axiom Client failed to save config: " + e.getMessage());
        }
    }

    private static void readFromModules() {
        if (current == null) current = new Config();
        for (Module m : ModuleManager.getModules()) {
            Config.ModuleConfig mc = new Config.ModuleConfig();
            mc.enabled = m.isEnabled();
            mc.key = m.getKey();
            for (Setting<?> s : m.getSettings()) {
                mc.settings.put(s.getName(), s.toConfigValue());
            }
            current.modules.put(m.getName(), mc);
        }
    }

    private static void applyToModules() {
        for (Module m : ModuleManager.getModules()) {
            Config.ModuleConfig mc = current.modules.get(m.getName());
            if (mc == null) continue;
            m.setEnabled(mc.enabled);
            m.setKey(mc.key);
            for (Setting<?> s : m.getSettings()) {
                Object value = mc.settings.get(s.getName());
                if (value == null) continue;
                if (s instanceof BooleanSetting bs) bs.fromConfigValue(value);
                else if (s instanceof NumberSetting ns) ns.fromConfigValue(value);
                else if (s instanceof ModeSetting ms) ms.fromConfigValue(value);
            }
        }
    }
}
