package com.axiom.client.module.combat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import com.axiom.client.event.TickEvent;
import com.axiom.client.module.Category;
import com.axiom.client.module.Module;
import com.axiom.client.module.settings.BooleanSetting;
import com.axiom.client.module.settings.ModeSetting;
import com.axiom.client.module.settings.NumberSetting;
import com.axiom.client.util.EntityUtil;

import java.util.List;

/**
 * Automatically attacks the closest valid entity within range.
 * Includes target filtering, attack delay and optional silent/rotate aiming.
 */
public class CombatAssistModule extends Module {
    private final ModeSetting target = addSetting(new ModeSetting("Target", "All", "Players", "Mobs", "All"));
    private final NumberSetting range = addSetting(new NumberSetting("Range", 4.0, 1.0, 6.0, 0.1));
    private final NumberSetting delay = addSetting(new NumberSetting("Delay", 10.0, 0.0, 20.0, 1.0));
    private final BooleanSetting rotate = addSetting(new BooleanSetting("Rotate", false));
    private final BooleanSetting swing = addSetting(new BooleanSetting("Swing", true));

    private int tickCounter = 0;

    public CombatAssistModule() {
        super("CombatAssist", "Auto-swing at the nearest target", Category.COMBAT);
        setKey(71); // G
    }

    @Override
    public void onTick(TickEvent event) {
        MinecraftClient client = event.client;
        ClientPlayerEntity player = client.player;
        if (player == null || client.interactionManager == null) return;

        boolean players = target.getValue().equals("Players") || target.getValue().equals("All");
        boolean mobs = target.getValue().equals("Mobs") || target.getValue().equals("All");
        double r = range.getValue();

        List<Entity> targets = EntityUtil.getSortedEntities(client, r, e -> EntityUtil.isValidTarget(e, player, players, mobs));
        if (targets.isEmpty()) return;

        Entity victim = targets.get(0);
        if (!(victim instanceof LivingEntity living)) return;

        if (rotate.getValue()) {
            aimAt(player, victim);
        }

        if (tickCounter-- > 0) return;
        tickCounter = delay.getValue().intValue();

        if (player.getAttackCooldownProgress(0.5f) >= 0.95f) {
            client.interactionManager.attackEntity(player, living);
            if (swing.getValue()) player.swingHand(Hand.MAIN_HAND);
        }
    }

    private void aimAt(ClientPlayerEntity player, Entity target) {
        Vec3d eye = player.getEyePos();
        Vec3d center = target.getBoundingBox().getCenter();
        double dx = center.x - eye.x;
        double dy = center.y - eye.y;
        double dz = center.z - eye.z;
        double distXZ = MathHelper.sqrt((float) (dx * dx + dz * dz));

        float yaw = (float) (MathHelper.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0f;
        float pitch = (float) (-(MathHelper.atan2(dy, distXZ) * (180.0 / Math.PI)));

        player.setYaw(yaw);
        player.setPitch(MathHelper.clamp(pitch, -90.0f, 90.0f));
    }
}
