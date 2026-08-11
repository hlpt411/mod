package com.axiom.client.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import com.axiom.client.AxiomClientMod;
import com.axiom.client.config.ConfigManager;
import com.axiom.client.module.Module;
import com.axiom.client.module.ModuleManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Registers the global ClickGUI key and polls module keybinds every tick.
 * Module keys are stored as raw GLFW codes and do not need a registered KeyBinding.
 */
public class KeyInputHandler {
    public static KeyBinding clickGuiKey;
    public static KeyBinding hudEditKey;
    private static final Map<Integer, Boolean> keyStates = new HashMap<>();

    public static void register() {
        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.axiomclient.clickgui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.axiomclient.general"
        ));
        hudEditKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.axiomclient.hudedit",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.axiomclient.general"
        ));
    }

    public static void checkGuiToggle(MinecraftClient client) {
        while (clickGuiKey.wasPressed()) {
            AxiomClientMod.openClickGui();
        }
    }

    public static void checkModuleToggles(MinecraftClient client) {
        long handle = client.getWindow().getHandle();
        for (Module m : ModuleManager.getModules()) {
            int key = m.getKey();
            if (key <= 0) continue;
            boolean down = InputUtil.isKeyPressed(handle, key);
            boolean wasDown = keyStates.getOrDefault(key, false);
            if (down && !wasDown) {
                m.toggle();
                ConfigManager.save();
            }
            keyStates.put(key, down);
        }
    }
}
