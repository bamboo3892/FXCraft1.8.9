package com.okina.fxcraft.tileentity;

import com.google.common.collect.Lists;
import com.okina.fxcraft.client.IHUDBlock;
import com.okina.fxcraft.utils.ColoredString;
import com.okina.fxcraft.utils.InfinitEnergyStorage;
import com.okina.fxcraft.utils.RenderingHelper;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

public class EternalStorageEnergyTileEntity extends TileEntity implements IEnergyProvider, IEnergyReceiver, IEnergyStorage, IHUDBlock {

	public InfinitEnergyStorage storage = new InfinitEnergyStorage();

	public EternalStorageEnergyTileEntity() {
		//		storage.energy = new InfinitInteger(Integer.MAX_VALUE);
	}

	@Override
	public boolean canConnectEnergy(EnumFacing facing) {
		return true;
	}

	@Override
	public int getEnergyStored(EnumFacing facing) {
		return getEnergyStored();
	}

	@Override
	public int getEnergyStored() {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing facing) {
		return getMaxEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return storage.getMaxEnergyStored();
	}

	@Override
	public int receiveEnergy(EnumFacing facing, int maxReceive, boolean simulate) {
		return receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		markDirty();
		return storage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(EnumFacing facing, int maxExtract, boolean simulate) {
		return extractEnergy(maxExtract, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		markDirty();
		return storage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		worldObj.markBlockForUpdate(pos);
	}

	@Override
	public final Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return new S35PacketUpdateTileEntity(pos, 1, nbtTagCompound);
	}

	@Override
	public final void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		NBTTagCompound nbtTagCompound = pkt.getNbtCompound();
		readFromNBT(nbtTagCompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		storage.readFromNBT(compound);
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		storage.writeToNBT(compound);
	}

	@Override
	public void renderHUD(Minecraft mc, double renderTicks, MovingObjectPosition mop) {
		RenderingHelper.renderHUDCenter(mc, Lists.newArrayList(new ColoredString("0x" + storage.energy.getHexString() + " RF", 0xffffff)));
	}

	@Override
	public boolean comparePastRenderObj(Object object, MovingObjectPosition past, MovingObjectPosition current) {
		return this == object;
	}

}
