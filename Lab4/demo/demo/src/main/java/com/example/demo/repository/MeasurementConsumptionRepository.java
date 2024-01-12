package com.example.demo.repository;

import com.example.demo.model.measurement.MeasurementConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementConsumptionRepository extends JpaRepository<MeasurementConsumption, Long> {
    // Add custom query methods if needed
}