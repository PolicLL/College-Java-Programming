package com.example.demo.h2;

import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.Lab6Application;
import com.example.demo.controller.MeasurementConsumptionController;
import com.example.demo.exception.MeasurementForThisMonthInYearExistsException;
import com.example.demo.service.MeasurementConsumptionService;
import com.example.demo.utils.DTOUtils;
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

import java.util.List;
import java.util.Objects;
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
	private DTOUtils dtoUtils;

	private int tempNumberOfMeasurementConsumptionsInDatabase = 0;


	@BeforeEach
	public void setUp(){
		measurementConsumptionService.deleteAllMeasurementConsumptions();

		setTempNumberOfMeasurementConsumptionsInDatabase();
	}

	private void setTempNumberOfMeasurementConsumptionsInDatabase(){
		tempNumberOfMeasurementConsumptionsInDatabase = Objects.requireNonNull(measurementConsumptionController.getMeasurementConsumptionList().getBody()).size();
	}

	private ResponseEntity<?> createMeasurementConsumption(){
		return  measurementConsumptionController.createMeasurementConsumption(dtoUtils.getCreatedDevice().getId(), dtoUtils.getMeasurementConsumptionDTO());
	}

	private ResponseEntity<?> createMeasurementConsumption(UUID id, MeasurementConsumptionDTO measurementConsumptionDTO){
		return  measurementConsumptionController.createMeasurementConsumption(id, measurementConsumptionDTO);
	}


	@Test
	public void testCreateMeasurementConsumptionEndpoint() {
		ResponseEntity<?> responseEntity = createMeasurementConsumption();

		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	public void testGetMeasurementConsumptionListEndpoint() {
		createMeasurementConsumption();

		ResponseEntity<List<MeasurementConsumptionDTO>> responseEntity = measurementConsumptionController.getMeasurementConsumptionList();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
	}

	@Test
	public void testGetMeasurementConsumptionByIdEndpoint() {
		ResponseEntity<?> createResponse = createMeasurementConsumption();
		assertNotNull(createResponse.getBody());

		UUID measurementId = ((MeasurementConsumptionDTO) createResponse.getBody()).getId();

		ResponseEntity<?> getByIdResponse = measurementConsumptionController.getMeasurementConsumptionById(measurementId);
		assertEquals(HttpStatus.OK, getByIdResponse.getStatusCode());
		assertNotNull(getByIdResponse.getBody());
	}

	@Test
	public void testUpdateMeasurementConsumptionEndpoint() {
		ResponseEntity<?> createResponse = createMeasurementConsumption();
		assertNotNull(createResponse.getBody());

		UUID measurementId = ((MeasurementConsumptionDTO) createResponse.getBody()).getId();

		MeasurementConsumptionDTO updatedMeasurementDTO = dtoUtils.getMeasurementConsumptionDTO();
		ResponseEntity<?> updateResponse = measurementConsumptionController.updateMeasurementConsumption(measurementId, updatedMeasurementDTO);
		assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
		assertNotNull(updateResponse.getBody());
	}

	@Test
	public void testDeleteMeasurementConsumptionEndpoint() {
		ResponseEntity<?> createResponse = createMeasurementConsumption();

		assertNotNull(createResponse.getBody());

		UUID measurementId = ((MeasurementConsumptionDTO) createResponse.getBody()).getId();

		ResponseEntity<?> deleteResponse = measurementConsumptionController.deleteMeasurementConsumption(measurementId);
		assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
	}

	@Test
	public void testMeasurementConsumptionForMonthAlreadyExists() {
		MeasurementConsumptionDTO createResponse = (MeasurementConsumptionDTO) createMeasurementConsumption().getBody();

		assertNotNull(createResponse);

		MeasurementConsumptionDTO newDTO = new MeasurementConsumptionDTO();
		newDTO.setMeasurementDate(createResponse.getMeasurementDate());

		assertThrows(MeasurementForThisMonthInYearExistsException.class, () -> {
			createMeasurementConsumption(createResponse.getDeviceID(), newDTO);
		});


	}



}
