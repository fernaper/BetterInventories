package com.kramez.betterinventories.client;
import com.kramez.betterinventories.common.BetterInventoriesPacketHandler;
import com.kramez.betterinventories.common.SortInventoryPacket;
import com.kramez.betterinventories.utilities.CustomSounds;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyInputHandler {

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onKeyInput(KeyInputEvent event) {
        if (Keybinds.sort.isPressed()) {
        	BetterInventoriesPacketHandler.INSTANCE.sendToServer(new SortInventoryPacket());

        	Minecraft mc = Minecraft.getInstance();
        	mc.player.playSound(CustomSounds.pressKey, 1, 1);
        }
    }
}