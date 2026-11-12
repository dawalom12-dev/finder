package com.example.packetlogger.mixin;

import com.example.packetlogger.PacketLoggerClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onPacket", at = @At("HEAD"))
    private void onPacketReceived(Packet<?> packet, CallbackInfo ci) {
        if (PacketLoggerClient.loggingEnabled) {
            System.out.println("[S2C] " + packet.getClass().getSimpleName() + " -> " + packet);
        }
    }
}
