package com.scchuangtou.utils;

import java.math.BigDecimal;
import java.util.Random;

public class MathUtils {
	public static float sum(float v1, float v2) {
		BigDecimal sum = new BigDecimal(Float.toString(v1));
		sum = sum.add(new BigDecimal(Float.toString(v2)));
		return sum.floatValue();
	}

	public static float sub(float v1, float v2) {
		BigDecimal sub = new BigDecimal(Float.toString(v1));
		sub = sub.subtract(new BigDecimal(Float.toString(v2)));
		return sub.floatValue();
	}

	public static float multiply(float v1, float v2) {
		BigDecimal sub = new BigDecimal(Float.toString(v1));
		sub = sub.multiply(new BigDecimal(Float.toString(v2)));
		return sub.floatValue();
	}

	public static float divide(float v1, float v2) {
		BigDecimal sub = new BigDecimal(Float.toString(v1));
		sub = sub.divide(new BigDecimal(Float.toString(v2)),2,BigDecimal.ROUND_HALF_EVEN);
		return sub.floatValue();
	}
	
	public static float getRandomGold(float min,float max){
		float gold=new Random().nextFloat();
		gold=MathUtils.multiply(gold, max);
		BigDecimal b = new BigDecimal(gold);  
		gold = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		if(gold<min)
			gold=min;
		if(gold>max)
			gold=max;
		return gold;
	}

	public static void main(String[] args) {
		float f = 100000.99f;
		float f2 = 0.02f;
		float sum = f + f2;
		System.out.println(sum);
		System.out.println(sum(f, f2));
		System.out.println(sub(f, f2));
		System.out.println(multiply(1.1f, 2f));
		System.out.println(divide(10, 3));
	}
}
