package com.onxon.marlows;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class MarlowsConfig {
    private static final Properties DATA = new Properties();
    private static boolean loaded;
    private MarlowsConfig() {}
    private static Path file() {
        return MinecraftClient.getInstance().runDirectory.toPath()
            .resolve("config/marlows-crystal-optimiser.properties");
    }
    public static synchronized void load() {
        if (loaded) return;
        loaded = true;
        try {
            Path file = file();
            Files.createDirectories(file.getParent());
            if (Files.exists(file)) try (var in = Files.newInputStream(file)) { DATA.load(in); }
        } catch (IOException e) {
            System.err.println("Marlow config load failed: " + e.getMessage());
        }
    }
    public static String get(String key) { load(); return DATA.getProperty(key, "minecraft:map"); }
    public static void put(String key, String value) { load(); DATA.setProperty(key, value); }
    public static int getMenuKey() {
        load();
        try { return Integer.parseInt(DATA.getProperty("menu.key", Integer.toString(GLFW.GLFW_KEY_O))); }
        catch (NumberFormatException ignored) { return GLFW.GLFW_KEY_O; }
    }
    public static void setMenuKey(int key) { load(); DATA.setProperty("menu.key", Integer.toString(key)); save(); }
    public static void save() {
        load();
        try {
            Path file = file();
            Files.createDirectories(file.getParent());
            try (var out = Files.newOutputStream(file)) { DATA.store(out, "Marlow's crystal optimiser"); }
        } catch (IOException e) {
            System.err.println("Marlow config save failed: " + e.getMessage());
        }
    }
}
