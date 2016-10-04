package com.okina.fxcraft.client.gui.account_manager;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.okina.fxcraft.account.Reward;
import com.okina.fxcraft.client.gui.GuiIconLabel;
import com.okina.fxcraft.utils.RenderingHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;

public class GuiRewardLabel extends GuiIconLabel {

	public Reward reward;
	public boolean dropShadow;

	public GuiRewardLabel(int id, int x, int y, Reward reward, boolean dropShadow, RenderItem renderItem) {
		super(x, y, reward.getItem(), renderItem);
		this.id = id;
		this.reward = reward;
		this.dropShadow = dropShadow;
		this.enabled = true;
	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if(visible && dropShadow){
			FontRenderer fontrenderer = minecraft.fontRendererObj;
			hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;

			if(hovered){
				GlStateManager.pushAttrib();
				GlStateManager.enableBlend();
				GL11.glEnable(GL11.GL_BLEND);
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);
				GlStateManager.color(0.0F, 0.0F, 0.0F, 0.5F);
				RenderingHelper.drawRect(xPosition, yPosition, width, height);
				GlStateManager.popAttrib();
			}

			mouseDragged(minecraft, mouseX, mouseY);
		}
		super.drawButton(minecraft, mouseX, mouseY);
	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
		if(enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height){
			return true;
		}
		return false;
	}

	@Override
	public List<String> getTipList(int mouseX, int mouseY, boolean shift, boolean ctrl) {
		List<String> toolTip = Lists.newArrayList("§b§o" + reward.getDisplayName(), "-" + reward.getConditionMessage() + "-");
		toolTip.addAll(super.getTipList(mouseX, mouseY, shift, ctrl));
		return toolTip;
	}

}
