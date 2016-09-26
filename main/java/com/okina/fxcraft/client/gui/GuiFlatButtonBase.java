package com.okina.fxcraft.client.gui;

import org.lwjgl.opengl.GL11;

import com.okina.fxcraft.main.FXCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiFlatButtonBase extends GuiButton {

	private final static ResourceLocation TEXTURE = new ResourceLocation(FXCraft.MODID + ":textures/gui/container/flat_button.png");

	public GuiFlatButtonBase(int buttonID, int startX, int startY, int sizeX, int sizeY, String display) {
		super(buttonID, startX, startY, sizeX, sizeY, display);
	}

	protected void drawButtonFromState(Minecraft minecraft, int state, boolean pushed, float[] textureColor, int strColor) {
		FontRenderer fontrenderer = minecraft.fontRendererObj;

		GlStateManager.pushAttrib();
		minecraft.getTextureManager().bindTexture(TEXTURE);
		GlStateManager.enableBlend();
		GL11.glEnable(GL11.GL_BLEND);
		GlStateManager.enableTexture2D();
		GlStateManager.disableCull();
		GlStateManager.enableDepth();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.blendFunc(770, 771);
		GlStateManager.color(textureColor[0], textureColor[1], textureColor[2], 0.5F);

		int offsetX = 0;
		int offsetY = 0;
		boolean flag = true;
		switch (state) {
		case 1:
			offsetX = 128;
			offsetY = 0;
			flag = false;
			break;
		case 2:
			offsetX = 0;
			offsetY = 128;
			flag = false;
			break;
		case 3:
			offsetX = 128;
			offsetY = 128;
			break;
		}
		if(pushed){
			if(flag){
				drawTexturedModalRect(xPosition + 1, yPosition + 1, offsetX, offsetY, width - 3, height - 3);
				drawTexturedModalRect(xPosition + 1, yPosition + 1, offsetX + 128 - width + 2, offsetY + 128 - height + 2, width - 2, height - 2);
			}else{
				drawTexturedModalRect(xPosition + 1, yPosition + 1, offsetX, offsetY, width - 2, height - 2);
			}
		}else{
			if(flag){
				drawTexturedModalRect(xPosition, yPosition, offsetX, offsetY, width - 1, height - 1);
				drawTexturedModalRect(xPosition, yPosition, offsetX + 128 - width, offsetY + 128 - height, width, height);
			}else{
				drawTexturedModalRect(xPosition, yPosition, offsetX, offsetY, width, height);
			}
		}

		fontrenderer.drawString(displayString, xPosition + width / 2 - fontrenderer.getStringWidth(displayString) / 2, yPosition + (height - 8) / 2, strColor, false);

		GlStateManager.popAttrib();
	}

	@Override
	public void playPressSound(SoundHandler soundHandler) {}

}
