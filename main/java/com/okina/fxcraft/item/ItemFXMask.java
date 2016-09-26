package com.okina.fxcraft.item;

import java.util.List;

import com.okina.fxcraft.client.IToolTipUser;
import com.okina.fxcraft.client.model.ModelFXMask;
import com.okina.fxcraft.main.ClientProxy;
import com.okina.fxcraft.main.FXCraft;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFXMask extends ItemArmor implements IToolTipUser {

	public ItemFXMask(ArmorMaterial material, int renderId) {
		super(material, renderId, 0);
		setMaxStackSize(1);
		setCreativeTab(FXCraft.FXCraftCreativeTab);
		setUnlocalizedName("fxcraft_fx_mask");
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if(!world.isRemote){

		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemstack, int armorSlot) {
		ModelFXMask armorModel = ClientProxy.modelFXMask;

		armorModel.isSneak = entityLiving.isSneaking();
		armorModel.isRiding = entityLiving.isRiding();
		armorModel.isChild = entityLiving.isChild();

		armorModel.heldItemRight = 0;
		armorModel.aimedBow = false;

		EntityPlayer player = (EntityPlayer) entityLiving;
		ItemStack held_item = player.getEquipmentInSlot(0);
		if(held_item != null){
			armorModel.heldItemRight = 1;
			if(player.getItemInUseCount() > 0){
				EnumAction enumaction = held_item.getItemUseAction();
				if(enumaction == EnumAction.BOW){
					armorModel.aimedBow = true;
				}else if(enumaction == EnumAction.BLOCK){
					armorModel.heldItemRight = 3;
				}
			}
		}
		return armorModel;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String layer) {
		return FXCraft.MODID + ":textures/models/armor/fx_mask.png";
	}

	@Override
	public void addToolTip(List<String> toolTip, ItemStack itemStack, EntityPlayer player, boolean shiftPressed, boolean advancedToolTip) {
		toolTip.add("You Have Nothing To Lost!");
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
