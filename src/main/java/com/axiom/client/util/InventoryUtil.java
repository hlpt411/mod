package com.axiom.client.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Quick inventory helpers used by modules like Offhand Manager and Scaffold.
 */
public class InventoryUtil {

    /**
     * Finds the first hotbar slot containing the given item.
     * Returns -1 if none is found.
     */
    public static int findHotbarSlot(ClientPlayerEntity player, Item item) {
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getStack(i).isOf(item)) return i;
        }
        return -1;
    }

    /**
     * Finds the first inventory slot (including hotbar) containing the item.
     */
    public static int findInventorySlot(ClientPlayerEntity player, Item item) {
        for (int i = 0; i < player.getInventory().main.size(); i++) {
            if (player.getInventory().main.get(i).isOf(item)) return i;
        }
        return -1;
    }

    /**
     * Converts a PlayerInventory main index to a PlayerScreenHandler slot index.
     */
    public static int toScreenHandlerSlot(int inventoryIndex) {
        return inventoryIndex < 9 ? 36 + inventoryIndex : inventoryIndex;
    }

    /**
     * Returns true if the stack is empty or a totem already.
     */
    public static boolean needsTotem(ItemStack stack) {
        return stack.isEmpty() || !stack.isOf(Items.TOTEM_OF_UNDYING);
    }
}
