package com.example.demo;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.controller.AddressController;
import com.example.demo.model.Address;
import com.example.demo.service.AddressService;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = Lab4Application.class)
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

    private int tempNumberOfAddressesInDatabase = 0;

    @BeforeEach
    public void setUp(){
        setTempNumberOfAddressesInDatabase();
    }

    private void setTempNumberOfAddressesInDatabase(){
        tempNumberOfAddressesInDatabase = addressController.getAddressList().getBody().size();
    }

    @Test
    public void testCreateAddress() {

        AddressDTO addressDTO = new AddressDTO("Street Name 1", "Postal Code 1", "State 1");
        ResponseEntity<Address> responseEntity = (ResponseEntity<Address>) addressController.createAddress(addressDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Street Name 1", responseEntity.getBody().getStreetName());

        Address address = addressController.getAddress(responseEntity.getBody().getId()).getBody();
        assertEquals("Street Name 1", address.getStreetName());
    }


    @Test
    public void testGetAllAddresses() {

        addressService.createAddress(new AddressDTO("Street Name 1", "Postal Code 1", "State 1"));
        addressService.createAddress(new AddressDTO("Street Name 2", "Postal Code 2", "State 2"));

        List<Address> addressList = addressController.getAddressList().getBody();

        System.out.println("Address list size : " + addressList.size());

        assertNotNull(addressList);
        assertEquals(tempNumberOfAddressesInDatabase + 2, addressList.size());
    }

    @Test
    public void testGetAddressById() {

        Address newAddress = addressService.createAddress(new AddressDTO("Street Name 1", "Postal Code 1", "State 1"));
        Address gettedAddress = addressController.getAddress(newAddress.getId()).getBody();

        assertNotNull(gettedAddress);
        assertEquals("Street Name 1", gettedAddress.getStreetName());
    }

    @Test
    public void testUpdateAddress() {

        Address newAddress = addressService.createAddress(new AddressDTO("Street Name 1", "Postal Code 1", "State 1"));

        AddressDTO addressDTO = new AddressDTO("Updated Address", "Updated Postal Code", "Updated State");

        Address updateAddress = (Address) addressController.updateAddress(newAddress.getId(), addressDTO).getBody();

        assertNotNull(updateAddress);
        assertEquals("Updated Address", updateAddress.getStreetName());
        assertEquals("Updated Postal Code", updateAddress.getPostalCode());
        assertEquals("Updated State", updateAddress.getState());
    }

    @Test
    public void testDeleteAddress() {

        Address newAddress = addressService.createAddress(new AddressDTO("Street Name 1", "Postal Code 1", "State 1"));

        HttpStatus status = (HttpStatus) addressController.deleteAddress(newAddress.getId()).getStatusCode();

        assertEquals(HttpStatus.NO_CONTENT, status);
    }
}