package com.onxon.marlows;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

public class MarlowsClient implements ClientModInitializer {
    private boolean menuKeyWasDown;

    @Override public void onInitializeClient() {
        MarlowsConfig.load();
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
        if (client.getWindow() == null) return;
        boolean down = InputUtil.isKeyPressed(client.getWindow().getHandle(), MarlowsConfig.getMenuKey());
        if (down && !menuKeyWasDown && client.currentScreen == null) {
            client.setScreen(new MarlowsMenuScreen());
        }
        menuKeyWasDown = down;
    }
}
