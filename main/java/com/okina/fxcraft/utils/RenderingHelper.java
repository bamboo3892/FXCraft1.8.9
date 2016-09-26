package com.okina.fxcraft.utils;

import java.util.List;

import org.lwjgl.util.Point;

import com.okina.fxcraft.main.FXCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public final class RenderingHelper {

	public final static ResourceLocation CHAR_TEXTURE = new ResourceLocation(FXCraft.MODID + ":textures/gui/container/numbers.png");

	public static void drawMiniString(String str, int x, int y, int color) {
		str = str.toUpperCase();
		GlStateManager.pushAttrib();
		Minecraft.getMinecraft().getTextureManager().bindTexture(CHAR_TEXTURE);
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.depthMask(true);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		//		GL11.glEnable(GL11.GL_BLEND);
		//		GL11.glDisable(GL11.GL_LIGHTING);
		//		GL11.glDisable(GL11.GL_CULL_FACE);
		//		GL11.glDepthMask(true);
		//		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		//		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		for (int i = 0; i < str.length(); i++){
			char c = str.charAt(i);
			switch (c) {
			case '0':
				drawTexturedModalRect(x + i * 4, y, 0, 0, 3, 6);
				break;
			case '1':
				drawTexturedModalRect(x + i * 4, y, 4, 0, 3, 6);
				break;
			case '2':
				drawTexturedModalRect(x + i * 4, y, 8, 0, 3, 6);
				break;
			case '3':
				drawTexturedModalRect(x + i * 4, y, 12, 0, 3, 6);
				break;
			case '4':
				drawTexturedModalRect(x + i * 4, y, 16, 0, 3, 6);
				break;
			case '5':
				drawTexturedModalRect(x + i * 4, y, 20, 0, 3, 6);
				break;
			case '6':
				drawTexturedModalRect(x + i * 4, y, 24, 0, 3, 6);
				break;
			case '7':
				drawTexturedModalRect(x + i * 4, y, 28, 0, 3, 6);
				break;
			case '8':
				drawTexturedModalRect(x + i * 4, y, 32, 0, 3, 6);
				break;
			case '9':
				drawTexturedModalRect(x + i * 4, y, 36, 0, 3, 6);
				break;
			case '.':
				drawTexturedModalRect(x + i * 4, y, 40, 0, 3, 6);
				break;
			case '/':
				drawTexturedModalRect(x + i * 4, y, 44, 0, 3, 6);
				break;
			case '_':
				drawTexturedModalRect(x + i * 4, y, 48, 0, 3, 6);
				break;
			case ':':
				drawTexturedModalRect(x + i * 4, y, 52, 0, 3, 6);
				break;

			case 'A':
				drawTexturedModalRect(x + i * 4, y, 0, 7, 3, 6);
				break;
			case 'B':
				drawTexturedModalRect(x + i * 4, y, 4, 7, 3, 6);
				break;
			case 'C':
				drawTexturedModalRect(x + i * 4, y, 8, 7, 3, 6);
				break;
			case 'D':
				drawTexturedModalRect(x + i * 4, y, 12, 7, 3, 6);
				break;
			case 'E':
				drawTexturedModalRect(x + i * 4, y, 16, 7, 3, 6);
				break;
			case 'F':
				drawTexturedModalRect(x + i * 4, y, 20, 7, 3, 6);
				break;
			case 'G':
				drawTexturedModalRect(x + i * 4, y, 24, 7, 3, 6);
				break;
			case 'H':
				drawTexturedModalRect(x + i * 4, y, 28, 7, 3, 6);
				break;
			case 'I':
				drawTexturedModalRect(x + i * 4, y, 32, 7, 3, 6);
				break;
			case 'J':
				drawTexturedModalRect(x + i * 4, y, 36, 7, 3, 6);
				break;
			case 'K':
				drawTexturedModalRect(x + i * 4, y, 40, 7, 3, 6);
				break;
			case 'L':
				drawTexturedModalRect(x + i * 4, y, 44, 7, 3, 6);
				break;
			case 'M':
				drawTexturedModalRect(x + i * 4, y, 48, 7, 3, 6);
				break;
			case 'N':
				drawTexturedModalRect(x + i * 4, y, 52, 7, 3, 6);
				break;
			case 'O':
				drawTexturedModalRect(x + i * 4, y, 56, 7, 3, 6);
				break;
			case 'P':
				drawTexturedModalRect(x + i * 4, y, 60, 7, 3, 6);
				break;
			case 'Q':
				drawTexturedModalRect(x + i * 4, y, 64, 7, 3, 6);
				break;
			case 'R':
				drawTexturedModalRect(x + i * 4, y, 68, 7, 3, 6);
				break;
			case 'S':
				drawTexturedModalRect(x + i * 4, y, 72, 7, 3, 6);
				break;
			case 'T':
				drawTexturedModalRect(x + i * 4, y, 76, 7, 3, 6);
				break;
			case 'U':
				drawTexturedModalRect(x + i * 4, y, 80, 7, 3, 6);
				break;
			case 'V':
				drawTexturedModalRect(x + i * 4, y, 84, 7, 3, 6);
				break;
			case 'W':
				drawTexturedModalRect(x + i * 4, y, 88, 7, 3, 6);
				break;
			case 'X':
				drawTexturedModalRect(x + i * 4, y, 92, 7, 3, 6);
				break;
			case 'Y':
				drawTexturedModalRect(x + i * 4, y, 96, 7, 3, 6);
				break;
			case 'Z':
				drawTexturedModalRect(x + i * 4, y, 100, 7, 3, 6);
				break;
			}
		}
		GlStateManager.popAttrib();
	}

	public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x + 0, y + height, 0).tex((textureX + 0) * f, (textureY + height) * f1).endVertex();
		worldrenderer.pos(x + width, y + height, 0).tex((textureX + width) * f, (textureY + height) * f1).endVertex();
		worldrenderer.pos(x + width, y + 0, 0).tex((textureX + width) * f, (textureY + 0) * f1).endVertex();
		worldrenderer.pos(x + 0, y + 0, 0).tex((textureX + 0) * f, (textureY + 0) * f1).endVertex();
		tessellator.draw();
	}

	public static void renderHUDCenter(Minecraft mc, List<ColoredString> list) {
		if(list != null && !list.isEmpty()){
			ScaledResolution sr = new ScaledResolution(mc);
			Point center = new Point(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2);
			int size = list.size();
			for (int i = 0; i < list.size(); i++){
				ColoredString str = list.get(i);
				if(str != null && !str.isEmpty()){
					int length = mc.fontRendererObj.getStringWidth(str.str);
					GlStateManager.pushMatrix();
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					GlStateManager.translate(center.getX(), center.getY(), 0);
					GlStateManager.translate(-length / 2, 20 + i * 10, 0);
					//					GL11.glPushMatrix();
					//					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					//					GL11.glTranslatef(center.getX(), center.getY(), 0);
					//					GL11.glTranslatef(-length / 2, 20 + i * 10, 0);
					mc.fontRendererObj.drawString(str.str, 0, 0, str.color, true);
					//					GL11.glPopMatrix();
					GlStateManager.popMatrix();
				}
			}
		}
	}

	public static void renderHUDRight(Minecraft mc, List<ColoredString> list) {
		if(list != null && !list.isEmpty()){
			ScaledResolution sr = new ScaledResolution(mc);
			Point right = new Point(sr.getScaledWidth(), sr.getScaledHeight() / 2);
			int size = list.size();
			for (int i = 0; i < list.size(); i++){
				ColoredString str = list.get(i);
				if(str != null && !str.isEmpty()){
					int length = mc.fontRendererObj.getStringWidth(str.str);
					GlStateManager.pushMatrix();
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					GlStateManager.translate(right.getX(), right.getY(), 0);
					GlStateManager.translate(-length - 5, -size * 10 / 2 + i * 10, 0);
					//					GL11.glPushMatrix();
					//					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					//					GL11.glTranslatef(center.getX(), center.getY(), 0);
					//					GL11.glTranslatef(-length / 2, 20 + i * 10, 0);
					mc.fontRendererObj.drawString(str.str, 0, 0, str.color, true);
					//					GL11.glPopMatrix();
					GlStateManager.popMatrix();

					//					GL11.glPushMatrix();
					//					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					//					GL11.glTranslatef(right.getX(), right.getY(), 0);
					//					GL11.glTranslatef(-length - 5, -size * 10 / 2 + i * 10, 0);
					//					mc.fontRendererObj.drawString(str.str, 0, 0, str.color, true);
					//					GL11.glPopMatrix();
				}
			}
		}
	}

}




