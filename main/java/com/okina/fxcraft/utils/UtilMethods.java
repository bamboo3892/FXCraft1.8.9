package com.okina.fxcraft.utils;

import java.util.HashMap;

import com.google.common.collect.Maps;
import com.okina.fxcraft.account.AccountInfo;
import com.okina.fxcraft.account.FXPosition;
import com.okina.fxcraft.account.GetPositionOrder;
import com.okina.fxcraft.account.SettlePositionOrder;
import com.okina.fxcraft.rate.FXRateGetHelper;
import com.okina.fxcraft.rate.RateData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class UtilMethods {

	public static MovingObjectPosition getMovingObjectPositionFromPlayer(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
		float f = playerIn.rotationPitch;
		float f1 = playerIn.rotationYaw;
		double d0 = playerIn.posX;
		double d1 = playerIn.posY + (double) playerIn.getEyeHeight();
		double d2 = playerIn.posZ;
		Vec3 vec3 = new Vec3(d0, d1, d2);
		float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = 5.0D;
		if(playerIn instanceof EntityPlayerMP){
			d3 = ((EntityPlayerMP) playerIn).theItemInWorldManager.getBlockReachDistance();
		}
		Vec3 vec31 = vec3.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
		return worldIn.rayTraceBlocks(vec3, vec31, useLiquids, !useLiquids, false);
	}

	public static Entity getCollidedEntityFromEntity(World world, EntityPlayer player, double radius) {
		float f1 = player.rotationPitch;
		float f2 = player.rotationYaw;
		double entityPosX = player.posX;
		double entityPosY = player.posY + (double) player.getEyeHeight();
		double entityPosZ = player.posZ;
		Vec3 startPos = new Vec3(entityPosX, entityPosY, entityPosZ);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		Vec3 endPos = startPos.addVector(f7 * radius, f6 * radius, f8 * radius);
		Vec3 vec = endPos.subtract(startPos);

		float gap = 0.05f;
		int count = (int) (radius / gap);
		for (int i = 0; i < count; i++){
			float dist = i * gap;
			Vec3 point = startPos.addVector(vec.xCoord * dist, vec.yCoord * dist, vec.zCoord * dist);
			for (int j = 0; j < world.loadedEntityList.size(); j++){
				Entity e = world.loadedEntityList.get(j);
				if(e != player && isEntiyOnPoint(e, point)){
					return e;
				}
			}
		}
		return null;
	}

	public static boolean isEntiyOnPoint(Entity entity, Vec3 point) {
		AxisAlignedBB box = entity.getEntityBoundingBox();
		return box.minX < point.xCoord && box.maxX > point.xCoord && box.minY < point.yCoord && box.maxY > point.yCoord && box.minZ < point.zCoord && box.maxZ > point.zCoord;
	}

	public static int[] getRandomArray(int min, int max) {
		int[] re = new int[max - min + 1];
		for (int i = 0; i < re.length; i++){
			re[i] = i + min;
		}
		re = getRandomArray(re);
		return re;
	}

	public static int[] getRandomArray(int[] array) {
		if(array == null || array.length == 0) return null;
		int[] newArray = array.clone();
		boolean[] flags = new boolean[array.length];
		int index1;
		int index2;
		for (int i = 0; i < array.length; i++){
			index1 = (int) (Math.random() * (array.length - i));
			index2 = 0;
			for (int j = 0; j <= index1; j++){
				while (flags[index2])
					index2++;
				if(j < index1) index2++;
			}
			newArray[i] = array[index2];
			flags[index2] = true;
		}
		return newArray;
	}

	public static String zeroFill(int number, int length) {
		String str = String.valueOf(number);
		if(str.length() >= length){
			return str;
		}else{
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < length - str.length(); i++){
				builder.append("0");
			}
			builder.append(str);
			return builder.toString();
		}
	}

	public static HashMap<String, Object> getTableFromRate(RateData rate) {
		HashMap<String, Object> map = Maps.newHashMap();
		map.put("rate", rate.open);
		map.put("date", FXRateGetHelper.getCalendarString(rate.calendar, -1));
		return map;
	}

	public static HashMap<String, Object> getTableFromPosition(FXPosition position) {
		HashMap<String, Object> map = Maps.newHashMap();
		map.put("date", FXRateGetHelper.getCalendarString(position.contractDate, -1));
		map.put("pair", position.currencyPair);
		map.put("lot", position.lot);
		map.put("deposit", position.depositLot);
		map.put("askOrBid", position.askOrBid);
		map.put("rate", position.contractRate);
		map.put("positionID", position.positionID);
		return map;
	}

	public static HashMap<String, Object> getTableFromGetOrder(GetPositionOrder order) {
		HashMap<String, Object> map = Maps.newHashMap();
		map.put("date", FXRateGetHelper.getCalendarString(order.contractDate, -1));
		map.put("pair", order.currencyPair);
		map.put("lot", order.lot);
		map.put("deposit", order.depositLot);
		map.put("askOrBid", order.askOrBid);
		map.put("limits", order.limits);
		map.put("orderID", order.orderID);
		return map;
	}

	public static HashMap<String, Object> getTableFromSettleOrder(SettlePositionOrder order) {
		HashMap<String, Object> map = Maps.newHashMap();
		map.put("position", getTableFromPosition(order.position));
		map.put("date", FXRateGetHelper.getCalendarString(order.contractDate, -1));
		map.put("limits", order.limits);
		map.put("orderID", order.position.positionID);
		return map;
	}

	public static HashMap<String, Object> getTableFromAccount(AccountInfo account) {
		HashMap<String, Object> map = Maps.newHashMap();
		map.put("name", account.name);
		map.put("balance", account.balance);
		return map;
	}

}
