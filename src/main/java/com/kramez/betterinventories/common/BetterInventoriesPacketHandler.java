package com.kramez.betterinventories.common;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class BetterInventoriesPacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	private static int ID = 0;

	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
	    new ResourceLocation("betterinventories", "main"),
	    () -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);
	
	public static void registerMessages() {
		INSTANCE.registerMessage(ID++, SortInventoryPacket.class, SortInventoryPacket::encode, SortInventoryPacket::new, SortInventoryPacket::handle);
	}
}
