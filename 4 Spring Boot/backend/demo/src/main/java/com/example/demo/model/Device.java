package com.example.demo.model;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.model.measurement.MeasurementConsumption;
import com.example.demo.utils.DateUtils;
import com.example.demo.utils.RandomValueGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "device", cascade = CascadeType.ALL)
    private List<MeasurementConsumption> consumptionsHistory;

    public Device(String name) {
        this.name = name;

        this.id = UUID.randomUUID();
        this.consumptionsHistory = new ArrayList<>();
    }


    public void generateMeasurementNow(double startRange, double endRange){
        consumptionsHistory.add(generateMeasurement(startRange, endRange, new Date()));
    }

    private MeasurementConsumption generateMeasurement(double startRange, double endRange, Date measureDate){
        double randomValue = RandomValueGenerator.generateRandomValue(startRange, endRange);
        return new MeasurementConsumption(this, measureDate, randomValue);
    }

    public void measureConsumptionForMonth(int month, double startRange, double endRange) {
        double randomValue = RandomValueGenerator.generateRandomValue(startRange, endRange);
        consumptionsHistory.add(new MeasurementConsumption(this, DateUtils.createDateWithMonth(month), randomValue));
    }

    public void addMeasurement(MeasurementConsumption measurementConsumption) {
        consumptionsHistory.add(measurementConsumption);
    }

}