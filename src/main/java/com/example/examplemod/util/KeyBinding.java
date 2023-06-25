package com.example.examplemod.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_HACKING = "key.category.examplemod.hacking";
    public static final String KEY_HACK_DEVICE = "key.examplemod.hack_device";

    public static final KeyMapping HACK_KEY = new KeyMapping(KEY_HACK_DEVICE, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O,KEY_CATEGORY_HACKING);
}
