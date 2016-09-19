package com.okina.fxcraft.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.okina.fxcraft.main.FXCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class GuiFlatToggleButton extends GuiButton {

	private final static ResourceLocation TEXTURE = new ResourceLocation(FXCraft.MODID + ":textures/gui/container/flat_button.png");

	protected boolean isClicked = false;
	private float[] selectedColor;
	public boolean selected = false;

	public GuiFlatToggleButton(int buttonID, int startX, int startY, int sizeX, int sizeY, String display, float[] selectedColor) {
		super(buttonID, startX, startY, sizeX, sizeY, display);
		this.selectedColor = selectedColor;
	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if(visible){
			GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
			FontRenderer fontrenderer = minecraft.fontRendererObj;
			minecraft.getTextureManager().bindTexture(TEXTURE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			int k = getHoverState(hovered);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			if(selected){
				GL11.glColor4f(selectedColor[0], selectedColor[1], selectedColor[2], 0.5F);
			}else{
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
			}
			int offsetX = 0;
			int offsetY = 0;
			if(k == 0){//disabled
				offsetX = 128;
				offsetY = 128;
				drawTexturedModalRect(xPosition, yPosition, offsetX, offsetY, width - 1, height - 1);
				drawTexturedModalRect(xPosition, yPosition, offsetX + 128 - width, offsetY + 128 - height, width, height);
			}else if(isClicked){
				drawTexturedModalRect(xPosition + 1, yPosition + 1, 0, 128, width - 2, height - 2);
				drawTexturedModalRect(xPosition + 1, yPosition + 1, 0 + 128 - width, 256 - height, width - 2, height - 2);
				//				this.drawTexturedModalRect(this.xPosition + 1, this.yPosition + 1, 0 + 128 - this.width, 128, this.width - 2, this.height - 2);
				//				this.drawTexturedModalRect(this.xPosition + 1, this.yPosition + 1, 0, 256 - this.height, this.width - 2, this.height - 2);
			}else{
				if(k == 1){//enabled, not hovering
					offsetX = 0;
					offsetY = 0;
					drawTexturedModalRect(xPosition, yPosition, offsetX, offsetY, width - 1, height - 1);
					drawTexturedModalRect(xPosition, yPosition, offsetX + 128 - width, offsetY + 128 - height, width, height);
				}else{//enabled, hovering
					offsetX = 128;
					offsetY = 0;
					drawTexturedModalRect(xPosition, yPosition, offsetX, offsetY, width, height);
					drawTexturedModalRect(xPosition, yPosition, offsetX + 128 - width, offsetY + 128 - height, width, height);
				}
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			mouseDragged(minecraft, mouseX, mouseY);

			int color;
			if(selected){
				if(isClicked || k == 2){
					color = new Color(selectedColor[0], selectedColor[1], selectedColor[2]).darker().darker().getRGB();
				}else{
					color = new Color(selectedColor[0], selectedColor[1], selectedColor[2]).getRGB();
				}
			}else{
				if(isClicked || k == 2){
					color = 0x808080;
				}else{
					color = 0xFFFFFF;
				}
			}
			fontrenderer.drawString(displayString, xPosition + width / 2 - fontrenderer.getStringWidth(displayString) / 2, yPosition + (height - 8) / 2, color, false);
			GL11.glPopAttrib();
		}

	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
		if(enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height){
			isClicked = true;
			selected = !selected;
			return true;
		}
		return false;
	}

	@Override
	protected void mouseDragged(Minecraft minecraft, int mouseX, int mouseY) {
		if(!(mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height)){
			isClicked = false;
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		isClicked = false;
	}

	@Override
	public void playPressSound(SoundHandler p_146113_1_) {}

}
