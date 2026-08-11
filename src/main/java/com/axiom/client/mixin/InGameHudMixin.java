package com.axiom.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.axiom.client.gui.HudOverlay;
import com.axiom.client.module.ModuleManager;

/**
 * Hooks the in-game HUD render pass so the draggable module list overlay
 * and VDS labels are drawn at the correct time.
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void axiom$renderHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        Window window = client.getWindow();
        ModuleManager.onRenderHud(context, tickCounter, window.getScaledWidth(), window.getScaledHeight());
        HudOverlay.INSTANCE.render(context, tickCounter);
    }
}
