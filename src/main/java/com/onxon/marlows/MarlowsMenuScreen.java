package com.onxon.marlows;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class MarlowsMenuScreen extends Screen {
    private final String page;
    private final List<TextFieldWidget> fields = new ArrayList<>();
    private final List<String> keys = new ArrayList<>();
    private boolean waitingForKey;

    public MarlowsMenuScreen() { this("home"); }
    private MarlowsMenuScreen(String page) {
        super(Text.literal("Onxon's rig mod"));
        this.page = page;
        MarlowsConfig.load();
    }

    @Override protected void init() {
        fields.clear(); keys.clear();
        if (page.equals("home")) {
            addPage("Paper", "paper", 80);
            addPage("BJ", "bj", 110);
            addPage("50/50", "fifty", 140);
            addPage("Odd or Even", "odd", 170);
            addPage("RR", "rr", 200);
            addDrawableChild(ButtonWidget.builder(Text.literal("Hidden keybind settings"), b -> open("keybind"))
                .dimensions(width / 2 - 105, 235, 210, 23).build());
            return;
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("< Back"), b -> open("home"))
            .dimensions(20, 20, 75, 20).build());
        int y = 85;
        if (page.equals("paper")) {
            for (int i = 1; i <= 9; i++) addField("paper." + i, "Number " + i, y + (i - 1) * 25);
        } else if (page.equals("bj")) {
            int i = 0;
            for (String s : new String[]{"1_11", "3", "4", "5", "6", "7", "8", "9", "10"})
                addField("bj." + s, s.replace('_', '/'), y + (i++) * 25);
        } else if (page.equals("fifty")) {
            addField("fifty.left", "Left item", y);
            addField("fifty.right", "Right item", y + 35);
        } else if (page.equals("odd")) {
            for (int i = 1; i <= 4; i++) addField("odd." + i, "Odd " + i, y + (i - 1) * 25);
            for (int i = 1; i <= 4; i++) addField("even." + i, "Even " + i, y + 115 + (i - 1) * 25);
            addField("or.item", "Or item", y + 225);
        } else if (page.equals("keybind")) {
            addDrawableChild(ButtonWidget.builder(Text.literal("Change menu key"), b -> waitingForKey = true)
                .dimensions(width / 2 - 100, 100, 200, 24).build());
            addDrawableChild(ButtonWidget.builder(Text.literal("Reset to O"), b -> MarlowsConfig.setMenuKey(GLFW.GLFW_KEY_O))
                .dimensions(width / 2 - 100, 132, 200, 24).build());
        }
        if (!page.equals("keybind")) {
            addDrawableChild(ButtonWidget.builder(Text.literal("Save settings"), b -> save())
                .dimensions(width - 145, height - 35, 125, 20).build());
        }
    }

    private void addPage(String name, String id, int y) {
        addDrawableChild(ButtonWidget.builder(Text.literal(name), b -> open(id))
            .dimensions(width / 2 - 105, y, 210, 23).build());
    }
    private void addField(String key, String name, int y) {
        TextFieldWidget f = new TextFieldWidget(textRenderer, 55, y, 300, 20, Text.literal(name));
        f.setMaxLength(128);
        f.setPlaceholder(Text.literal("e.g. minecraft:diamond"));
        f.setText(MarlowsConfig.get(key));
        addDrawableChild(f); fields.add(f); keys.add(key);
    }
    private void save() {
        for (int i = 0; i < fields.size(); i++) MarlowsConfig.put(keys.get(i), fields.get(i).getText().trim());
        MarlowsConfig.save();
    }
    private void open(String nextPage) {
        if (client != null) client.setScreen(new MarlowsMenuScreen(nextPage));
    }

    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (waitingForKey) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) { waitingForKey = false; return true; }
            if (keyCode != GLFW.GLFW_KEY_UNKNOWN) {
                MarlowsConfig.setMenuKey(keyCode);
                waitingForKey = false;
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.fill(0, 0, width, 44, 0xE5101422);
        context.drawCenteredTextWithShadow(textRenderer, "ONXON'S RIG MOD", width / 2, 13, 0xFFB77CFF);
        String heading = page.equals("home") ? "Marlow's crystal optimiser" : page.equals("keybind") ? "Hidden keybind settings" : page.toUpperCase();
        context.drawCenteredTextWithShadow(textRenderer, heading, width / 2, 53, 0xFFFFFFFF);
        if (page.equals("keybind")) {
            String keyName = InputUtil.fromKeyCode(MarlowsConfig.getMenuKey(), 0).getLocalizedText().getString();
            context.drawCenteredTextWithShadow(textRenderer, "Current menu key: " + keyName, width / 2, 78, 0xFFE0E3EC);
            context.drawCenteredTextWithShadow(textRenderer, waitingForKey ? "Press any key (ESC cancels)" : "This key is hidden from Minecraft Controls", width / 2, 170, 0xFFE0E3EC);
        } else if (page.equals("rr")) {
            context.drawCenteredTextWithShadow(textRenderer, "RR configuration is reserved for a future update.", width / 2, 100, 0xFFE0E3EC);
        } else if (!page.equals("home")) {
            context.drawTextWithShadow(textRenderer, "Enter item IDs, for example minecraft:diamond", 55, 68, 0xFFE0E3EC);
        }
        super.render(context, mouseX, mouseY, delta);
    }
    @Override public boolean shouldPause() { return false; }
}
