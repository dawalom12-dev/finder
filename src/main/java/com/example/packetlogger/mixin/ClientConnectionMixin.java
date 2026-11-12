package com.example.packetlogger.mixin;

import com.example.packetlogger.PacketLoggerClient;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.common.ServerTransferS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V",
            at = @At("HEAD"))
    private void packetlogger$onReceive(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof ServerTransferS2CPacket transfer) {
            PacketLoggerClient.report("Transfer -> " + transfer.host() + ":" + transfer.port());
        }
    }
}
