package com.example.demo.model;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.mapper.MeasurementConsumptionMapper;
import com.example.demo.model.measurement.MeasurementConsumption;
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

    public void measureConsumptionBefore(double startRange, double endRange){
        consumptionsHistory.add(generateMeasurement(startRange, endRange, RandomValueGenerator.generateRandomDate()));
    }

    public void measureConsumptionNow(double startRange, double endRange){
        consumptionsHistory.add(generateMeasurement(startRange, endRange, new Date()));
    }

    private MeasurementConsumption generateMeasurement(double startRange, double endRange, Date measureDate){
        double randomValue = RandomValueGenerator.generateRandomValue(startRange, endRange);
        return new MeasurementConsumption(this, measureDate, randomValue);
    }

    public void updateUsingDTO(DeviceDTO deviceDTO) {
        if(deviceDTO.getName() != null) this.setName(deviceDTO.getName());
    }


    public void measureConsumptionForMonth(int month, double startRange, double endRange) {
        double randomValue = RandomValueGenerator.generateRandomValue(startRange, endRange);
        consumptionsHistory.add(new MeasurementConsumption(this, createDateWithMonth(month), randomValue));
    }

    public void measureConsumptionForMonth(Date date, double value) {
        consumptionsHistory.add(new MeasurementConsumption(this, date));
    }

    public void createMeasurement(MeasurementConsumptionDTO measurementConsumptionDTO, Device device) {
        consumptionsHistory.add(MeasurementConsumptionMapper.toMeasurementConsumption(measurementConsumptionDTO, device));
    }

    public static Date createDateWithMonth(int targetMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, targetMonth - 1); // Subtract 1 as months are zero-based

        // Set the year to the current year (you might want to adjust if you need a different year)
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.YEAR, currentYear);

        return calendar.getTime();
    }
}