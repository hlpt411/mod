package com.axiom.client.module.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import com.axiom.client.event.TickEvent;
import com.axiom.client.module.Category;
import com.axiom.client.module.Module;
import com.axiom.client.module.settings.BooleanSetting;
import com.axiom.client.module.settings.NumberSetting;
import com.axiom.client.util.BlockUtil;

/**
 * Places blocks under the player's feet while moving in the air.
 * Tower mode places blocks while the player holds jump.
 */
public class ScaffoldModule extends Module {
    private final BooleanSetting tower = addSetting(new BooleanSetting("Tower", false));
    private final NumberSetting delay = addSetting(new NumberSetting("Delay", 1.0, 0.0, 10.0, 1.0));

    private int tickCounter = 0;

    public ScaffoldModule() {
        super("Scaffold", "Auto-places blocks beneath you", Category.MOVEMENT);
        setKey(66); // B
    }

    @Override
    public void onTick(TickEvent event) {
        MinecraftClient client = event.client;
        ClientPlayerEntity player = client.player;
        if (player == null || client.interactionManager == null) return;
        if (tickCounter-- > 0) return;

        boolean shouldPlace = !player.isOnGround() || tower.getValue() && client.options.jumpKey.isPressed();
        if (!shouldPlace) return;

        BlockPos placePos = player.getBlockPos().down();
        if (!client.world.getBlockState(placePos).isAir()) return;

        BlockPos support = BlockUtil.findSolidNeighbor(client, placePos);
        if (support == null) return;

        int slot = BlockUtil.findBlockInHotbar(player);
        if (slot == -1) return;

        int prevSlot = player.getInventory().selectedSlot;
        player.getInventory().selectedSlot = slot;

        Direction face = Direction.fromVector(
                placePos.getX() - support.getX(),
                placePos.getY() - support.getY(),
                placePos.getZ() - support.getZ()
        );
        if (face == null) face = Direction.UP;

        Vec3d hitVec = Vec3d.ofCenter(support).add(Vec3d.of(face.getVector()).multiply(0.5));
        BlockHitResult hit = new BlockHitResult(hitVec, face, support, false);

        client.interactionManager.interactBlock(player, Hand.MAIN_HAND, hit);
        player.swingHand(Hand.MAIN_HAND);

        player.getInventory().selectedSlot = prevSlot;
        tickCounter = delay.getValue().intValue();
    }
}
