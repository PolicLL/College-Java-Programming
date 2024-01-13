package com.example.demo.model.measurement;

import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.model.Device;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "Measurement_Consumption")
public class MeasurementConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    @Column(name = "measurement_date")
    private Date measurementDate;

    @Column(name = "measuring_unit_energy_consumption")
    @Enumerated(EnumType.STRING)
    private MeasuringUnitEnergyConsumption measuringUnitEnergyConsumption;

    @Column(name = "measurement_value")
    private double measurementValue;

    @ManyToOne
    @JoinColumn(name = "device_id")
    @JsonIgnoreProperties("consumptionsHistory")
    private Device device;

    public MeasurementConsumption(Device device, Date measurementDate, MeasuringUnitEnergyConsumption measuringUnitEnergyConsumption, double measurementValue) {
        this.measurementDate = measurementDate;
        this.measuringUnitEnergyConsumption = measuringUnitEnergyConsumption;
        this.measurementValue = measurementValue;
        this.id = UUID.randomUUID();
        this.device = device;
    }

    public MeasurementConsumption(Device device, Date measurementDate, double measurementValue) {
        this.measurementDate = measurementDate;
        this.measurementValue = measurementValue;
        this.measuringUnitEnergyConsumption = MeasuringUnitEnergyConsumption.kWh;
        this.id = UUID.randomUUID();
        this.device = device;
    }

    public MeasurementConsumption(Device device, Date date) {

    }

    @Override
    public String toString() {
        return String.format("ID : %s Date : %s | Unit : %s | Value : %s\n",
                this.id, this.measurementDate, this.measuringUnitEnergyConsumption, this.measurementValue);
    }

    public void updateUsingDTO(MeasurementConsumptionDTO measurementDTO) {
        if (measurementDTO.getMeasurementDate() != null) {
            this.setMeasurementDate(measurementDTO.getMeasurementDate());
        }
        if (measurementDTO.getMeasuringUnitEnergyConsumption() != null) {
            this.setMeasuringUnitEnergyConsumption(measurementDTO.getMeasuringUnitEnergyConsumption());
        }
        if (measurementDTO.getMeasurementValue() != 0.0) {
            this.setMeasurementValue(measurementDTO.getMeasurementValue());
        }
    }

}
