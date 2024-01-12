package com.example.demo.repository;

import com.example.demo.model.measurement.MeasurementConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MeasurementConsumptionRepository extends JpaRepository<MeasurementConsumption, UUID> {
    // Add custom query methods if needed

	@Modifying
	@Transactional
	@Query("DELETE FROM MeasurementConsumption")
	void deleteAllMeasurementConsumptions();

	@Modifying
	@Transactional
	@Query("DELETE FROM MeasurementConsumption WHERE id = :itemId")
	void deleteMeasurementConsumptionById(@Param("itemId") UUID id);

	@Query("SELECT m FROM MeasurementConsumption m WHERE YEAR(m.measurementDate) = :year")
	List<MeasurementConsumption> findByYear(@Param("year") int year);

	@Query("SELECT m FROM MeasurementConsumption m WHERE YEAR(m.measurementDate) = :year AND MONTH(m.measurementDate) = :month")
	List<MeasurementConsumption> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

	@Query("SELECT MONTH(m.measurementDate) AS month, SUM(m.measurementValue) AS sumValue " +
			"FROM MeasurementConsumption m " +
			"WHERE YEAR(m.measurementDate) = :year " +
			"GROUP BY MONTH(m.measurementDate)")
	List<Map<Integer, Double>> getMonthlyConsumptionByYear(@Param("year") int year);

}