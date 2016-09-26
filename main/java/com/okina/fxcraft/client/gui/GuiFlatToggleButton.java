package com.okina.fxcraft.client.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;

public class GuiFlatToggleButton extends GuiFlatButtonBase {

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
			hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			int k = getHoverState(hovered);

			float[] c;
			if(selected){
				c = selectedColor;
			}else{
				c = new float[] { 1.0F, 1.0F, 1.0F };
			}
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
			if(k == 0){//disabled
				drawButtonFromState(minecraft, 3, false, c, color);
			}else if(isClicked){
				drawButtonFromState(minecraft, 2, true, c, color);
			}else{
				if(k == 1){//enabled, not hovering
					drawButtonFromState(minecraft, 0, false, c, color);
				}else{//enabled, hovering
					drawButtonFromState(minecraft, 1, false, c, color);
				}
			}

			mouseDragged(minecraft, mouseX, mouseY);
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

}
