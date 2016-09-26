package com.okina.fxcraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemMetaBlock extends ItemBlock {

	public ItemMetaBlock(Block block) {
		super(block);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

}
