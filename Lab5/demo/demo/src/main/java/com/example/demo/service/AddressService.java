package com.example.demo.service;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.exception.AddressNotFoundException;
import com.example.demo.mapper.AddressMapper;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address createAddress(AddressDTO addressDTO) {
        Address newAddress = AddressMapper.toAddress(addressDTO);
        addressRepository.save(newAddress);
        return newAddress;
    }

    public List<Address> getAddressList() {
        return addressRepository.findAll();
    }

    public Optional<Address> getAddressById(UUID id) {
        return addressRepository.findById(id);
    }

    public Address updateAddress(UUID addressId, AddressDTO addressDTO) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);

        if (optionalAddress.isPresent()) {
            Address addressToUpdate = optionalAddress.get();

            addressToUpdate.updateUsingDTO(addressDTO);

            return addressRepository.save(addressToUpdate);
        } else {
            throw new AddressNotFoundException("There is no address with this id.");
        }
    }

    public void deleteAddress(UUID addressId) {
        addressRepository.deleteById(addressId);
    }

    public void deleteAll(){ addressRepository.deleteAll(); }
}