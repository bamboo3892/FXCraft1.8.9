package com.okina.fxcraft.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class InfinitFluidTank implements IFluidTank {

	public FluidStack fluid;
	public InfinitInteger amount = InfinitInteger.ZERO;

	public InfinitFluidTank() {
		fluid = null;
	}

	public InfinitFluidTank(FluidStack fluid) {
		this.fluid = fluid;
		if(fluid != null){
			this.amount = new InfinitInteger(fluid.amount);
		}else{
			this.amount = InfinitInteger.ZERO;
		}
	}

	public void setFluid(FluidStack fluid) {
		this.fluid = fluid;
		if(fluid != null){
			this.amount = new InfinitInteger(fluid.amount);
		}else{
			this.amount = InfinitInteger.ZERO;
		}
	}

	@Override
	public FluidStack getFluid() {
		int amount = getFluidAmount();
		if(amount != 0){
			FluidStack stack = fluid.copy();
			stack.amount = amount;
			return stack;
		}
		return null;
	}

	@Override
	public int getFluidAmount() {
		if(fluid == null){
			return 0;
		}
		return Math.min((int) (Integer.MAX_VALUE / 2.0), (int) amount.getLongValue());
	}

	@Override
	public int getCapacity() {
		return Integer.MAX_VALUE;
	}

	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if(resource == null){
			return 0;
		}
		if(!doFill){
			if(fluid == null){
				return resource.amount;
			}else{
				if(!fluid.isFluidEqual(resource)){
					return 0;
				}
				return resource.amount;
			}
		}else{
			if(fluid == null){
				fluid = resource.copy();
				amount = new InfinitInteger(resource.amount);
				return (int) amount.getLongValue();
			}else{
				if(!fluid.isFluidEqual(resource)){
					return 0;
				}else{
					amount = amount.plus(resource.amount);
					return resource.amount;
				}
			}
		}
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if(fluid == null){
			return null;
		}
		int drained = Math.min(maxDrain, (int) amount.getLongValue());
		FluidStack stack = new FluidStack(fluid, drained);
		if(doDrain){
			amount = amount.plus(-drained);
			if(amount.getLongValue() <= 0){
				fluid = null;
				amount = InfinitInteger.ZERO;
			}
		}
		return stack;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		if(!nbt.hasKey("Empty")){
			fluid = FluidStack.loadFluidStackFromNBT(nbt);
			NBTTagCompound amountTag = nbt.getCompoundTag("inf_amount");
			String value = amountTag.getString("value");
			boolean negative = amountTag.getBoolean("negative");
			amount = new InfinitInteger(value, negative);
		}else{
			setFluid(null);
		}
	}

	public void writeToNBT(NBTTagCompound nbt) {
		if(fluid != null){
			fluid.writeToNBT(nbt);
			NBTTagCompound amountTag = new NBTTagCompound();
			amountTag.setString("value", amount.value);
			amountTag.setBoolean("negative", amount.negative);
			nbt.setTag("inf_amount", amountTag);
		}else{
			nbt.setString("Empty", "");
		}
	}

}
