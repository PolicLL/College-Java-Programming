package com.example.demo.mapper;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.model.Address;

import java.util.Optional;

public class AddressMapper {

    public static AddressDTO toAddressRequest(Address address) {
        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setStreetName(address.getStreetName());
        addressDTO.setPostalCode(address.getPostalCode());
        addressDTO.setState(address.getState());
        // Map other fields if needed
        return addressDTO;
    }

    public static Address toAddress(AddressDTO addressRequest) {
        Address address = new Address();

        Optional.ofNullable(addressRequest.getPostalCode()).ifPresent(address::setPostalCode);
        Optional.ofNullable(addressRequest.getState()).ifPresent(address::setState);
        Optional.ofNullable(addressRequest.getStreetName()).ifPresent(address::setStreetName);

        return address;
    }
}