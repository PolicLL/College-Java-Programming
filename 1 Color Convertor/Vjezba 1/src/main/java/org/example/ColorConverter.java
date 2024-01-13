package org.example;

import javax.sound.midi.Soundbank;
import java.awt.Color;
public class ColorConverter {
    public static void main(String[] args) {

        System.out.println("123".substring(0, 1));

        String hexColor = "0xF100A4";

        Color c = Color.decode(hexColor);
        MyColor c1 = MyColor.decode(hexColor);

        float[] hsbCode = new float[3];
        float[] hsbCode1 = new float[3];

        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbCode);
        MyColor.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), hsbCode1);

        System.out.println("RGB : " + c.getRGB());

        System.out.println("Boja u HEX formatu: 0x" + Integer.toHexString(c.getRGB() & 0x00FFFFFF));
        System.out.println("Boja u RGB formatu: " + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue());
        System.out.println("Boja u HSB formatu: " + hsbCode[0] * 360 + "°, " + hsbCode[1] * 100 + "%, " + hsbCode[2] * 100 + "%");

        System.out.println("My class : ");

        System.out.println("Boja u HEX formatu: 0x" + Integer.toHexString(c1.getRGB() & 0x00FFFFFF));
        System.out.println("Boja u RGB formatu: " + c1.getRed() + ", " + c1.getGreen() + ", " + c1.getBlue());
        System.out.println("Boja u HSB formatu: " + hsbCode1[0] * 360 + "°, " + hsbCode1[1] * 100 + "%, " + hsbCode1[2] * 100 + "%");
    }
}
