package ru.minezone.ipviewer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class ServerIPViewer implements ClientModInitializer {
    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        // Регистрируем клавишу I (можно поменять в настройках управления)
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.serveripviewer.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "category.serveripviewer"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ServerInfoScreen());
                }
            }
        });
    }
}
