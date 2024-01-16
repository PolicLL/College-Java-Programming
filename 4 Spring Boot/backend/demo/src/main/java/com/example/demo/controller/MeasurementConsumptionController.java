package com.example.demo.controller;

import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.DTO.MeasurementConsumptionReport;
import com.example.demo.exception.response.ErrorResponse;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.MeasurementConsumptionNotFoundException;
import com.example.demo.model.measurement.MeasurementConsumption;
import com.example.demo.service.MeasurementConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;


@RestController
@RequestMapping("/measurement-consumption")
@RequiredArgsConstructor
public class MeasurementConsumptionController {

	private final MeasurementConsumptionService measurementConsumptionService;
	private static final Logger logger = Logger.getLogger(MeasurementConsumptionController.class.getName());

	@PostMapping("/{deviceId}")
	public ResponseEntity<?> createMeasurementConsumption(@PathVariable UUID deviceId, @RequestBody MeasurementConsumptionDTO measurementDTO) {

		MeasurementConsumptionDTO createdMeasurement = measurementConsumptionService.createMeasurementConsumption(measurementDTO, deviceId);
		logger.info("Measurement consumption created: " + createdMeasurement.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(createdMeasurement);
	}

	@GetMapping
	public ResponseEntity<List<MeasurementConsumptionDTO>> getMeasurementConsumptionList() {

		List<MeasurementConsumptionDTO> measurementConsumptions = measurementConsumptionService.getMeasurementConsumptionList();

		return Optional.ofNullable(measurementConsumptions)
				.map(list -> {
					if (list.isEmpty()) {
						logger.warning("No measurement consumptions found.");
						return ResponseEntity.status(HttpStatus.NOT_FOUND).body(list);
					} else {
						logger.info("Retrieved measurement consumptions list: " + list.toString());
						return ResponseEntity.ok(list);
					}
				})
				.orElseGet(() -> {
					logger.warning("Internal server error while fetching measurement consumptions list.");
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				});
	}

	@GetMapping("/byDevice/{deviceId}")
	public ResponseEntity<List<MeasurementConsumptionDTO>> getAllMeasurementsForDevice(@PathVariable UUID deviceId) {
		List<MeasurementConsumptionDTO> measurements = measurementConsumptionService.getAllMeasurementsForDevice(deviceId);

		return measurements.isEmpty()
				? ResponseEntity.status(HttpStatus.NOT_FOUND).body(measurements)
				: ResponseEntity.ok(measurements);
	}


	@GetMapping("/year/month")
	public ResponseEntity<MeasurementConsumptionReport> getMeasurementConsumptionListForYearForMonth(
			@RequestParam(name = "year") int year,
			@RequestParam(name = "month") int month
	) {

		MeasurementConsumptionReport measurementConsumptionYearlyDTO =
				measurementConsumptionService.getMeasurementConsumptionListForYearForMonth(year, month);

		return Optional.ofNullable(measurementConsumptionYearlyDTO)
				.map(report -> {
					if (report.getMeasurementConsumptionList().isEmpty()) {
						logger.warning("No measurement consumptions found for year " + year + " and month " + month);
						return ResponseEntity.status(HttpStatus.NOT_FOUND).body(report);
					} else {
						logger.info("Retrieved measurement consumptions for year " + year + " and month " + month + ": " + report.toString());
						return ResponseEntity.ok(report);
					}
				})
				.orElseGet(() -> {
					logger.warning("Internal server error while fetching measurement consumptions for year " + year + " and month " + month);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				});
	}

	@GetMapping("/year/months")
	public ResponseEntity<Map<String, Double>> getMeasurementConsumptionListForYearByAllMonths(
			@RequestParam(name = "year") int year
	) {

		Map<String, Double> mapMeasurementByMonth =
				measurementConsumptionService.getMeasurementConsumptionListByYearForAllMonths(year);

		return Optional.ofNullable(mapMeasurementByMonth)
				.map(map -> {
					if (map.isEmpty()) {
						logger.warning("No measurement consumptions found for year " + year);
						return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
					} else {
						logger.info("Retrieved measurement consumptions for year " + year + ": " + map.toString());
						return ResponseEntity.ok(map);
					}
				})
				.orElseGet(() -> {
					logger.warning("Internal server error while fetching measurement consumptions for year " + year);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				});
	}

	@GetMapping("/{measurementId}")
	public ResponseEntity<?> getMeasurementConsumptionById(@PathVariable UUID measurementId) {

		return Optional.ofNullable(measurementConsumptionService.getMeasurementConsumptionById(measurementId))
				.map(measurementConsumptionDTO -> {
					logger.info("Retrieved measurement consumption by ID: " + measurementId.toString());
					return ResponseEntity.ok(measurementConsumptionDTO);
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PutMapping("/{measurementId}")
	public ResponseEntity<?> updateMeasurementConsumption(@PathVariable UUID measurementId, @RequestBody MeasurementConsumptionDTO measurementDTO) {

		MeasurementConsumptionDTO updatedMeasurement = measurementConsumptionService.updateMeasurementConsumption(measurementId, measurementDTO);
		logger.info("Measurement consumption updated: " + updatedMeasurement.toString());
		return ResponseEntity.ok(updatedMeasurement);
	}

	@DeleteMapping("/{measurementId}")
	public ResponseEntity<?> deleteMeasurementConsumption(@PathVariable UUID measurementId) {

		measurementConsumptionService.deleteMeasurementConsumption(measurementId);
		logger.info("Measurement consumption deleted: " + measurementId.toString());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}