package com.kramez.betterinventories.client;
import com.kramez.betterinventories.common.BetterInventoriesPacketHandler;
import com.kramez.betterinventories.commonPackets.SortContainerPacket;
import com.kramez.betterinventories.commonPackets.SortInventoryPacket;
import com.kramez.betterinventories.utilities.CustomSounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyInputHandler {

	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=true)
    public void onKeyInput(KeyInputEvent event) {
        if (Keybinds.sort.isPressed()) {
        	System.out.println("Sorting own inventory");
        	BetterInventoriesPacketHandler.INSTANCE.sendToServer(new SortInventoryPacket());

        	Minecraft mc = Minecraft.getInstance();
        	mc.player.playSound(CustomSounds.pressKey, 1, 1);
        }
    }
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=true)
	public void onGuiInput(GuiScreenEvent.KeyboardKeyPressedEvent.Pre evt) {
		// If pressed Sort key
		if (keyEvaluate(Keybinds.sort, evt)) {
			final Screen gui = evt.getGui();
			
			// If it is a GUI with container
			if (gui instanceof ContainerScreen && !(gui instanceof CreativeScreen)) {				
				final ContainerScreen<?> guiContainer = (ContainerScreen<?>) gui;
				
				// If it is some kind of chest
				if (guiContainer instanceof ChestScreen) {					
					final Container container = guiContainer.getContainer();
					
					// Check it just in case
					if (container != null && container.inventorySlots != null) {
						System.out.println("HOLA MUNDO DESDE GUI SCREEN EVENT");
						BetterInventoriesPacketHandler.INSTANCE.sendToServer(new SortContainerPacket(container.getInventory()));
					}
				}
			}
		}
	}
	
	private boolean keyEvaluate(final KeyBinding kb, final GuiScreenEvent.KeyboardKeyPressedEvent.Pre evt) {
        return kb.matchesKey(evt.getKeyCode(), evt.getScanCode());
    }

}