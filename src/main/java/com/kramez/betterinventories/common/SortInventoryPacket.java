package com.kramez.betterinventories.common;

import java.util.function.Supplier;

import com.kramez.betterinventories.utilities.InventoryManager;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;


public class SortInventoryPacket {
    //private final int data;

    public SortInventoryPacket(PacketBuffer buf) {
        //this.data = buf.readInt();
    }

    public SortInventoryPacket() {
        //this.data = data;
    }

    public void encode(PacketBuffer buf) {
        //buf.writeInt(data);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
    	InventoryManager.sortInventory(context.get().getSender().inventory);
    	context.get().setPacketHandled(true);
    }
}