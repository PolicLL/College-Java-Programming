package com.example.demo.controller;

import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.DTO.MeasurementConsumptionReport;
import com.example.demo.exception.ErrorResponse;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.MeasurementConsumptionNotFoundException;
import com.example.demo.model.measurement.MeasurementConsumption;
import com.example.demo.service.MeasurementConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/measurement-consumption")
public class MeasurementConsumptionController {

	private final MeasurementConsumptionService measurementConsumptionService;

	@Autowired
	public MeasurementConsumptionController(MeasurementConsumptionService measurementConsumptionService) {
		this.measurementConsumptionService = measurementConsumptionService;
	}

	@PostMapping("/{deviceId}")
	public ResponseEntity<?> createMeasurementConsumption(@PathVariable UUID deviceId, @RequestBody MeasurementConsumptionDTO measurementDTO) {
		try {
			MeasurementConsumption createdMeasurement = measurementConsumptionService.createMeasurementConsumption(measurementDTO, deviceId);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdMeasurement);
		} catch (InvalidInputException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	@GetMapping
	public ResponseEntity<List<MeasurementConsumption>> getMeasurementConsumptionList() {
		List<MeasurementConsumption> measurementConsumptions = measurementConsumptionService.getMeasurementConsumptionList();
		if (measurementConsumptions.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(measurementConsumptions);
		} else {
			return ResponseEntity.ok(measurementConsumptions);
		}
	}

	@GetMapping("/year")
	public ResponseEntity<MeasurementConsumptionReport> getMeasurementConsumptionListForYear(
			@RequestParam(name = "year") int year
	) {
		MeasurementConsumptionReport measurementConsumptionYearlyDTO =
				measurementConsumptionService.getMeasurementConsumptionListForYear(year);

		if (measurementConsumptionYearlyDTO.getMeasurementConsumptionList().isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(measurementConsumptionYearlyDTO);
		} else {
			return ResponseEntity.ok(measurementConsumptionYearlyDTO);
		}
	}


	@GetMapping("/year/month")
	public ResponseEntity<MeasurementConsumptionReport> getMeasurementConsumptionListForYearForMonth(
			@RequestParam(name = "year") int year,
			@RequestParam(name = "month") int month
	) {
		MeasurementConsumptionReport measurementConsumptionYearlyDTO
				= measurementConsumptionService.getMeasurementConsumptionListForYearForMonth(year, month);

		if (measurementConsumptionYearlyDTO.getMeasurementConsumptionList().isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(measurementConsumptionYearlyDTO);
		} else {
			return ResponseEntity.ok(measurementConsumptionYearlyDTO);
		}
	}

	@GetMapping("/year/months")
	public ResponseEntity<Map<String, Double>> getMeasurementConsumptionListForYearByAllMonths(
			@RequestParam(name = "year") int year
	) {

		Map<String, Double> mapMeasurementByMonth =
				measurementConsumptionService.getMeasurementConsumptionListByYearForAllMonths(year);

		if (mapMeasurementByMonth == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapMeasurementByMonth);
		} else {
			return ResponseEntity.ok(mapMeasurementByMonth);
		}
	}




	@GetMapping("/{measurementId}")
	public ResponseEntity<?> getMeasurementConsumptionById(@PathVariable UUID measurementId) {
		Optional<MeasurementConsumption> measurementConsumption = measurementConsumptionService.getMeasurementConsumptionById(measurementId);
		return measurementConsumption.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PutMapping("/{measurementId}")
	public ResponseEntity<?> updateMeasurementConsumption(@PathVariable UUID measurementId, @RequestBody MeasurementConsumptionDTO measurementDTO) {
		try {
			MeasurementConsumption updatedMeasurement = measurementConsumptionService.updateMeasurementConsumption(measurementId, measurementDTO);
			return ResponseEntity.ok(updatedMeasurement);
		} catch (MeasurementConsumptionNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	@DeleteMapping("/{measurementId}")
	public ResponseEntity<?> deleteMeasurementConsumption(@PathVariable UUID measurementId) {
		try {
			measurementConsumptionService.deleteMeasurementConsumption(measurementId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (MeasurementConsumptionNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllMeasurementConsumptions() {
		measurementConsumptionService.deleteAllMeasurementConsumptions();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
