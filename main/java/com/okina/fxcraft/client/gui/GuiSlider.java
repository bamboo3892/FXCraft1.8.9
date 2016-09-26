
package com.okina.fxcraft.client.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiSlider extends GuiButton {

	private int value;
	private int dragValue;
	private int minValue;
	private int maxValue;
	private int interval;
	private boolean nowClicked = false;
	private int dragStartX = 0;

	public GuiSlider(int buttonID, int startX, int startY, int sizeX, int minValue, int maxValue, int initValue, int interval) {
		super(buttonID, startX, startY, sizeX, 12, "");
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.interval = interval;
		value = initValue;
	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if(visible){
			FontRenderer fontrenderer = minecraft.fontRendererObj;
			hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			int k = getHoverState(hovered);

			GlStateManager.pushAttrib();
			GlStateManager.enableBlend();
			GL11.glEnable(GL11.GL_BLEND);
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);

			GlStateManager.disableTexture2D();
			drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x73000000);

			if(k == 0){//disabled
			}else if(nowClicked){
				if(dragStartX <= xPosition + 10){
					drawRect(xPosition + 1, yPosition + 1, xPosition + 9, yPosition + height - 1, 0x33000000);
				}else if(dragStartX >= xPosition + width - 10){
					drawRectangle(xPosition + width - 9, yPosition + 1, xPosition + width - 1, yPosition + height - 1, 0x33000000);
				}else{
					drawRect(xPosition + 11, yPosition + 1, xPosition + width - 11, yPosition + height - 1, 0x33000000);
				}
			}else{
				if(k == 1){//enabled, not hovering
				}else{//enabled, hovering
					if(mouseX <= xPosition + 10){
						drawRect(xPosition, yPosition, xPosition + 10, yPosition + height, 0x33000000);
					}else if(mouseX >= xPosition + width - 10){
						drawRect(xPosition + width - 10, yPosition, xPosition + width, yPosition + height, 0x33000000);
					}else{
						drawRect(xPosition + 10, yPosition, xPosition + width - 10, yPosition + height, 0x33000000);
					}
				}
			}
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			mouseDragged(minecraft, mouseX, mouseY);

			int color;
			if(nowClicked || k == 2){
				color = 0x808080;
			}else{
				color = 0xFFFFFF;
			}
			color = 0xFFFFFF;
			String str = String.valueOf(value + dragValue);

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableTexture2D();
			fontrenderer.drawString(str, xPosition + width / 2 - fontrenderer.getStringWidth(str) / 2, yPosition + (height - 8) / 2, color, false);
			GlStateManager.popAttrib();
		}
	}

	@Override
	public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
		if(!nowClicked){
			if(enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height){
				nowClicked = true;
				dragStartX = mouseX;
				boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
				boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
				int change = ctrl ? interval * 30 : shift ? interval * 10 : interval;
				if(dragStartX <= xPosition + 10){
					value -= change;
					if(value < minValue){
						value = minValue;
					}
				}else if(dragStartX >= xPosition + width - 10){
					value += change;
					if(value > maxValue){
						value = maxValue;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected void mouseDragged(Minecraft minecraft, int mouseX, int mouseY) {
		if(nowClicked){
			if(dragStartX <= xPosition + 10 || dragStartX >= xPosition + width - 10){
				if(!(mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height)){
					nowClicked = false;
					dragStartX = 0;
				}
			}else{
				dragValue = ((mouseX - dragStartX) / 5) * interval;
				if(value + dragValue < minValue){
					dragValue = minValue - value;
				}else if(value + dragValue > maxValue){
					dragValue = maxValue - value;
				}
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		nowClicked = false;
		value += dragValue;
		dragValue = 0;
		dragStartX = 0;
	}

	public void setValue(int value) {
		this.value = value;
		dragValue = 0;
	}

	public int getValue() {
		return value + dragValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
		value = Math.max(value, minValue);
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		value = Math.min(value, maxValue);
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public void playPressSound(SoundHandler soundHandler) {}

	public static void drawRectangle(int left, int top, int right, int bottom, int color) {
		if(left < right){
			int i = left;
			left = right;
			right = i;
		}
		if(top < bottom){
			int j = top;
			top = bottom;
			bottom = j;
		}

		float alpha = (color >> 24 & 255) / 255.0F;
		float red = (color >> 16 & 255) / 255.0F;
		float green = (color >> 8 & 255) / 255.0F;
		float blue = (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.color(red, green, blue, alpha);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(left, bottom, 0.0D).endVertex();
		worldrenderer.pos(right, bottom, 0.0D).endVertex();
		worldrenderer.pos(right, top, 0.0D).endVertex();
		worldrenderer.pos(left, top, 0.0D).endVertex();
		tessellator.draw();
	}

}
