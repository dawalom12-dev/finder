package com.example.packetlogger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier; // Добавлен импорт
import org.lwjgl.glfw.GLFW;

public class PacketLoggerClient implements ClientModInitializer {
    public static boolean loggingEnabled = false;
    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        // 1. Создаём категорию для клавиши в настройках управления
        KeyBinding.Category category = KeyBinding.Category.create(
                Identifier.of("packetlogger", "general")
        );

        // 2. Используем новый конструктор с категорией
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.packetlogger.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F7,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                loggingEnabled = !loggingEnabled;
                if (client.player != null) {
                    client.player.sendMessage(net.minecraft.text.Text.literal(
                            "§e[PacketLogger] Логирование " + (loggingEnabled ? "§aвключено" : "§cвыключено")
                    ), false);
                }
            }
        });
    }
}
