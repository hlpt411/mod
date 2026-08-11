package com.axiom.client.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.axiom.client.event.PacketEvent;
import com.axiom.client.module.ModuleManager;

/**
 * Hooks all packet traffic through the client connection.
 * Outbound packets are intercepted before serialization; inbound packets are
 * intercepted before they reach the packet listener.
 */
@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(
            method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Z)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void axiom$onSendPacket(Packet<?> packet, PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        PacketEvent event = new PacketEvent(packet, true);
        ModuleManager.onPacket(event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static void axiom$onHandlePacket(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
        PacketEvent event = new PacketEvent(packet, false);
        ModuleManager.onPacket(event);
        if (event.isCancelled()) ci.cancel();
    }
}
