package com.axiom.client.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * Block placement helpers used by Scaffold and similar modules.
 */
public class BlockUtil {

    /**
     * Finds a usable block item in the player's hotbar.
     * Prefers non-totem blocks that can be placed.
     */
    public static int findBlockInHotbar(ClientPlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() instanceof BlockItem) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a solid neighbouring position that can be used as a placement anchor,
     * or null if no solid side exists.
     */
    public static BlockPos findSolidNeighbor(MinecraftClient client, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.offset(dir);
            BlockState state = client.world.getBlockState(neighbor);
            if (!state.isAir() && state.isSolid()) {
                return neighbor;
            }
        }
        return null;
    }
}
