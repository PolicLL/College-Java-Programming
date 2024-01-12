package main;

import java.util.Random;

public class Sensor {
    private double startRange, endRange, value;
    private int factor;
    private String unit, name;
    public Sensor(double startRange, double endRange, int factor, String unit, String name){
        this.startRange = startRange;
        this.endRange = endRange;
        this.factor = factor;
        this.unit = unit;
        this.name = name;
    }

    public double generateRandomNumberInRange(){
        this.value = new Random().nextInt((int)(endRange - startRange) + (int)startRange);
        return this.value;
    }
    public String getSensorData(){
        generateRandomNumberInRange();
        return String.format("main.Device : %s value : %s factor : %s unit : %s", name, value, factor, unit);
    }

    public double getValue() {
        return value;
    }
}