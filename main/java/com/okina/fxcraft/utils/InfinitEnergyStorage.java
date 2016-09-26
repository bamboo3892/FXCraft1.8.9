package com.okina.fxcraft.utils;

import cofh.api.energy.IEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;

public class InfinitEnergyStorage implements IEnergyStorage {

	public InfinitInteger energy = InfinitInteger.ZERO;
	protected int maxReceive;
	protected int maxExtract;

	public InfinitEnergyStorage() {
		this(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	public InfinitEnergyStorage(int maxTransfer) {
		this(maxTransfer, maxTransfer);
	}

	public InfinitEnergyStorage(int maxReceive, int maxExtract) {
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public IEnergyStorage setMaxTransfer(int maxTransfer) {
		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
		return this;
	}

	public IEnergyStorage setMaxReceive(int maxReceive) {
		this.maxReceive = maxReceive;
		return this;
	}

	public IEnergyStorage setMaxExtract(int maxExtract) {
		this.maxExtract = maxExtract;
		return this;
	}

	public int getMaxReceive() {
		return this.maxReceive;
	}

	public int getMaxExtract() {
		return this.maxExtract;
	}

	public void setEnergyStored(InfinitInteger energy) {
		this.energy = energy;
		if(this.energy.negative){
			this.energy = InfinitInteger.ZERO;
		}
	}

	public int receiveEnergy(int maxReceive, boolean simulate) {
		int energyReceived = Math.min(this.maxReceive, maxReceive);
		if(!simulate){
			energy = energy.plus(energyReceived);
		}
		return energyReceived;
	}

	public int extractEnergy(int maxExtract, boolean simulate) {
		int energyExtracted = Math.min((int) this.energy.getLongValue(), Math.min(this.maxExtract, maxExtract));
		if(!simulate){
			energy = energy.plus(-energyExtracted);
		}
		return energyExtracted;
	}

	public int getEnergyStored() {
		return Math.min((int) energy.getLongValue(), Integer.MAX_VALUE >> 2);
	}

	public int getMaxEnergyStored() {
		return Integer.MAX_VALUE;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		String value = nbt.getString("value");
		boolean negative = nbt.getBoolean("negative");
		energy = new InfinitInteger(value, negative);
	}

	public void writeToNBT(NBTTagCompound nbt) {
		if(energy.getLongValue() < 0){
			energy = energy.ZERO;
		}
		nbt.setString("value", energy.value);
		nbt.setBoolean("negative", energy.negative);
	}

}
