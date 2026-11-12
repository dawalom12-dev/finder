package ru.minezone.ipviewer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;

import java.net.InetAddress;

public class ServerInfoScreen extends Screen {
    private String domain = "Не подключен";
    private String resolvedIP = "Резолвинг...";
    private boolean isResolving = false;

    public ServerInfoScreen() {
        super(Text.literal("Информация о сервере"));
    }

    @Override
    protected void init() {
        super.init();
        MinecraftClient client = MinecraftClient.getInstance();
        ServerInfo serverInfo = client.getCurrentServerEntry();

        if (serverInfo != null) {
            domain = serverInfo.address;
            // Запускаем резолв IP в отдельном потоке, чтобы не лагала игра
            if (!isResolving) {
                isResolving = true;
                new Thread(() -> {
                    try {
                        // Убираем порт, если он есть в адресе (например, play.minezone.com:25565)
                        String host = domain.split(":")[0];
                        InetAddress address = InetAddress.getByName(host);
                        resolvedIP = address.getHostAddress();
                    } catch (Exception e) {
                        resolvedIP = "Не удалось определить";
                    }
                }).start();
            }
        } else {
            domain = "Одиночная игра / Неизвестно";
            resolvedIP = "Нет";
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Рисуем полупрозрачный фон
        this.renderBackground(context, mouseX, mouseY, delta);

        // Рисуем текст
        int centerX = this.width / 2;
        int y = this.height / 2 - 40;

        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("§l§b--- Информация о сервере ---"), centerX, y, 0xFFFFFF);
        y += 30;
        
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("§fДомен / Адрес:"), centerX, y, 0xFFFFFF);
        y += 15;
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("§a" + domain), centerX, y, 0xFFFFFF);
        y += 30;

        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("§fЦифровой IP:"), centerX, y, 0xFFFFFF);
        y += 15;
        // Если IP спарсился - зеленый, если нет - красный
        String ipColor = resolvedIP.contains(".") || resolvedIP.contains(":") ? "§a" : "§c";
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(ipColor + resolvedIP), centerX, y, 0xFFFFFF);
        
        y += 40;
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("§7Нажмите ESC, чтобы закрыть"), centerX, y, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false; // Игра не встает на паузу при открытии меню
    }
}
