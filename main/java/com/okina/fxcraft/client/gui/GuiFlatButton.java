package com.okina.fxcraft.client.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;

public class GuiFlatButton extends GuiFlatButtonBase {

	private float[] selectedColor;
	private boolean isClicked = false;

	public GuiFlatButton(int buttonID, int startX, int startY, int sizeX, int sizeY, String display, float[] selectedColor) {
		super(buttonID, startX, startY, sizeX, sizeY, display);
		this.selectedColor = selectedColor;
	}

	//	@Override
	//	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
	//		if(visible){
	//			hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
	//			int k = getHoverState(hovered);
	//			FontRenderer fontrenderer = minecraft.fontRendererObj;
	//
	//			GlStateManager.pushAttrib();
	//			minecraft.getTextureManager().bindTexture(TEXTURE);
	//			GL11.glEnable(GL11.GL_BLEND);
	//			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	//			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	//			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
	//
	//			int offsetX = 0;
	//			int offsetY = 0;
	//			if(k == 0){//disabled
	//				offsetX = 128;
	//				offsetY = 128;
	//				drawTexturedModalRect(xPosition, yPosition, offsetX, offsetY, width - 1, height - 1);
	//				drawTexturedModalRect(xPosition, yPosition, offsetX + 128 - width, offsetY + 128 - height, width, height);
	//			}else if(isClicked){
	//				GL11.glColor4f(selectedColor[0], selectedColor[1], selectedColor[2], 0.5F);
	//				drawTexturedModalRect(xPosition + 1, yPosition + 1, 0, 128, width - 2, height - 2);
	//				drawTexturedModalRect(xPosition + 1, yPosition + 1, 0 + 128 - width, 256 - height, width - 2, height - 2);
	//				//				this.drawTexturedModalRect(this.xPosition + 1, this.yPosition + 1, 0 + 128 - this.width, 128, this.width - 2, this.height - 2);
	//				//				this.drawTexturedModalRect(this.xPosition + 1, this.yPosition + 1, 0, 256 - this.height, this.width - 2, this.height - 2);
	//			}else{
	//				if(k == 1){//enabled, not hovering
	//					offsetX = 0;
	//					offsetY = 0;
	//					drawTexturedModalRect(xPosition, yPosition, offsetX, offsetY, width - 1, height - 1);
	//					drawTexturedModalRect(xPosition, yPosition, offsetX + 128 - width, offsetY + 128 - height, width, height);
	//				}else{//enabled, hovering
	//					GL11.glColor4f(selectedColor[0], selectedColor[1], selectedColor[2], 0.5F);
	//					offsetX = 128;
	//					offsetY = 0;
	//					drawTexturedModalRect(xPosition, yPosition, offsetX, offsetY, width, height);
	//					drawTexturedModalRect(xPosition, yPosition, offsetX + 128 - width, offsetY + 128 - height, width, height);
	//				}
	//			}
	//			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	//
	//			mouseDragged(minecraft, mouseX, mouseY);
	//
	//			int color;
	//			if(!enabled){
	//				color = 0xc0c0c0;
	//			}else if(isClicked || k == 2){
	//				color = 0x808080;
	//				color = new Color(selectedColor[0], selectedColor[1], selectedColor[2]).darker().darker().getRGB();
	//			}else{
	//				color = 0xFFFFFF;
	//			}
	//			fontrenderer.drawString(displayString, xPosition + width / 2 - fontrenderer.getStringWidth(displayString) / 2, yPosition + (height - 8) / 2, color, false);
	//
	//			GlStateManager.popAttrib();
	//		}
	//	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if(visible){
			hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			int k = getHoverState(hovered);

			if(k == 0){//disabled
				drawButtonFromState(minecraft, 3, false, new float[] { 1.0F, 1.0F, 1.0F }, 0xc0c0c0);
			}else if(isClicked){
				drawButtonFromState(minecraft, 2, true, selectedColor, new Color(selectedColor[0], selectedColor[1], selectedColor[2]).darker().darker().getRGB());
			}else{
				if(k == 1){//enabled, not hovering
					drawButtonFromState(minecraft, 0, false, new float[] { 1.0F, 1.0F, 1.0F }, 0xFFFFFF);
				}else{//enabled, hovering
					drawButtonFromState(minecraft, 1, false, selectedColor, new Color(selectedColor[0], selectedColor[1], selectedColor[2]).darker().darker().getRGB());
				}
			}

			mouseDragged(minecraft, mouseX, mouseY);
		}
	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
		if(enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height){
			isClicked = true;
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

}
