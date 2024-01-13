package com.example.demo;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.controller.DeviceController;
import com.example.demo.model.Device;
import com.example.demo.service.implementation.DeviceServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = Lab6Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class
})
public class DeviceControllerIntegrationTest {


    @Autowired
    private DeviceServiceImpl deviceServiceImpl;

    @Autowired
    private DeviceController deviceController;

    @BeforeEach
    public void setUp(){
        deviceServiceImpl.deleteAll();
    }

    @Test
    public void testCreateDevice() {
        DeviceDTO deviceDTO = new DeviceDTO("Device 1");
        ResponseEntity<Device> responseEntity = (ResponseEntity<Device>) deviceController.createDevice(deviceDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Device 1", responseEntity.getBody().getName());

        List<DeviceDTO> deviceList = deviceServiceImpl.getDeviceList();
        assertEquals(1, deviceList.size());
        assertEquals("Device 1", deviceList.get(0).getName());

    }


    @Test
    public void testGetAllDevices() {
        deviceServiceImpl.createDevice(new DeviceDTO("Device 1"));
        deviceServiceImpl.createDevice(new DeviceDTO("Device 2"));

        List<DeviceDTO> deviceList = deviceController.getDeviceList().getBody();

        assertNotNull(deviceList);
        assertEquals(2, deviceList.size());
        assertEquals("Device 1", deviceList.get(0).getName());
    }



    @Test
    public void testGetDeviceById() {
        DeviceDTO newDevice = deviceServiceImpl.createDevice(new DeviceDTO("Device 1"));
        DeviceDTO gotDevice = deviceController.getDevice(newDevice.getId()).getBody();

        assertNotNull(gotDevice);
        assertEquals("Device 1", gotDevice.getName());
    }



    @Test
    public void testUpdateDevice() {
        DeviceDTO newDevice = deviceServiceImpl.createDevice(new DeviceDTO("Device 1"));

        DeviceDTO deviceDTO = new DeviceDTO("Updated Name");

        Device updateDevice = (Device) deviceController.updateDevice(newDevice.getId(), deviceDTO).getBody();

        assertNotNull(updateDevice);
        assertEquals("Updated Name", updateDevice.getName());
    }


    @Test
    public void testDeleteDevice() {
        DeviceDTO newDevice = deviceServiceImpl.createDevice(new DeviceDTO("Device 1"));

        HttpStatus status = (HttpStatus) deviceController.deleteDevice(newDevice.getId()).getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, status);
    }

}