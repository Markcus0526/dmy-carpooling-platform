package com.nmdy.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class VCodeBuilder {
	
	public static BufferedImage genCaptcha(int width, int height, String secureCode) {		
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(getRandColor(200, 235));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.BOLD, 22)); 
        for (int i = 0; i < 4; i++) {
                g.setColor(new Color(20 + Common.rand(110), 20 + Common.rand(110), 20 + Common.rand(110)));
                int x = Common.rand(width);
                int y = Common.rand(height);
                int xl = Common.rand(width);
                int yl = Common.rand(height);
                g.drawLine(x, y, x + xl, y + yl);
        }
        
        for (int i = 0; i < 4; i++) {
                char rand = secureCode.charAt(i);
                g.setColor(getRandColor(10, 150));
                g.drawString(String.valueOf(rand), 24 * i + Common.rand(4), 22 + Common.rand(6));
        }
        
        g.dispose();
        return image;
    }
	
	public static String genSecureCode(int len) {
		return Common.genCode(len);
	}
	
	public static String genSecureCode() {
        int seccode = Integer.parseInt(Common.getRandStr(6, true));
        String s = Integer.toString(seccode, 24); 
        while (s.length() < 4) {
                s = "0" + s;
        }
        String seccodeUnits = "BCEFGHJKMPQRTVWXY2346789";
        StringBuilder secCodeHiddenBuf = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
                int unit = s.charAt(i);
                if (unit >= 0x30 && unit <= 0x39) {
                        secCodeHiddenBuf.append(seccodeUnits.charAt(unit - 0x30));
                } else {
                        secCodeHiddenBuf.append(seccodeUnits.charAt(unit - 0x57));
                }
        }
        return secCodeHiddenBuf.toString();
    }

	public static Color getRandColor(int fc, int bc) {
	        if (fc > 255) {
	                fc = 255;
	        }
	        if (bc > 255) {
	                bc = 255;
	        }
	        int r = fc + Common.rand(bc - fc);
	        int g = fc + Common.rand(bc - fc);
	        int b = fc + Common.rand(bc - fc);
	        return new Color(r, g, b);
	}
}
