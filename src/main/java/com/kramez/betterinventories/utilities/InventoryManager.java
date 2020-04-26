package com.kramez.betterinventories.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;

public class InventoryManager {

	public static void sortInventory(PlayerInventory inventory) {
		// From ID -> Name -> ItemStack (with Item and amount)
		// Because of Anvil...
		HashMap<Integer, HashMap<String, ItemStack>> inventoryData = new HashMap<Integer, HashMap<String, ItemStack>>();
		List<ItemStack> unstackableItems = new ArrayList<ItemStack>();
		
		HashMap<String, List<ItemStack>> writtenBooks = new HashMap<String, List<ItemStack>>();

		for (int i = 9; i < inventory.mainInventory.size(); i++) {
    		ItemStack stack = inventory.mainInventory.get(i);
    		
    		// Skip empty spaces
    		if (stack.getCount() == 0)
    			continue;
    		
    		// Skip unstackable items
    		if (stack.getMaxStackSize() == 1) {
    			unstackableItems.add(stack);
    			continue;
    		}
    		
    		int stack_id = Item.getIdFromItem(stack.getItem());
    		String stackName = stack.getDisplayName().getString();
    		
    		//System.out.println(Integer.toString(stack_id) + " " + stackName + ": " + stack.getCount());
    		
    		// Check problems with written books
    		if (stack.getItem() instanceof WrittenBookItem) {
    			if (writtenBooks.containsKey(stackName)) {
    				int index = getIndexNewBook(writtenBooks.get(stackName), stack);

    				// If it is a new written book with the same name but different content
    				// A crazy case...
    				if (index == -1) {
    					writtenBooks.get(stackName).add(stack);
    				} else {
    					ItemStack previousStack = writtenBooks.get(stackName).get(index);
            			previousStack.setCount(previousStack.getCount() + stack.getCount());
    				}
    			} else {
    				List<ItemStack> listBooks = new ArrayList<ItemStack>();
    				listBooks.add(stack);

    				writtenBooks.put(stackName, listBooks);
    			}

    			continue;
    		}
    		
    		if (!inventoryData.containsKey(stack_id)) {
    			HashMap<String, ItemStack> nameToStack = new HashMap<String,ItemStack>();
    			nameToStack.put(stackName, stack);
    			inventoryData.put(stack_id, nameToStack);
    		} else {
    			// If it is a new name for an existing ID
    			if (!inventoryData.get(stack_id).containsKey(stackName)) {
    				inventoryData.get(stack_id).put(stackName, stack);
    			} else {
    				ItemStack previousStack = inventoryData.get(stack_id).get(stackName);
        			previousStack.setCount(previousStack.getCount() + stack.getCount());
    			}
    		}
    	}
    	
    	// Sort all stack IDs ascending
		List<Integer> stackIds = new ArrayList<>(inventoryData.keySet());
    	Collections.sort(stackIds);
    	
    	// Sort all written books
    	List<String> bookNames = new ArrayList<>(writtenBooks.keySet());
    	Collections.sort(bookNames);
    	
    	int slot = 9; // Ignore tool bar
    	for (int stackId : stackIds) {
    		
    		List<String> stackNames = new ArrayList<>(inventoryData.get(stackId).keySet());
    		Collections.sort(stackNames);
    		
    		for (String stackName : stackNames) {
	    		ItemStack stack = inventoryData.get(stackId).get(stackName);
	    		slot = placeItemStacks(inventory, slot, stack);
    		}
    	}
    	
    	// Place all written books
    	for (String bookName : bookNames) {
    		for (ItemStack stack : writtenBooks.get(bookName)) {
    			slot = placeItemStacks(inventory, slot, stack);
    		}
    	}
    	
    	// Place all unstackable items (like enchanted books, etc.)
    	for (ItemStack stack : unstackableItems) {
    		inventory.setInventorySlotContents(slot, stack);
    		slot++;
    	}

    	// Clean rest of inventory
    	for (int i = slot; i < inventory.mainInventory.size(); i++) {
    		inventory.removeStackFromSlot(i);
    	}
	}

	private static int getIndexNewBook(List<ItemStack> books, ItemStack newBook) {
		for (int i = 0; i < books.size(); i++) {
			if (ItemStack.areItemStackTagsEqual(books.get(i), newBook)) {
				return i;
			}
		}

		return -1;
	}
	
	private static int placeItemStacks(PlayerInventory inventory, int slot, ItemStack stack) {
		int numItems = stack.getCount();
		int maxStackSize = stack.getMaxStackSize();
		
		int numStacks = numItems / maxStackSize;
		
		// Aggregate all stacks of the same type
		for (int i = 0; i < numStacks; i++) {
			inventory.setInventorySlotContents(slot, createItemStack(stack, maxStackSize));
			slot++;
		}
		
		// Extra blocks of the same stack
		if (numItems % maxStackSize > 0) {
			inventory.setInventorySlotContents(slot, createItemStack(stack, numItems % maxStackSize));
			slot++;
		}
		
		return slot;
	}
	
	private static ItemStack createItemStack(ItemStack itemStack, int amount) {
		// Create a copy so we conserve complete information about the stack
		ItemStack myItemStack = itemStack.copy();
		myItemStack.setCount(amount);
		return myItemStack;
		//return new ItemStack(itemStack.getItem(), amount);
	}

}
