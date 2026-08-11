package com.axiom.client.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import com.axiom.client.module.ModuleManager;
import com.axiom.client.module.combat.VelocityReducerModule;

/**
 * Scales knockback strength for the local player.
 * A value of 0 cancels all knockback; 100 keeps vanilla behaviour.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyVariable(method = "takeKnockback", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double axiom$scaleKnockback(double strength) {
        if (!((Object) this instanceof ClientPlayerEntity)) return strength;

        VelocityReducerModule mod = ModuleManager.getModule(VelocityReducerModule.class);
        if (mod == null || !mod.isEnabled()) return strength;

        return strength * (mod.getPercent() / 100.0);
    }
}
