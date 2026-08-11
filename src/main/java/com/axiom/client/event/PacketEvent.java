package com.axiom.client.event;

import net.minecraft.network.packet.Packet;

/**
 * Fired from the packet Mixin hooks for both inbound and outbound traffic.
 * Modules can inspect or cancel packets here.
 */
public class PacketEvent extends Event {
    public final Packet<?> packet;
    public final boolean outbound;
    private boolean cancelled;

    public PacketEvent(Packet<?> packet, boolean outbound) {
        this.packet = packet;
        this.outbound = outbound;
    }

    public boolean outbound() {
        return outbound;
    }

    public void cancel() {
        this.cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
