package com.axiom.client.module.misc;

import net.minecraft.network.packet.Packet;
import com.axiom.client.event.PacketEvent;
import com.axiom.client.module.Category;
import com.axiom.client.module.Module;

/**
 * Example packet inspection module.
 * Cancels outbound animation packets when active to demonstrate packet hook usage.
 */
public class PacketLoggerModule extends Module {

    public PacketLoggerModule() {
        super("PacketLogger", "Inspect and cancel packet traffic", Category.MISC);
    }

    @Override
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.packet;
        // This is a sandbox-only example: drop player swing animation packets outbound.
        if (event.outbound() && packet.getClass().getSimpleName().contains("HandSwing")) {
            event.cancel();
        }
    }
}
