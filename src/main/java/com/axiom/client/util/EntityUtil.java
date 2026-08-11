package com.axiom.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Helper methods for scanning and filtering entities around the local player.
 */
public class EntityUtil {

    /**
     * Returns entities around the player matching the predicate, sorted by distance.
     */
    public static List<Entity> getSortedEntities(MinecraftClient client, double range, Predicate<Entity> filter) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) return List.of();

        Box box = player.getBoundingBox().expand(range, range, range);
        List<Entity> entities = new ArrayList<>(client.world.getEntitiesByClass(Entity.class, box, filter));
        Vec3d eye = player.getEyePos();
        entities.sort(Comparator.comparingDouble(e -> e.getPos().squaredDistanceTo(eye)));
        return entities;
    }

    public static boolean isValidTarget(Entity e, ClientPlayerEntity player, boolean players, boolean mobs) {
        if (e == null || e.isRemoved() || !e.isAlive()) return false;
        if (e == player) return false;
        if (e instanceof PlayerEntity && players) return true;
        if (e instanceof MobEntity && mobs) return true;
        return false;
    }

    public static float getHealth(LivingEntity entity) {
        return entity.getHealth() + entity.getAbsorptionAmount();
    }

    public static float getMaxHealth(LivingEntity entity) {
        return entity.getMaxHealth();
    }
}
