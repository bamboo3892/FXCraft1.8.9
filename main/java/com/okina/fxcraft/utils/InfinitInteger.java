package com.okina.fxcraft.utils;

public class InfinitInteger implements Comparable<InfinitInteger> {

	public static final InfinitInteger ZERO = new InfinitInteger(0);

	public final boolean negative;
	public final String value;

	public InfinitInteger(long value) {
		this.negative = value < 0;
		this.value = fromHexToChars(Long.toHexString(Math.abs(value)));
	}

	public InfinitInteger(String value, boolean negative) {
		if(value.startsWith(String.valueOf((char) 0)) && value.length() > 1){
			StringBuilder str = new StringBuilder(value);
			do{
				str.deleteCharAt(0);
			}while (str.charAt(0) == ((char) 0) && str.length() > 1);
			value = str.toString();
		}
		if(value.length() == 0){
			value = String.valueOf((char) 0);
		}
		this.value = value;
		this.negative = negative;
	}

	public InfinitInteger plus(long l) {
		return plus(new InfinitInteger(l));
	}

	public InfinitInteger plus(InfinitInteger integer) {
		if(negative == integer.negative){
			StringBuilder str = new StringBuilder();
			int roop = Math.max(value.length(), integer.value.length());
			char up = 0;//0 or 1
			for (int i = 0; i < roop; i++){
				char c1 = i < value.length() ? value.charAt(value.length() - i - 1) : (char) 0;
				char c2 = i < integer.value.length() ? integer.value.charAt(integer.value.length() - i - 1) : (char) 0;
				String chars = addChars(c1, c2);
				if(up == 1){
					if(chars.length() == 1){
						chars = addChars(chars.charAt(0), (char) 1);
					}else{
						char c3 = chars.charAt(1);
						c3++;
						chars = String.valueOf((char) 1) + c3;
					}
				}
				up = (char) (chars.length() == 1 ? 0 : 1);
				str.append(String.valueOf(chars.charAt(chars.length() - 1)));
			}
			if(up == 1){
				str.append(String.valueOf((char) 1));
			}
			char[] cs = new char[str.length()];
			for (int i = 0; i < cs.length; i++){
				cs[i] = str.charAt(cs.length - i - 1);
			}
			return new InfinitInteger(new String(cs), negative);
		}else{
			if(value.equals(integer.value)){
				return ZERO;
			}else{
				if(!negative){
					int scale = Math.max(value.length(), integer.value.length());
					String complement = getComplement(integer.value, scale);
					InfinitInteger i = plus(new InfinitInteger(complement, false));
					if(i.value.length() > scale){
						return new InfinitInteger(i.value.substring(1), false);
					}else{
						complement = getComplement(i.value, scale);
						return new InfinitInteger(complement, true);
					}
				}else{
					return integer.plus(this);
				}
			}
		}
	}

	public InfinitInteger neg() {
		return new InfinitInteger(value, !negative);
	}

	public long getLongValue() {
		if(compareTo(new InfinitInteger(Long.MAX_VALUE)) >= 0){
			return Long.MAX_VALUE;
		}else if(compareTo(new InfinitInteger(Long.MIN_VALUE)) < 0){
			return Long.MIN_VALUE;
		}else{
			long l = Long.parseLong(fromCharsToHex(value), 16);
			if(negative) l *= -1;
			return l;
		}
	}

	public String getHexString() {
		StringBuilder str = new StringBuilder(negative ? "-" : "");
		for (int i = 0; i < value.length(); i++){
			String s = Integer.toHexString(((int) value.charAt(i)));
			if(i != 0){
				for (int j = 0; j < 4 - s.length(); j++){
					str.append(0);
				}
			}
			str.append(s);
		}
		return str.toString();
	}

	@Override
	public String toString() {
		return getHexString();
		//		return (negative ? "-" : "+") + value;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof InfinitInteger){
			InfinitInteger integer = (InfinitInteger) obj;
			return value.equals(integer.value) && negative == integer.negative;
		}else{
			return false;
		}
	}

	@Override
	public int compareTo(InfinitInteger integer) {
		if(negative == integer.negative){
			if(value.length() < integer.value.length()){
				return negative ? 1 : -1;
			}else if(value.length() > integer.value.length()){
				return negative ? -1 : 1;
			}else{
				for (int i = 0; i < value.length(); i++){
					char c1 = value.charAt(i);
					char c2 = integer.value.charAt(i);
					if(c1 < c2){
						return negative ? 1 : -1;
					}else if(c1 > c2){
						return negative ? -1 : 1;
					}
				}
				return 0;
			}
		}else{
			return negative ? -1 : 1;
		}
	}

	public static String addChars(char c1, char c2) {
		int i = (int) c1 + (int) c2;
		if(i > 0xffff){
			i -= 0x10000;
			return (char) 1 + "" + (char) i;
		}else{
			return String.valueOf((char) i);
		}
	}

	public static String fromHexToChars(String hex) {
		if(hex.startsWith("0x")){
			hex = hex.substring(2);
		}
		int roop = (int) (hex.length() / 4.0f);
		int flag = hex.length() % 4;
		StringBuilder str = new StringBuilder();
		if(flag != 0){
			str.append(String.valueOf((char) Integer.parseUnsignedInt(hex.substring(0, flag), 16)));
		}
		for (int i = 0; i < roop; i++){
			String hex1 = hex.substring(i * 4 + flag, i * 4 + flag + 4);
			char c = (char) Integer.parseUnsignedInt(hex1, 16);
			str.append(String.valueOf(c));
		}
		return str.toString();
	}

	public static String fromCharsToHex(String chars) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < chars.length(); i++){
			String s = Integer.toHexString(((int) chars.charAt(i)));
			if(i != 0){
				for (int j = 0; j < 4 - s.length(); j++){
					str.append(0);
				}
			}
			str.append(s);
		}
		return str.toString();
	}

	public static String getComplement(String charStr, int scale) {
		if(charStr.length() > scale) throw new IllegalArgumentException();
		char[] chars = charStr.toCharArray();
		char[] dest = new char[scale];
		boolean flag = true;
		for (int i = 0; i < scale; i++){
			char c1 = chars.length - i - 1 < 0 ? (char) 0 : chars[chars.length - i - 1];
			int i2 = (flag ? 0x10000 - c1 : 0xffff - c1);
			if(i2 == 0x10000){
				dest[scale - i - 1] = (char) 0;
			}else{
				dest[scale - i - 1] = (char) i2;
				flag = false;
			}
		}
		return flag ? (char) 1 + new String(dest) : new String(dest);
	}

}




