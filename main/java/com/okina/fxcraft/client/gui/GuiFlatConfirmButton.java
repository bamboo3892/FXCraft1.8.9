package com.okina.fxcraft.client.gui;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;

public class GuiFlatConfirmButton extends GuiFlatButtonBase implements ITipComponent {

	private boolean isClicked = false;
	private float[] selectedColor;
	public boolean selected = false;
	private long selectedTime;
	private List<String> tips;
	private List<String> confirmTips;

	public GuiFlatConfirmButton(int buttonID, int startX, int startY, int sizeX, int sizeY, String display, float[] selectedColor, List<String> tips, List<String> confirmTips) {
		super(buttonID, startX, startY, sizeX, sizeY, display);
		this.selectedColor = selectedColor;
		this.tips = tips;
		this.confirmTips = confirmTips;
	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if(visible){
			if(selected && System.currentTimeMillis() - selectedTime > 5000){
				selected = false;
			}
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
			if(selected){
				selectedTime = System.currentTimeMillis();
			}
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
	public List<String> getTipList(int mouseX, int mouseY, boolean shift, boolean ctrl) {
		return selected ? confirmTips : tips;
	}

}
