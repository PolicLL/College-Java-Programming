import org.example.MyColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class ColorTest {

    private Color color;
    private MyColor myColor;

    private String hexadecimalColor;
    private float[] hsbCode = new float[3], myHSBCode = new float[3];

    @Test
    public void TestRGBToHSB() {
        setColorObjects("0x1FF0FF");
        runConversionToHSB();
        assertEqualityOfAllHSBValues();

        // Another case

        setColorObjects("0x1AF0BF");
        runConversionToHSB();
        assertEqualityOfAllHSBValues();

        // Another case

        setColorObjects("0x1122BF");
        runConversionToHSB();
        assertEqualityOfAllHSBValues();
    }

    private void setColorObjects(String hexadecimalColor){
        color = Color.decode(hexadecimalColor);
        myColor = MyColor.decode(hexadecimalColor);
    }
    private void runConversionToHSB(){
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbCode);
        MyColor.RGBtoHSB(myColor.getRed(), myColor.getGreen(), myColor.getBlue(), myHSBCode);
    }
    private void assertEqualityOfAllHSBValues(){
        Assertions.assertEquals(hsbCode[0], myHSBCode[0], 0.001);
        Assertions.assertEquals(hsbCode[1], myHSBCode[1], 0.001);
        Assertions.assertEquals(hsbCode[2], myHSBCode[2], 0.001);
    }

    @Test
    public void TestRGBRepresentation() {
        hexadecimalColor = "0x1AF0BF";
        Assertions.assertTrue(Color.decode(hexadecimalColor).getRGB() == MyColor.decode(hexadecimalColor).getRGB());

        hexadecimalColor = "0x1AFFBB";
        Assertions.assertTrue(Color.decode(hexadecimalColor).getRGB() == MyColor.decode(hexadecimalColor).getRGB());

        hexadecimalColor = "0x100100";
        Assertions.assertTrue(Color.decode(hexadecimalColor).getRGB() == MyColor.decode(hexadecimalColor).getRGB());
    }

    @Test
    public void TestRGBToHLS() {
        float[] results = MyColor.RGBtoHSL(100, 120, 150);

        Assertions.assertEquals(216, results[0], 0.1);
        Assertions.assertEquals(0.2, results[1], 0.1);
        Assertions.assertEquals(0.49, results[2], 0.1);

        // Another test

        results = MyColor.RGBtoHSL(112, 89, 115);

        Assertions.assertEquals(293, results[0], 0.1);
        Assertions.assertEquals(0.127, results[1], 0.1);
        Assertions.assertEquals(0.4, results[2], 0.1);
    }

    @Test
    public void TestRGBToCMYK() {
        float[] results = MyColor.RGBtoCMYK(100, 120, 150);

        Assertions.assertEquals(0.33, results[0], 0.01);
        Assertions.assertEquals(0.2, results[1], 0.01);
        Assertions.assertEquals(0.00, results[2], 0.01);
        Assertions.assertEquals(0.41, results[3], 0.01);

        // Another test

        results = MyColor.RGBtoCMYK(111, 33, 225);

        Assertions.assertEquals(0.51, results[0], 0.01);
        Assertions.assertEquals(0.85, results[1], 0.01);
        Assertions.assertEquals(0.00, results[2], 0.01);
        Assertions.assertEquals(0.12, results[3], 0.01);

    }
}

