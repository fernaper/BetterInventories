package com.kramez.betterinventories.client;

//import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
	public static KeyBinding sort;

	public static void register() {
		sort = new KeyBinding("Sort inventory", GLFW.GLFW_KEY_R, "Better Inventories"); // key.categories.inventory
		ClientRegistry.registerKeyBinding(sort);
		
		MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
	}

}