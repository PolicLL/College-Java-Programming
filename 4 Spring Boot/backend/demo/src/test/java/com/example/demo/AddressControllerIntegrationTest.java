package com.example.demo;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.controller.AddressController;
import com.example.demo.model.Address;
import com.example.demo.service.AddressService;
import com.example.demo.utils.DTOUtils;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = Lab6Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class
})
public class AddressControllerIntegrationTest {


    @Autowired
    private AddressController addressController;

    @Autowired
    private AddressService addressService;

    @Autowired
    private DTOUtils dtoUtils;

    private int tempNumberOfAddressesInDatabase = 0;

    @BeforeEach
    public void setUp(){
        setTempNumberOfAddressesInDatabase();
    }

    private void setTempNumberOfAddressesInDatabase(){

        tempNumberOfAddressesInDatabase = addressService.getAddressList().size();
    }

    @Test
    public void testCreateAddress() {

        AddressDTO addressDTO = dtoUtils.getAddressDTO();
        addressDTO.setStreetName("Street Name 1");


        ResponseEntity<AddressDTO> responseEntity = (ResponseEntity<AddressDTO>) addressController.createAddress(addressDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Street Name 1", responseEntity.getBody().getStreetName());

        AddressDTO address = addressController.getAddress(responseEntity.getBody().getId()).getBody();
	    assert address != null;
	    assertEquals("Street Name 1", address.getStreetName());
    }


    @Test
    public void testGetAllAddresses() {

        addressService.createAddress(dtoUtils.getAddressDTO());
        addressService.createAddress(dtoUtils.getAddressDTO());

        List<AddressDTO> addressList = addressService.getAddressList();

	    assert addressList != null;
	    System.out.println("Address list size : " + addressList.size());

        assertNotNull(addressList);
        assertEquals(tempNumberOfAddressesInDatabase + 2, addressList.size());
    }

    @Test
    public void testGetAddressById() {
        AddressDTO addressDTO = dtoUtils.getAddressDTO();
        addressDTO.setStreetName("Street Name 1");

        AddressDTO newAddress = addressService.createAddress(addressDTO);
        AddressDTO retrievedAddress = addressController.getAddress(newAddress.getId()).getBody();

        assertNotNull(retrievedAddress);
        assertEquals("Street Name 1", retrievedAddress.getStreetName());
    }

    @Test
    public void testUpdateAddress() {

        AddressDTO newAddress = addressService.createAddress(dtoUtils.getAddressDTO());

        AddressDTO addressDTO = new AddressDTO("Updated Address", "Updated Postal Code", "Updated State");

        AddressDTO updateAddress = (AddressDTO) addressController.updateAddress(newAddress.getId(), addressDTO).getBody();

        assertNotNull(updateAddress);
        assertEquals("Updated Address", updateAddress.getStreetName());
        assertEquals("Updated Postal Code", updateAddress.getPostalCode());
        assertEquals("Updated State", updateAddress.getState());
    }

    @Test
    public void testDeleteAddress() {

        AddressDTO newAddress = addressService.createAddress(dtoUtils.getAddressDTO());

        HttpStatus status = (HttpStatus) addressController.deleteAddress(newAddress.getId()).getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, status);
    }
}