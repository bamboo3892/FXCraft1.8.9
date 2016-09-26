package com.okina.fxcraft.main;

import com.okina.fxcraft.utils.InfinitInteger;

public class Main {

	public static void main(String[] args) {
		System.out.println(new InfinitInteger("", false).getHexString());

		//		Random rand = new Random();
		//		for (int i = 0; i < 100; i++){
		//		InfinitInteger ii1 = new InfinitInteger(Math.abs(rand.nextLong()));
		//		InfinitInteger ii2 = new InfinitInteger(InfinitInteger.getComplement(ii1.value, ii1.value.length()), false);
		//		InfinitInteger ii3 = new InfinitInteger(InfinitInteger.getComplement(ii2.value, ii2.value.length()), false);
		//		if(!ii1.equals(ii3)){
		//		System.out.println(ii1.getHexString());
		//		System.out.println(ii2.getHexString());
		//		System.out.println(ii3.getHexString());
		//		System.out.println(ii1.add(ii2).getHexString());
		//		}
		//		}

		//		InfinitInteger ii4 = new InfinitInteger(0xffff);
		//		InfinitInteger ii5 = new InfinitInteger(-0xffff);
		//		InfinitInteger ii6 = ii4.add(ii5);
		//		System.out.println(ii4.getHexString());
		//		System.out.println(ii5.getHexString());
		//		System.out.println(ii6.getHexString());

		//		for (int i = 0xfff0; i <= 0x10000; i++){
		//			for (int j = 0; j <= 0x20; j++){
		//				check(new InfinitInteger(i), new InfinitInteger(j));
		//				check(new InfinitInteger(i), new InfinitInteger(-j));
		//			}
		//		}

		//		new InfinitInteger((char) 0 + " d", false);

		//		check(new InfinitInteger(-0x22972608), new InfinitInteger(0x2bcdffba));
		//		System.out.println("-22972608");
		//		System.out.println("-22972608");
		//		System.out.println(new InfinitInteger(0x936d9b2).add(new InfinitInteger(InfinitInteger.fromHexToChars("D4320046"), false)).getHexString());
		//		System.out.println(new InfinitInteger(0x936d9b2).add(new InfinitInteger(-0x2bcdffba)).getHexString());

		//		System.out.println(new InfinitInteger(0xd9b2).add(new InfinitInteger(0xffba).neg()).getHexString());
		//		System.out.println(new InfinitInteger(0x936).add(new InfinitInteger(0x2bcd).neg()).getHexString());

		//		for (int i = 0; i < 1000000; i++){
		//			check(new InfinitInteger(rand.nextLong()), new InfinitInteger(rand.nextLong()));
		//		}
	}

	private static void check(InfinitInteger i1, InfinitInteger i2) {
		InfinitInteger i3 = i1.plus(i2);
		InfinitInteger i12 = i3.plus(i2.neg());
		InfinitInteger i22 = i3.plus(i1.neg());
		if(!i1.equals(i12) || !i2.equals(i22)){
			System.out.println("i1: " + i1.getHexString());
			System.out.println("i2: " + i2.getHexString());
			System.out.println("i1 + i2: " + i3.getHexString());
			System.out.println("chech: " + i12.getHexString());
			System.out.println(i1.equals(i12) ? "OK" : "Wrong");
			System.out.println("chech: " + i22.getHexString());
			System.out.println(i2.equals(i22) ? "OK" : "Wrong");
		}
	}

}
