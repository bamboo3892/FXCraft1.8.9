package com.okina.fxcraft.main;

import static com.okina.fxcraft.main.FXCraft.*;

import org.lwjgl.input.Keyboard;

import com.okina.fxcraft.account.AccountHandler;
import com.okina.fxcraft.account.AccountInfo;
import com.okina.fxcraft.client.IHUDArmor;
import com.okina.fxcraft.client.IHUDBlock;
import com.okina.fxcraft.client.IHUDItem;
import com.okina.fxcraft.client.IToolTipUser;
import com.okina.fxcraft.item.ItemCapitalistGuard;
import com.okina.fxcraft.main.CommonProxy.PopUpMessage;
import com.okina.fxcraft.utils.UtilMethods;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void toolTip(ItemTooltipEvent event) {
		Item item = event.itemStack.getItem();
		if(item instanceof IToolTipUser){
			((IToolTipUser) item).addToolTip(event.toolTip, event.itemStack, event.entityPlayer, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT), event.showAdvancedItemTooltips);
		}else if(item != null){
			Block block = Block.getBlockFromItem(item);
			if(block instanceof IToolTipUser){
				((IToolTipUser) block).addToolTip(event.toolTip, event.itemStack, event.entityPlayer, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT), event.showAdvancedItemTooltips);
			}
		}

		if(item != null){
			if(item instanceof IToolTipUser || (item instanceof ItemBlock && Block.getBlockFromItem(item) instanceof IToolTipUser)){
				IToolTipUser tooltip;
				if(item instanceof IToolTipUser){
					tooltip = (IToolTipUser) item;
				}else{
					tooltip = (IToolTipUser) Block.getBlockFromItem(item);
				}
				for (int i = 0; i < tooltip.getNeutralLines(); i++){
					event.toolTip.add(StatCollector.translateToLocal("tooltip." + item.getUnlocalizedName() + "." + i));
				}

				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
					for (int i = 0; i < tooltip.getShiftLines(); i++){
						event.toolTip.add(StatCollector.translateToLocal("tooltipshift." + item.getUnlocalizedName() + "." + i));
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingAttack(LivingAttackEvent event) {
		if(FMLCommonHandler.instance().getEffectiveSide().isServer()){
			if(event.isCancelable() && !event.isCanceled()){
				ItemStack armor = event.entityLiving.getEquipmentInSlot(3);
				if(armor != null && armor.getItem() == FXCraft.capitalist_guard){
					NBTTagCompound tag = armor.getTagCompound();
					if(tag != null){
						AccountInfo account = AccountHandler.instance.getAccountInfo(tag.getString("account"));
						if(account != null){
							tag.setDouble("balance", account.balance);
							if(AccountHandler.instance.decBalance(tag.getString("account"), event.ammount * ItemCapitalistGuard.HEAL_COST)){
								event.setCanceled(true);
							}
						}else{
							tag.setDouble("balance", 0);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onServerTick(TickEvent.ServerTickEvent event) {
		for (int i = 0; i < proxy.serverPacketList.size(); i++){
			packetDispatcher.sendToAll(proxy.serverPacketList.get(i));
		}
		proxy.serverPacketList.clear();
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		for (int i = 0; i < FXCraft.proxy.messageList.size(); i++){
			PopUpMessage msg = FXCraft.proxy.messageList.get(i);
			if(msg == null || msg.liveTime <= 0){
				FXCraft.proxy.messageList.remove(i);
				i--;
			}else{
				msg.liveTime--;
			}
		}
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {}

	private ItemStack[] pastRenderedArmor = new ItemStack[4];
	private double[] renderTicksArmor = new double[4];
	private ItemStack pastRenderedItem = null;
	private double renderTicksItem = 0;
	private IHUDBlock pastRenderedObject = null;
	private MovingObjectPosition pastMOP = null;
	private double renderTicksBlock = 0;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();

		if(!mc.isGamePaused() && mc.thePlayer != null){

			//hud
			if(mc.currentScreen == null){
				for (int i = 0; i < 4; i++){
					ItemStack armor = Minecraft.getMinecraft().thePlayer.getCurrentArmor(i);
					if(armor != null && armor.getItem() instanceof IHUDArmor){
						IHUDArmor hud = (IHUDArmor) armor.getItem();
						if(pastRenderedArmor[i] != null && hud.comparePastRenderObj(pastRenderedArmor[i])){
							double tick = mc.theWorld.getTotalWorldTime() % 72000 + event.renderTickTime;
							hud.renderHUD(mc, tick - renderTicksArmor[i], armor);
						}else{
							hud.renderHUD(mc, 0.0D, armor);
							pastRenderedArmor[i] = armor;
							renderTicksArmor[i] = mc.theWorld.getTotalWorldTime() % 72000 + event.renderTickTime;
						}
					}else{
						pastRenderedArmor[i] = null;
					}
				}
				ItemStack current = mc.thePlayer.getCurrentEquippedItem();
				if(current != null && current.getItem() instanceof IHUDItem){
					IHUDItem hud = (IHUDItem) current.getItem();
					if(pastRenderedItem != null && hud.comparePastRenderObj(pastRenderedItem)){
						double tick = mc.theWorld.getTotalWorldTime() % 72000 + event.renderTickTime;
						hud.renderHUD(mc, tick - renderTicksItem, current);
					}else{
						hud.renderHUD(mc, 0.0D, current);
						pastRenderedItem = current;
						renderTicksItem = mc.theWorld.getTotalWorldTime() % 72000 + event.renderTickTime;
					}
				}else{
					pastRenderedItem = null;
				}
				MovingObjectPosition mop = UtilMethods.getMovingObjectPositionFromPlayer(mc.theWorld, mc.thePlayer, true);
				if(mop != null && mop.sideHit != null && mop.typeOfHit == MovingObjectType.BLOCK){
					IHUDBlock renderObj = null;
					if(mc.theWorld.getTileEntity(mop.getBlockPos()) instanceof IHUDBlock){
						renderObj = (IHUDBlock) mc.theWorld.getTileEntity(mop.getBlockPos());
					}else if(mc.theWorld.getBlockState(mop.getBlockPos()).getBlock() instanceof IHUDBlock){
						renderObj = (IHUDBlock) mc.theWorld.getBlockState(mop.getBlockPos()).getBlock();
					}
					if(renderObj != null){
						if(renderObj.comparePastRenderObj(pastRenderedObject, pastMOP, mop)){
							double tick = mc.theWorld.getTotalWorldTime() % 72000 + event.renderTickTime;
							renderObj.renderHUD(mc, tick - renderTicksBlock, mop);
						}else{
							renderObj.renderHUD(mc, 0.0D, mop);
							pastRenderedObject = renderObj;
							pastMOP = mop;
							renderTicksBlock = mc.theWorld.getTotalWorldTime() % 72000 + event.renderTickTime;
						}
					}else{
						pastRenderedObject = null;
						pastMOP = null;
					}
				}
			}

			//popup
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer wordRenderer = Tessellator.getInstance().getWorldRenderer();
			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

			GlStateManager.pushAttrib();
			GlStateManager.enableBlend();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.depthMask(true);
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			//			GL11.glEnable(GL11.GL_BLEND);
			//			GL11.glDisable(GL11.GL_LIGHTING);
			//			GL11.glDisable(GL11.GL_CULL_FACE);
			//			GL11.glDepthMask(true);
			//			OpenGlHelper.glBlendFunc(770, 771, 1, 0);

			for (PopUpMessage msg : FXCraft.proxy.messageList){
				if(msg.liveTime >= 0){
					int offsetX = msg.liveTime <= 10 ? 10 - msg.liveTime : 0;
					float alpha = msg.liveTime <= 10 ? (msg.liveTime / 10f) + 0.1f : 1.0F;
					int size = fontRenderer.getStringWidth(msg.message);
					if(alpha > 1f){
						alpha = 1f;
					}
					GlStateManager.disableTexture2D();
					GlStateManager.color(0.0F, 0.0F, 0.0F, 0.5F);
					wordRenderer.begin(7, DefaultVertexFormats.POSITION);
					wordRenderer.pos(29 + offsetX, 72 + msg.index * 10, 0).endVertex();
					wordRenderer.pos(29 + offsetX, 72 + (msg.index + 1) * 10, 0).endVertex();
					wordRenderer.pos(31 + size + offsetX, 72 + (msg.index + 1) * 10, 0).endVertex();
					wordRenderer.pos(31 + size + offsetX, 72 + msg.index * 10, 0).endVertex();
					tessellator.draw();
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					GlStateManager.enableTexture2D();
					fontRenderer.drawString(msg.message, 30 + offsetX, 73 + msg.index * 10, 0x7cfc00, false);
				}
			}

			GlStateManager.enableLighting();
			GlStateManager.popAttrib();
		}
		pastRenderedObject = null;
		pastMOP = null;
	}

}
