package com.example.demo.model.measurement;

import com.example.demo.model.Device;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class MeasurementConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @Column(name = "measurement_date")
    private Date measurementDate;

    @Column(name = "measuring_unit_energy_consumption")
    private MeasuringUnitEnergyConsumption measuringUnitEnergyConsumption;

    @Column(name = "measurement_value")
    private double measurementValue;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    public MeasurementConsumption(Date measurementDate, MeasuringUnitEnergyConsumption measuringUnitEnergyConsumption, double measurementValue) {
        this.measurementDate = measurementDate;
        this.measuringUnitEnergyConsumption = measuringUnitEnergyConsumption;
        this.measurementValue = measurementValue;
    }

    public MeasurementConsumption(Date measurementDate, double measurementValue) {
        this.measurementDate = measurementDate;
        this.measurementValue = measurementValue;
        this.measuringUnitEnergyConsumption = MeasuringUnitEnergyConsumption.kWh;
    }

    @Override
    public String toString() {
        return String.format("Date : %s | Unit : %s | Value : %s\n",
                this.measurementDate, this.measuringUnitEnergyConsumption, this.measurementValue);
    }
}
