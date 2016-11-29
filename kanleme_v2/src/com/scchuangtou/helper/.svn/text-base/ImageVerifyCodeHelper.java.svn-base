package com.scchuangtou.helper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scchuangtou.utils.MD5Utils;

/**
 * Created by SYT on 2016/4/29.
 */
public class ImageVerifyCodeHelper {

	/**
	 * 验证码图片的宽度。
	 */
	private final static int width = 100;

	/**
	 * 验证码图片的高度。
	 */
	private final static int height = 40;

	/**
	 * 验证码字符个数
	 */
	private final static int codeCount = 5;

	/**
	 * codeSequence
	 */
	private final static String[] codeSequence = { "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f",
			"g", "h", "j", "k", "m", "n", "p", "r", "s", "t", "u", "v", "w", "x", "y" };

	public static String sendVerifyCodeImage(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int xx = width / (codeCount + 2); // 生成随机数的水平距离
		int fontHeight = height - 12; // 生成随机数的数字高度
		int codeY = height - 8; // 生成随机数的垂直距离
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D gd = buffImg.createGraphics();

		// 创建一个随机数生成器类
		Random random = new Random();

		// 将图像填充为白色
		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);

		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		// 设置字体。
		gd.setFont(font);

		// 画边框。
		gd.setColor(Color.BLACK);
		gd.drawRect(0, 0, width - 1, height - 1);

		// 随机产生4条干扰线，使图象中的认证码不易被其它程序探测到。
		gd.setColor(Color.BLACK);
		for (int i = 0; i < 8; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gd.drawLine(x, y, x + xl, y + yl);
		}

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();

		int red = 0;
		int green = 0;
		int blue = 0;

		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			String strRand = codeSequence[random.nextInt(codeSequence.length)];
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			red = random.nextInt(125);
			green = random.nextInt(255);
			blue = random.nextInt(200);

			// 用随机产生的颜色将验证码绘制到图像中。
			gd.setColor(new Color(red, green, blue));
			gd.drawString(strRand, (i + 1) * xx, codeY);

			// 将产生的四个随机数组合在一起。
			randomCode.append(strRand);
		}
		String code = randomCode.toString();
		setCode(request, response, code);

		// 禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		response.setContentType("image/jpeg");

		// 将图像输出到Servlet输出流中。
		ImageIO.write(buffImg, "jpeg", response.getOutputStream());

		return code;
	}

	private static final String CODE = "image_code";

	public static boolean checkCode(HttpServletRequest request, String code) {
		if (code == null) {
			return false;
		}
		code = MD5Utils.md5(code.toLowerCase().getBytes(), MD5Utils.MD5Type.MD5_16);

		Object imageCode = request.getSession(true).getAttribute(CODE);
		imageCode = imageCode == null ? "" : imageCode.toString();
		request.getSession(true).setAttribute(CODE, null);//比较过后清除
		return imageCode.equals(code);
	}

	private static void setCode(HttpServletRequest request, HttpServletResponse response, String code) {
		code = MD5Utils.md5(code.toLowerCase().getBytes(), MD5Utils.MD5Type.MD5_16);

		request.getSession(true).setAttribute(CODE, code);
	}
}
