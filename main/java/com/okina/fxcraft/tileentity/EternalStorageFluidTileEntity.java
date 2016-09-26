package com.okina.fxcraft.tileentity;

import com.google.common.collect.Lists;
import com.okina.fxcraft.client.IHUDBlock;
import com.okina.fxcraft.utils.ColoredString;
import com.okina.fxcraft.utils.InfinitFluidTank;
import com.okina.fxcraft.utils.InfinitInteger;
import com.okina.fxcraft.utils.RenderingHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class EternalStorageFluidTileEntity extends TileEntity implements IFluidHandler, IHUDBlock {

	private InfinitFluidTank tank = new InfinitFluidTank();

	public EternalStorageFluidTileEntity() {
		tank.fluid = new FluidStack(FluidRegistry.WATER, 1000);
		tank.amount = new InfinitInteger(1123115);
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		return tank.fluid == null ? true : tank.fluid.getFluid() == fluid;
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return tank.fluid == null ? false : tank.fluid.getFluid() == fluid;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		if(resource != null && canDrain(from, resource.getFluid())){
			return tank.drain(resource.amount, doDrain);
		}else{
			return null;
		}
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return new FluidTankInfo[] { tank.getInfo() };
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
		tank.readFromNBT(compound);
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		tank.writeToNBT(compound);
	}

	@Override
	public void renderHUD(Minecraft mc, double renderTicks, MovingObjectPosition mop) {
		if(tank.getFluidAmount() != 0){
			RenderingHelper.renderHUDCenter(mc, Lists.newArrayList(new ColoredString(tank.fluid.getLocalizedName(), 0xffffff), new ColoredString("0x" + tank.amount.getHexString() + " mb", 0xffffff)));
		}else{
			RenderingHelper.renderHUDCenter(mc, Lists.newArrayList(new ColoredString("No Fluid Stored", 0xffffff)));
		}
	}

	@Override
	public boolean comparePastRenderObj(Object object, MovingObjectPosition past, MovingObjectPosition current) {
		return this == object;
	}

}
