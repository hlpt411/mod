package com.axiom.client.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import com.axiom.client.event.PacketEvent;
import com.axiom.client.event.RenderWorldEvent;
import com.axiom.client.event.TickEvent;
import com.axiom.client.module.combat.CombatAssistModule;
import com.axiom.client.module.combat.MeleeRangeModule;
import com.axiom.client.module.combat.OffhandManagerModule;
import com.axiom.client.module.combat.VelocityReducerModule;
import com.axiom.client.module.misc.PacketLoggerModule;
import com.axiom.client.module.movement.ScaffoldModule;
import com.axiom.client.module.visual.VisualDetectionSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Central registry for every module.
 * Responsible for ticking, rendering and dispatching packet events.
 */
public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        register(new MeleeRangeModule());
        register(new CombatAssistModule());
        register(new OffhandManagerModule());
        register(new VelocityReducerModule());
        register(new ScaffoldModule());
        register(new VisualDetectionSystem());
        register(new PacketLoggerModule());
    }

    private static void register(Module module) {
        modules.add(module);
    }

    public static List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T getModule(Class<T> clazz) {
        for (Module m : modules) {
            if (clazz.isInstance(m)) return (T) m;
        }
        return null;
    }

    public static void onTick(TickEvent event) {
        for (Module m : modules) {
            if (m.isEnabled()) m.onTick(event);
        }
    }

    public static void onRenderWorld(RenderWorldEvent event) {
        for (Module m : modules) {
            if (m.isEnabled()) m.onRenderWorld(event);
        }
    }

    public static void onRenderHud(DrawContext context, RenderTickCounter tickCounter, int sw, int sh) {
        for (Module m : modules) {
            if (m.isEnabled()) m.onRenderHud(context, tickCounter, sw, sh);
        }
    }

    public static void onPacket(PacketEvent event) {
        for (Module m : modules) {
            if (m.isEnabled()) m.onPacket(event);
            if (event.isCancelled()) break;
        }
    }

    public static void toggleByKey(int keyCode, MinecraftClient client) {
        if (client.player == null) return;
        for (Module m : modules) {
            if (m.getKey() == keyCode) {
                m.toggle();
            }
        }
    }
}
