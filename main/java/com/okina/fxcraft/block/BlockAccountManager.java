package com.okina.fxcraft.block;

import com.okina.fxcraft.main.FXCraft;
import com.okina.fxcraft.tileentity.AccountManegerTileEntity;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockAccountManager extends BlockContainer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockAccountManager() {
		super(Material.iron);
		setUnlocalizedName("fxcraft_account_manager");
		setCreativeTab(FXCraft.FXCraftCreativeTab);
		setLightOpacity(0);
		setHardness(1.5F);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase livingBase, ItemStack stack) {
		int l = MathHelper.floor_double(livingBase.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		if(l == 0){
			world.setBlockState(pos, state.withProperty(FACING, EnumFacing.NORTH), 2);
		}else if(l == 1){
			world.setBlockState(pos, state.withProperty(FACING, EnumFacing.EAST), 2);
		}else if(l == 2){
			world.setBlockState(pos, state.withProperty(FACING, EnumFacing.SOUTH), 2);
		}else if(l == 3){
			world.setBlockState(pos, state.withProperty(FACING, EnumFacing.WEST), 2);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) player.openGui(FXCraft.instance, FXCraft.BLOCK_GUI_ID_0, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	public int getRenderType() {
		return 3;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if(state != null && state.getValue(FACING) == null){
			throw new IllegalArgumentException("Don\'t know how to convert " + state + " back into data...");
		}else{
			return state.getValue(FACING).getIndex();
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new AccountManegerTileEntity();
	}

}
