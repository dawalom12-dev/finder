package com.example.packetlogger.mixin;

import com.example.packetlogger.PacketLoggerClient;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    // Исходящие пакеты (Client -> Server)
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"))
    private void packetlogger$onSend(Packet<?> packet, CallbackInfo ci) {
        if (PacketLoggerClient.loggingEnabled) {
            PacketLoggerClient.LOGGER.info("[C2S] {} -> {}", packet.getClass().getSimpleName(), packet);
        }
    }

    // Входящие пакеты (Server -> Client)
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V",
            at = @At("HEAD"))
    private void packetlogger$onReceive(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (PacketLoggerClient.loggingEnabled) {
            PacketLoggerClient.LOGGER.info("[S2C] {} -> {}", packet.getClass().getSimpleName(), packet);
        }
    }
}
