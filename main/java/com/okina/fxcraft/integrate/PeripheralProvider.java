package com.okina.fxcraft.integrate;

import com.okina.fxcraft.tileentity.FXDealerTileEntity;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class PeripheralProvider implements IPeripheralProvider {

	@Override
	public IPeripheral getPeripheral(World world, BlockPos pos, EnumFacing side) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof FXDealerTileEntity){
			return (IPeripheral) tile;
		}
		return null;
	}

}
