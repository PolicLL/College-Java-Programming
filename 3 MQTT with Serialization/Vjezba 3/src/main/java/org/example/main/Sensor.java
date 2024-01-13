package org.example.main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@NoArgsConstructor
public class Sensor {

    private double startRange;
    private double endRange;
    private int factor;
    private String unit;
    private String name;

    @JsonIgnore
    private double value;

    public Sensor(double startRange, double endRange, int factor, String unit, String name) {
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