package com.axiom.client.module.combat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import com.axiom.client.event.TickEvent;
import com.axiom.client.module.Category;
import com.axiom.client.module.Module;
import com.axiom.client.util.InventoryUtil;

/**
 * Keeps a Totem of Undying in the offhand.
 * Moves a totem from the inventory when the offhand is empty or holding something else.
 */
public class OffhandManagerModule extends Module {
    private int cooldown = 0;

    public OffhandManagerModule() {
        super("OffhandManager", "Auto-refills the offhand with totems", Category.COMBAT);
        setKey(84); // T
    }

    @Override
    public void onTick(TickEvent event) {
        MinecraftClient client = event.client;
        ClientPlayerEntity player = client.player;
        if (player == null || client.interactionManager == null) return;
        if (cooldown-- > 0) return;

        ItemStack offhand = player.getOffHandStack();
        if (!InventoryUtil.needsTotem(offhand)) return;

        int slot = InventoryUtil.findInventorySlot(player, Items.TOTEM_OF_UNDYING);
        if (slot == -1) return;

        int screenSlot = InventoryUtil.toScreenHandlerSlot(slot);
        int syncId = player.playerScreenHandler.syncId;

        // Pick up the totem and place it into the offhand slot (45).
        client.interactionManager.clickSlot(syncId, screenSlot, 0, SlotActionType.PICKUP, player);
        client.interactionManager.clickSlot(syncId, 45, 0, SlotActionType.PICKUP, player);
        cooldown = 10;
    }
}
