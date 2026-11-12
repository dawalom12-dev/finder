package com.example.packetlogger;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketLoggerClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("PacketLogger");
    private static final Identifier BUNGEE_CHANNEL = new Identifier("bungeecord", "main");

    @Override
    public void onInitializeClient() {
        // Регистрируем обработчик входящих сообщений от BungeeCord
        ClientPlayNetworking.registerGlobalReceiver(BUNGEE_CHANNEL, (client, handler, buf, responseSender) -> {
            // Читаем подканал
            String subchannel = buf.readString();
            // Обрабатываем ответы
            if (subchannel.equals("GetServer")) {
                String serverName = buf.readString();
                report("Вы на сервере: " + serverName);
                // Запрашиваем IP этого сервера
                requestServerIP(serverName);
            } else if (subchannel.equals("ServerIP")) {
                String serverName = buf.readString();
                String ip = buf.readString();
                int port = buf.readInt();
                report("IP сервера " + serverName + ": " + ip + ":" + port);
            }
        });

        // При подключении к серверу запрашиваем имя текущего сервера
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            requestServerName();
        });
    }

    private void requestServerName() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString("GetServer");
        ClientPlayNetworking.send(BUNGEE_CHANNEL, buf);
    }

    private void requestServerIP(String serverName) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString("ServerIP");
        buf.writeString(serverName);
        ClientPlayNetworking.send(BUNGEE_CHANNEL, buf);
    }

    public static void report(String message) {
        LOGGER.info(message);
        MinecraftClient client = MinecraftClient.getInstance();
        client.execute(() -> {
            if (client.player != null) {
                client.player.sendMessage(
                        Text.literal("[PacketLogger] " + message).formatted(Formatting.YELLOW), false);
            }
        });
    }
}
