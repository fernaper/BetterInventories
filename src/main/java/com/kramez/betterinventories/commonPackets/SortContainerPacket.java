package com.kramez.betterinventories.commonPackets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.kramez.betterinventories.utilities.InventoryManager;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SortContainerPacket {
    private final int inventorySize;
    private final List<ItemStack> inventory;

    public SortContainerPacket(PacketBuffer buf) {
    	this.inventorySize = buf.readInt();
    	ArrayList<ItemStack> inventory = new ArrayList<ItemStack>();
    	
    	for (int i = 0; i < this.inventorySize; i++)
    		inventory.add(buf.readItemStack());
    	
    	this.inventory = inventory;
    }

    public SortContainerPacket(List<ItemStack> inventory) {
    	this.inventorySize = inventory.size();
    	this.inventory = inventory;
    }

    public void encode(PacketBuffer buf) {
    	buf.writeInt(this.inventorySize);
    	
    	for (ItemStack stack : this.inventory)
    		buf.writeItemStack(stack);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
    	InventoryManager.sortInventory(context.get().getSender().openContainer);
    	context.get().setPacketHandled(true);
    }
}
