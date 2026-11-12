package com.example.packetlogger.mixin;

import com.example.packetlogger.PacketLoggerClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"))
    private void onPacketSent(Packet<?> packet, CallbackInfo ci) {
        if (PacketLoggerClient.loggingEnabled) {
            System.out.println("[C2S] " + packet.getClass().getSimpleName() + " -> " + packet);
        }
    }
}
