package com.okina.fxcraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockEternalStorage extends ItemBlock {

	public ItemBlockEternalStorage(Block block) {
		super(block);
		setMaxStackSize(1);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

}
