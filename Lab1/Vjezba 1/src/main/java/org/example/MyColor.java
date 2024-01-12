package org.example;

public class MyColor {

    private int[] RGB;

    private static float convertedRed, convertedGreen, convertedBlue;
    private static float maxValue, minValue, difference;

    public MyColor(int red, int green, int blue) {
        this.RGB = new int[]{red, green, blue};
    }

    public static MyColor decode(String color){
        int red = Integer.parseInt(color.substring(2, 4), 16);
        int green = Integer.parseInt(color.substring(4, 6), 16);
        int blue = Integer.parseInt(color.substring(6, 8), 16);

        return new MyColor(red, green, blue);
    }

    public static void RGBtoHSB(int red, int green, int blue, float[] hsbCode){
        if(hsbCode == null) hsbCode = new float[3];

        convertToAppropriateRange(red, green, blue);
        setMaxAndMinValues();
        setDifference();

        float saturation = (maxValue == 0) ? 0 : (difference / maxValue);

        float hue;

        if(maxValue == convertedRed) hue = ( 60 * (convertedGreen - convertedBlue) / difference + 360);
        else if(maxValue == convertedGreen) hue = ( 60 * (convertedBlue - convertedRed) / difference + 120);
        else hue = (60 * (convertedRed - convertedGreen) / difference + 240);

        hue /= 360.0f;

        hsbCode[0] = hue;
        hsbCode[1] = saturation;
        hsbCode[2] = maxValue;
    }

    public static float[] RGBtoHSL(int red, int green, int blue){
        convertToAppropriateRange(red, green, blue);
        setMaxAndMinValues();
        setDifference();

        float lightness = (maxValue + minValue) / 2;

        float saturation = (difference == 0) ? 0 : (difference / (1 - Math.abs(2 * lightness - 1)));

        float hue = 0;

        if(maxValue == convertedRed) hue = 60 * (((convertedGreen - convertedBlue) / difference) % 6);
        else if(maxValue == convertedGreen) hue = 60 * (((convertedBlue - convertedRed) / difference) + 2);
        else hue = 60 * (((convertedRed - convertedGreen) / difference) + 4);

        return new float[] {hue, saturation, lightness};
    }

    public static float[] RGBtoCMYK(int red, int green, int blue){
        convertToAppropriateRange(red, green, blue);
        setMaxAndMinValues();

        float blackKey = 1 - maxValue;
        float cyanColor = (1 - convertedRed - blackKey) / (1 - blackKey);
        float magnetaColor = (1 - convertedGreen - blackKey) / (1 - blackKey);
        float yellowColor = (1 - convertedBlue - blackKey) / (1 - blackKey);

        return new float[]{cyanColor, magnetaColor, yellowColor, blackKey};
    }


    public int getRGB() {
        return (255 << 24) | (RGB[0] << 16) | (RGB[1] << 8) | (RGB[2]);
    }

    private static void convertToAppropriateRange(int red, int green, int blue){
        convertedRed = red / 255.0f;
        convertedGreen = green / 255.0f;
        convertedBlue = blue / 255.0f;
    }

    private static void setMaxAndMinValues(){
        maxValue = Math.max(Math.max(convertedRed, convertedGreen), convertedBlue);
        minValue = Math.min(Math.min(convertedRed, convertedGreen), convertedBlue);
    }

    private static void setDifference(){
        difference = maxValue - minValue;
    }

    public int getRed() {
        return RGB[0];
    }

    public int getGreen() {
        return RGB[1];
    }

    public int getBlue() {
        return RGB[2];
    }
}
