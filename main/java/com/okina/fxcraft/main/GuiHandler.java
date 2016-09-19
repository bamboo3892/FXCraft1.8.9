package com.okina.fxcraft.main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return getGuiElement(ID, player, world, x, y, z, true);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return getGuiElement(ID, player, world, x, y, z, false);
	}

	public Object getGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z, boolean serverSide) {
		try{
			if(ID == FXCraft.ITEM_GUI_ID){
				if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IGuiItem){
					return ((IGuiItem) player.getCurrentEquippedItem().getItem()).getGuiElement(player, serverSide);
				}
			}else if(ID >= FXCraft.BLOCK_GUI_ID_0 && ID <= FXCraft.BLOCK_GUI_ID_5){
				if(world.getTileEntity(new BlockPos(x, y, z)) instanceof IGuiTile){
					return ((IGuiTile) world.getTileEntity(new BlockPos(x, y, z))).getGuiElement(player, ID - FXCraft.BLOCK_GUI_ID_0, serverSide);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public interface IGuiItem {

		public Object getGuiElement(EntityPlayer player, boolean serverSide);

	}

	public interface IGuiTile {

		public Object getGuiElement(EntityPlayer player, int side, boolean serverSide);

	}

}
