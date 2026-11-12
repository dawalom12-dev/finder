package com.example.packetlogger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class PacketLoggerClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("PacketLogger");

    @Override
    public void onInitializeClient() {
        // Срабатывает при каждом входе в игру, в том числе при смене сервера через прокси
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            SocketAddress address = handler.getConnection().getAddress();
            report(client, "Connected to " + format(address));
        });
    }

    public static String format(SocketAddress address) {
        if (address instanceof InetSocketAddress inet) {
            String ip = inet.getAddress() != null ? inet.getAddress().getHostAddress() : "?";
            return inet.getHostString() + " (" + ip + ":" + inet.getPort() + ")";
        }
        return String.valueOf(address);
    }

    public static void report(net.minecraft.client.MinecraftClient client, String message) {
        LOGGER.info(message);
        client.execute(() -> {
            if (client.player != null) {
                client.player.sendMessage(
                        Text.literal("[PacketLogger] " + message).formatted(Formatting.YELLOW), false);
            }
        });
    }
}
