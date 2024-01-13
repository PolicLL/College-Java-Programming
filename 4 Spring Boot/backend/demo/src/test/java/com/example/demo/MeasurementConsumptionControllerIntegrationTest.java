package com.example.demo;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.controller.MeasurementConsumptionController;
import com.example.demo.exception.MeasurementForThisMonthInYearExistsException;
import com.example.demo.model.Device;
import com.example.demo.model.measurement.MeasurementConsumption;
import com.example.demo.model.measurement.MeasuringUnitEnergyConsumption;
import com.example.demo.service.implementation.DeviceServiceImpl;
import com.example.demo.service.MeasurementConsumptionService;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = Lab6Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestExecutionListeners({
		DependencyInjectionTestExecutionListener.class,
		FlywayTestExecutionListener.class
})
public class MeasurementConsumptionControllerIntegrationTest {

	@Autowired
	private MeasurementConsumptionController measurementConsumptionController;

	@Autowired
	private MeasurementConsumptionService measurementConsumptionService;

	@Autowired
	private DeviceServiceImpl deviceServiceImpl;

	private int tempNumberOfMeasurementConsumptionsInDatabase = 0;

	private DeviceDTO tempDevice;

	private MeasurementConsumptionDTO measurementDTO;

	private Date currentDate;

	@BeforeEach
	public void setUp(){

		System.out.println("NUMBER of measurements BEFORE : " +
				measurementConsumptionController.getMeasurementConsumptionList().getBody().size());

		measurementConsumptionService.deleteAllMeasurementConsumptions();


		System.out.println("NUMBER of measurements AFTER : " +
				measurementConsumptionController.getMeasurementConsumptionList().getBody().size());

		tempDevice = deviceServiceImpl.getDeviceList().get(0);
		currentDate = Date.valueOf(java.time.LocalDate.now());
		measurementDTO = new MeasurementConsumptionDTO(currentDate, MeasuringUnitEnergyConsumption.kWh, 100.0);
	}

	private void setTempNumberOfMeasurementConsumptionsInDatabase(){
		tempNumberOfMeasurementConsumptionsInDatabase = measurementConsumptionController.getMeasurementConsumptionList().getBody().size();
	}

	@Test
	public void testCreateMeasurementConsumptionEndpoint() {
		ResponseEntity<?> responseEntity = measurementConsumptionController.createMeasurementConsumption(tempDevice.getId(), measurementDTO);

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	public void testGetMeasurementConsumptionListEndpoint() {
		measurementConsumptionController.createMeasurementConsumption(tempDevice.getId(), measurementDTO);

		ResponseEntity<List<MeasurementConsumption>> responseEntity = measurementConsumptionController.getMeasurementConsumptionList();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	public void testGetMeasurementConsumptionByIdEndpoint() {
		ResponseEntity<?> createResponse = measurementConsumptionController.createMeasurementConsumption(tempDevice.getId(), measurementDTO);
		assertNotNull(createResponse.getBody());

		UUID measurementId = ((MeasurementConsumption) createResponse.getBody()).getId();

		ResponseEntity<?> getByIdResponse = measurementConsumptionController.getMeasurementConsumptionById(measurementId);
		assertEquals(HttpStatus.OK, getByIdResponse.getStatusCode());
		assertNotNull(getByIdResponse.getBody());
	}

	@Test
	public void testUpdateMeasurementConsumptionEndpoint() {
		ResponseEntity<?> createResponse = measurementConsumptionController.createMeasurementConsumption(tempDevice.getId(), measurementDTO);
		assertNotNull(createResponse.getBody());

		UUID measurementId = ((MeasurementConsumption) createResponse.getBody()).getId();

		MeasurementConsumptionDTO updatedMeasurementDTO = new MeasurementConsumptionDTO(currentDate, MeasuringUnitEnergyConsumption.kWh, 5000.0);
		ResponseEntity<?> updateResponse = measurementConsumptionController.updateMeasurementConsumption(measurementId, updatedMeasurementDTO);
		assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
		assertNotNull(updateResponse.getBody());
	}

	@Test
	public void testDeleteMeasurementConsumptionEndpoint() {
		ResponseEntity<?> createResponse = measurementConsumptionController.createMeasurementConsumption(tempDevice.getId(), measurementDTO);

		assertNotNull(createResponse.getBody());

		UUID measurementId = ((MeasurementConsumption) createResponse.getBody()).getId();

		ResponseEntity<?> deleteResponse = measurementConsumptionController.deleteMeasurementConsumption(measurementId);
		assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
	}

	@Test
	public void testMeasurementConsumptionForMonthAlreadyExists() {
		ResponseEntity<?> createResponse = measurementConsumptionController.createMeasurementConsumption(tempDevice.getId(), measurementDTO);

		assertNotNull(createResponse.getBody());

		assertThrows(MeasurementForThisMonthInYearExistsException.class, () -> {
			measurementConsumptionController.createMeasurementConsumption(tempDevice.getId(), measurementDTO);
		});


	}



}