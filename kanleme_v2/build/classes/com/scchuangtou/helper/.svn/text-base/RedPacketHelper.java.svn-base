package com.scchuangtou.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.scchuangtou.utils.MathUtils;

public class RedPacketHelper {
	public static List<RedPacket> createRedPacket(float money, int count) {
		final int min_money = 1;// 单位分
		BigDecimal sub = new BigDecimal(Float.toString(money));
		int totalMoney = sub.multiply(new BigDecimal(Float.toString(100))).intValue();

		if ((min_money * count) > totalMoney) {
			return null;
		}
		int m, n, residueCount;
		List<Float> packetMoneys = new ArrayList<>(count);
		Random mRandom = new Random();
		for (int i = 1; i < count; i++) {
			residueCount = count - i;
			n = totalMoney - min_money * residueCount;// 剩余可用总金额
			if (residueCount > 1) {
				n = n / residueCount;
			}
			if (n > 1) {
				m = mRandom.nextInt(n);
			} else {
				m = 0;
			}
			m = m + min_money;
			packetMoneys.add(MathUtils.divide(m, 100f));
			totalMoney -= m;
			if (totalMoney <= 0) {
				return null;
			}
		}
		if (totalMoney <= 0) {
			return null;
		}
		packetMoneys.add(MathUtils.divide(totalMoney, 100f));

		List<RedPacket> packets = new ArrayList<>(packetMoneys.size());
		RedPacket paket = null;
		if (packetMoneys.size() >= 4) {
			Collections.sort(packetMoneys);// 自然升序进行排序

			float min = packetMoneys.remove(0);// 手气最差
			float max = packetMoneys.remove(packetMoneys.size() - 1);// 手气最好

			if (packetMoneys.size() <= 2) {
				paket = new RedPacket(packetMoneys.remove(0), RedPacket.TYPE_FAST);
				packets.add(paket);
				paket = new RedPacket(min, RedPacket.TYPE_WORST);
				packets.add(paket);
				paket = new RedPacket(max, RedPacket.TYPE_GOOD);
				packets.add(paket);
				paket = new RedPacket(packetMoneys.remove(packetMoneys.size() - 1), RedPacket.TYPE_SLOWEST);
				packets.add(paket);
			} else {
				float fast = packetMoneys.remove(0);// 最快
				float slowest = packetMoneys.remove(mRandom.nextInt(packetMoneys.size()));// 最慢

				for (float p : packetMoneys) {
					paket = new RedPacket(p, RedPacket.TYPE_NORMAL);
					packets.add(paket);
				}
				paket = new RedPacket(min, RedPacket.TYPE_WORST);
				packets.add(paket);
				paket = new RedPacket(max, RedPacket.TYPE_GOOD);
				packets.add(paket);

				Collections.shuffle(packets);// 抛出最快和最慢进行随机排序
				paket = new RedPacket(fast, RedPacket.TYPE_FAST);
				packets.add(0, paket);
				paket = new RedPacket(slowest, RedPacket.TYPE_SLOWEST);
				packets.add(paket);
			}
		} else {
			for (float p : packetMoneys) {
				paket = new RedPacket(p, RedPacket.TYPE_NORMAL);
				packets.add(paket);
			}
		}
		return packets;
	}

	public static class RedPacket{
		public static final int TYPE_NORMAL = 0;// 普通
		public static final int TYPE_GOOD = 1;// 手气最好
		public static final int TYPE_WORST = 2;// 手气最差
		public static final int TYPE_FAST = 3;// 手速最快
		public static final int TYPE_SLOWEST = 4;// 手速最慢
		public float money;
		public int type;
		
		public RedPacket(float money) {
			this.money = money;
		}

		public RedPacket(float money, int type) {
			this.money = money;
			this.type = type;
		}
		
		public int getType(){
			return type;
		}
	}
}
