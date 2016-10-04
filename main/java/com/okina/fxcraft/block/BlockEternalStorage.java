package com.okina.fxcraft.block;

import java.util.List;

import com.google.common.collect.Lists;
import com.okina.fxcraft.client.IToolTipUser;
import com.okina.fxcraft.main.FXCraft;
import com.okina.fxcraft.tileentity.EternalStorageEnergyTileEntity;
import com.okina.fxcraft.tileentity.EternalStorageFluidTileEntity;
import com.okina.fxcraft.tileentity.EternalStorageItemTileEntity;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEternalStorage extends BlockContainer implements IToolTipUser {

	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 2);

	public BlockEternalStorage() {
		super(Material.iron);
		setCreativeTab(FXCraft.FXCraftCreativeTab);
		setUnlocalizedName("eternal_storage");
		setLightOpacity(0);
		setHardness(1.5F);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, 0));
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		TileEntity tile = worldIn.getTileEntity(pos);
		return getDefaultState().withProperty(TYPE, meta == 0 ? 0 : meta == 1 ? 1 : 2);
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		if(playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().canHarvestBlock(this)){
			return;
		}

		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof EternalStorageItemTileEntity){
			EternalStorageItemTileEntity storage = (EternalStorageItemTileEntity) tile;
			ItemStack item = storage.getStackInSlot(0);
			if(item != null){
				EnumFacing face = playerIn.getHorizontalFacing().getOpposite();
				if(!worldIn.isRemote){
					if(worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + face.getFrontOffsetX() + 0.5, pos.getY() + face.getFrontOffsetY() + 0.5, pos.getZ() + face.getFrontOffsetZ() + 0.5, item))){
						storage.removeStackFromSlot(0);
						storage.markDirty();
					}
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof EternalStorageItemTileEntity){
			EternalStorageItemTileEntity storage = (EternalStorageItemTileEntity) tile;
			if(System.currentTimeMillis() - storage.lastClickedTime < 200){//store all item player has
				ItemStack stored = storage.item;
				if(stored != null){
					int count = playerIn.inventory.clearMatchingItems(stored.getItem(), stored.getMetadata(), -1, stored.getTagCompound());
					storage.itemCount = storage.itemCount.plus(count);
					storage.markDirty();
				}
				storage.lastClickedTime = 0;
			}else{
				if(storage.getStackInSlot(63) == null && storage.isItemValidForSlot(63, playerIn.getCurrentEquippedItem())){
					storage.setInventorySlotContents(63, playerIn.getCurrentEquippedItem());
					storage.markDirty();
					playerIn.setCurrentItemOrArmor(0, null);
				}
				storage.lastClickedTime = System.currentTimeMillis();
			}
		}
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, TYPE);
	}

	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
		ItemStack item = new ItemStack(this, 1, state.getValue(TYPE));
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound blockTag = new NBTTagCompound();
		te.writeToNBT(blockTag);
		tag.setTag("BlockEntityTag", blockTag);
		item.setTagCompound(tag);
		spawnAsEntity(worldIn, pos, item);
		super.harvestBlock(worldIn, player, pos, state, te);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return Lists.newArrayList();
	}

	public int getRenderType() {
		return 3;
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		super.getSubBlocks(itemIn, tab, list);
		//		list.add(new ItemStack(itemIn, 1, 1));
		//		list.add(new ItemStack(itemIn, 1, 2));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return meta == 0 ? new EternalStorageItemTileEntity() : meta == 1 ? new EternalStorageEnergyTileEntity() : new EternalStorageFluidTileEntity();
	}

	@Override
	public void addToolTip(List<String> toolTip, ItemStack itemStack, EntityPlayer player, boolean shiftPressed, boolean advancedToolTip) {
		int damage = itemStack.getItemDamage();
		if(damage == 0){
			toolTip.add("For Item");
		}else if(damage == 1){
			toolTip.add("For Energy");
		}else if(damage == 2){
			toolTip.add("For Fuild");
		}
	}

	@Override
	public int getNeutralLines() {
		return 0;
	}

	@Override
	public int getShiftLines() {
		return 0;
	}

}
