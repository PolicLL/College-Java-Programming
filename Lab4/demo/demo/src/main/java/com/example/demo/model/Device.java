package com.example.demo.model;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.model.measurement.MeasurementConsumption;
import com.example.demo.utils.RandomValueGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @OneToMany(mappedBy = "device")
    private List<MeasurementConsumption> consumptionsHistory;

    public Device(String name) {
        this.name = name;

        this.id = UUID.randomUUID();
        this.consumptionsHistory = new ArrayList<>();
    }

    public void measureConsumptionBefore(double startRange, double endRange){
        consumptionsHistory.add(generateMeasurement(startRange, endRange, RandomValueGenerator.generateRandomDate()));
    }

    public void measureConsumptionNow(double startRange, double endRange){
        consumptionsHistory.add(generateMeasurement(startRange, endRange, new Date()));
    }

    private MeasurementConsumption generateMeasurement(double startRange, double endRange, Date measureDate){
        double randomValue = RandomValueGenerator.generateRandomValue(startRange, endRange);
        return new MeasurementConsumption(measureDate, randomValue);
    }

    public void updateUsingDTO(DeviceDTO deviceDTO) {
        if(deviceDTO.getName() != null) this.setName(deviceDTO.getName());
    }


}