package com.example.demo.model.measurement;


public enum MeasuringUnitEnergyConsumption {
    kWh("kWh"),
    Wh("Wh"),
    MWh("MWh"),
    GWh("GWh");

    private String label;

    MeasuringUnitEnergyConsumption(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

