package com.example.packetlogger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketLoggerClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("PacketLogger");
    public static volatile boolean loggingEnabled = false;
    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        KeyBinding.Category category = KeyBinding.Category.create(
                Identifier.of("packetlogger", "general")
        );

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
                    Text state = loggingEnabled
                            ? Text.literal("включено").formatted(Formatting.GREEN)
                            : Text.literal("выключено").formatted(Formatting.RED);
                    client.player.sendMessage(
                            Text.literal("[PacketLogger] Логирование ").formatted(Formatting.YELLOW).append(state),
                            false
                    );
                }
            }
        });
    }
}
