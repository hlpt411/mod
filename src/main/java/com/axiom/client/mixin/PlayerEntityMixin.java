package com.axiom.client.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.axiom.client.module.ModuleManager;
import com.axiom.client.module.combat.MeleeRangeModule;

/**
 * Injects into the player's entity interaction range.
 * Only affects the local client player so the server validation still decides
 * whether the hit actually lands.
 */
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true)
    private void axiom$modifyEntityInteractionRange(CallbackInfoReturnable<Double> cir) {
        if (!((Object) this instanceof ClientPlayerEntity)) return;

        MeleeRangeModule mod = ModuleManager.getModule(MeleeRangeModule.class);
        if (mod != null && mod.isEnabled()) {
            cir.setReturnValue(mod.getRange());
        }
    }
}
