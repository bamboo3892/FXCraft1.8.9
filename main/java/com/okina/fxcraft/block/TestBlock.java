package com.okina.fxcraft.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TestBlock extends Block {

	public static final PropertyInteger META = PropertyInteger.create("meta", 0, 1);

	public TestBlock() {
		super(Material.iron);
		setCreativeTab(CreativeTabs.tabBlock);
		setUnlocalizedName("test_block");
		setDefaultState(blockState.getBaseState().withProperty(META, 0));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(META, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(META);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, META);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		super.getSubBlocks(itemIn, tab, list);
		list.add(new ItemStack(itemIn, 1, 1));
	}

}
