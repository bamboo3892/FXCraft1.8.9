package com.okina.fxcraft.client.gui;

import java.util.Calendar;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.okina.fxcraft.main.FXCraft;
import com.okina.fxcraft.rate.FXRateGetHelper;
import com.okina.fxcraft.rate.RateData;
import com.okina.fxcraft.utils.RenderingHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiFXChart extends GuiButton {

	private final static ResourceLocation TEXTURE = new ResourceLocation(FXCraft.MODID + ":textures/gui/container/numbers.png");

	private List<RateData> dataList = Lists.newArrayList();
	private long updateMills = 0L;
	private boolean isClicked = false;
	private String displayPair = "USDJPY";
	private int displayTerm = FXRateGetHelper.TERM_REALTIME;
	private int displayDataMaxSize = 200;

	public GuiFXChart(int startX, int startY, int sizeX, int sizeY) {
		super(997, startX, startY, sizeX, sizeY, "");
	}

	public void setDisplayPair(String pair) {
		if(!displayPair.equals(pair)){
			displayPair = pair;
			updateMills = 0L;
		}
	}

	public String getDisplayPair() {
		return displayPair;
	}

	public void setDisplayTerm(int term) {
		if(displayTerm != term){
			displayTerm = term;
			updateMills = 0L;
		}
	}

	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
		if(visible){
			FontRenderer fontrenderer = minecraft.fontRendererObj;
			double effWidth = width - 30;
			double effHeight = height - 10;
			hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			int k = getHoverState(hovered);

			GlStateManager.pushAttrib();
			GlStateManager.enableBlend();
			GL11.glEnable(GL11.GL_BLEND);
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 0.8F);

			//			GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
			//			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			//			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			//			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);

			//back
			drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x60000000);

			//line mouse
			if(mouseX >= xPosition && mouseX < xPosition + width - 30 && mouseY >= yPosition && mouseY < yPosition + height - 10){
				drawRect(xPosition, mouseY, xPosition + width - 30, mouseY + 1, 0x30000000);
				drawRect(mouseX, yPosition, mouseX + 1, yPosition + height - 10, 0x30000000);
			}

			//bordar
			drawRect(xPosition, yPosition + height - 10, xPosition + width, yPosition + height, 0x60000000);
			drawRect(xPosition + width - 30, yPosition, xPosition + width, yPosition + height - 10, 0x60000000);

			//mesh
			double dx = effWidth / 8;
			for (int i = 1; i < 8; i++){
				drawRect((int) (xPosition + dx * i), yPosition, (int) (xPosition + dx * i + 1), (int) (yPosition + effHeight), 0x60000000);
			}

			//chart
			if(dataList == null || FXCraft.rateGetter.hasUpdate(updateMills)){
				List<RateData> list = FXCraft.rateGetter.getRateForChart(displayPair, displayTerm);
				dataList = list;
				updateMills = System.currentTimeMillis();
			}
			if(dataList != null && !dataList.isEmpty()){
				List<RateData> dataList = Lists.newLinkedList();
				for (int i = 0; i < displayDataMaxSize; i++){
					if(this.dataList.size() > i){
						dataList.add(this.dataList.get(i));
					}else{
						break;
					}
				}
				double maxRate = Integer.MIN_VALUE;
				double minRate = Integer.MAX_VALUE;
				for (RateData data : dataList){
					maxRate = Math.max(data.open, maxRate);
					minRate = Math.min(data.open, minRate);
				}
				double valueMargin = maxRate - minRate;
				Calendar latest = dataList.get(dataList.size() - 1).calendar;
				Calendar earliest = dataList.get(0).calendar;
				int dataSize = dataList.size();
				List<VertexCoord> coordList = Lists.newArrayList();
				for (int index = 0; index < dataSize; index++){
					RateData data = dataList.get(index);
					double x = xPosition + effWidth - ((double) index / dataSize) * effWidth;
					double y = yPosition + (maxRate - data.open) / valueMargin * effHeight;
					coordList.add(new VertexCoord(x, y, data));
				}
				drawLines(coordList, 0xFF7FFF00);

				RenderingHelper.drawMiniString(String.valueOf(maxRate), xPosition + width - 29, yPosition + 1, 0x60000000);
				RenderingHelper.drawMiniString(String.valueOf(minRate), xPosition + width - 29, yPosition + height - 18, 0x60000000);
				RenderingHelper.drawMiniString(String.valueOf(coordList.get(0).rate.open), xPosition + width - 29, (int) coordList.get(0).y - 3, 0x60000000);
				String time2 = FXRateGetHelper.getCalendarString(earliest, displayTerm);
				RenderingHelper.drawMiniString(time2, xPosition + width - 30 - time2.length() * 4, yPosition + height - 8, 0x60000000);
				RenderingHelper.drawMiniString(FXRateGetHelper.getCalendarString(latest, displayTerm), xPosition + 1, yPosition + height - 8, 0x60000000);

				//mouse coord
				if(mouseX >= xPosition && mouseX < xPosition + width - 30 && mouseY >= yPosition && mouseY < yPosition + height - 10){
					for (VertexCoord coord : coordList){
						if(mouseX > coord.x){
							drawRect((int) coord.x - 1, (int) coord.y - 1, (int) coord.x + 1, (int) coord.y + 1, 0xffd2691e);
							String str1 = String.valueOf(coord.rate.open);
							drawRect((int) coord.x + 3, (int) coord.y - 1, (int) coord.x + str1.length() * 4 + 5, (int) coord.y + 7, 0x60000000);
							RenderingHelper.drawMiniString(str1, (int) coord.x + 4, (int) coord.y, 0x60000000);
							String str2 = FXRateGetHelper.getCalendarString(coord.rate.calendar, displayTerm);
							drawRect((int) coord.x + 3, yPosition + height - 9, (int) coord.x + str2.length() * 4 + 4, yPosition + height, 0xa0000000);
							RenderingHelper.drawMiniString(str2, (int) coord.x + 4, yPosition + height - 8, 0x60000000);
							String str3 = String.format("%g", maxRate - (mouseY - yPosition) / effHeight * valueMargin);
							drawRect(xPosition + width - 29, mouseY - 1, xPosition + width - 29 + str3.length() * 4, mouseY + 7, 0xa0000000);
							RenderingHelper.drawMiniString(str3, xPosition + width - 29, mouseY, 0x60000000);
							break;
						}
					}
				}
			}else{
				fontrenderer.drawString("This chart is not loaded", xPosition + (int) (width / 2f) - 76, yPosition + (int) (height / 2f) - 8, 0xFFFFFF, false);
			}

			mouseDragged(minecraft, mouseX, mouseY);

			int color;
			if(isClicked || k == 2){
				color = 0x808080;
			}else{
				color = 0xFFFFFF;
			}
			fontrenderer.drawString(displayString, xPosition + width / 2 - fontrenderer.getStringWidth(displayString) / 2, yPosition + (height - 8) / 2, color, false);

			GlStateManager.popAttrib();
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

	public void handleMouseInput() {
		int i = Mouse.getEventDWheel();
		displayDataMaxSize -= i / 10f;
		if(displayDataMaxSize < 30){
			displayDataMaxSize = 30;
		}else if(displayDataMaxSize > 200){
			displayDataMaxSize = 200;
		}
	}

	public static void drawLines(List<VertexCoord> vertexList, int color) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		float f3 = (color >> 24 & 255) / 255.0F;
		float f = (color >> 16 & 255) / 255.0F;
		float f1 = (color >> 8 & 255) / 255.0F;
		float f2 = (color & 255) / 255.0F;
		GlStateManager.pushAttrib();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(f, f1, f2, f3);
		//		GL11.glEnable(GL11.GL_BLEND);
		//		GL11.glDisable(GL11.GL_TEXTURE_2D);
		//		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		//		GL11.glColor4f(f, f1, f2, f3);
		worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
		for (VertexCoord coords : vertexList){
			worldRenderer.pos(coords.x, coords.y, 0.0).endVertex();
		}
		tessellator.draw();
		GlStateManager.popAttrib();
		//		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void playPressSound(SoundHandler soundHandler) {}

	public class VertexCoord {
		final double x;
		final double y;
		final RateData rate;

		public VertexCoord(double x, double y, RateData rate) {
			this.x = x;
			this.y = y;
			this.rate = rate;
		}
	}

}
