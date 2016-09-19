package com.okina.fxcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IHUDArmor {

	@SideOnly(Side.CLIENT)
	public void renderHUD(Minecraft mc, double renderTicks, ItemStack itemStack);

	@SideOnly(Side.CLIENT)
	public boolean comparePastRenderObj(ItemStack object);

}
