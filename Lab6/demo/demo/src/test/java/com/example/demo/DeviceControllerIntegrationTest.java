package com.example.demo;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.controller.DeviceController;
import com.example.demo.model.Device;
import com.example.demo.service.DeviceService;
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
    private DeviceService deviceService;

    @Autowired
    private DeviceController deviceController;

    @BeforeEach
    public void setUp(){
        deviceService.deleteAll();
    }

    @Test
    public void testCreateDevice() {
        DeviceDTO deviceDTO = new DeviceDTO("Device 1");
        ResponseEntity<Device> responseEntity = (ResponseEntity<Device>) deviceController.createDevice(deviceDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Device 1", responseEntity.getBody().getName());

        List<Device> deviceList = deviceService.getDeviceList();
        assertEquals(1, deviceList.size());
        assertEquals("Device 1", deviceList.get(0).getName());

    }


    @Test
    public void testGetAllDevices() {
        deviceService.createDevice(new DeviceDTO("Device 1"));
        deviceService.createDevice(new DeviceDTO("Device 2"));

        List<Device> deviceList = deviceController.getDeviceList().getBody();

        assertNotNull(deviceList);
        assertEquals(2, deviceList.size());
        assertEquals("Device 1", deviceList.get(0).getName());
    }



    @Test
    public void testGetDeviceById() {
        Device newDevice = deviceService.createDevice(new DeviceDTO("Device 1"));
        Device gettedDevice = deviceController.getDevice(newDevice.getId()).getBody();

        assertNotNull(gettedDevice);
        assertEquals("Device 1", gettedDevice.getName());
    }



    @Test
    public void testUpdateDevice() {
        Device newDevice = deviceService.createDevice(new DeviceDTO("Device 1"));

        DeviceDTO deviceDTO = new DeviceDTO("Updated Name");

        Device updateDevice = (Device) deviceController.updateDevice(newDevice.getId(), deviceDTO).getBody();

        assertNotNull(updateDevice);
        assertEquals("Updated Name", updateDevice.getName());
    }


    @Test
    public void testDeleteDevice() {
        Device newDevice = deviceService.createDevice(new DeviceDTO("Device 1"));

        HttpStatus status = (HttpStatus) deviceController.deleteDevice(newDevice.getId()).getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, status);
    }

}