package com.example.demo.service;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.controller.AddressController;
import com.example.demo.exception.AddressNotFoundException;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.exception.response.ErrorResponse;
import com.example.demo.mapper.AddressMapper;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class AddressService {

	private final AddressRepository addressRepository;

	private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

	public AddressService(AddressRepository addressRepository) {

		this.addressRepository = addressRepository;
	}

	public Address createAddress(AddressDTO addressDTO) {
		Address newAddress = AddressMapper.toAddress(addressDTO);
		addressRepository.save(newAddress);
		return newAddress;
	}

	public Page<Address> getAddressPageWithSorting(int page, int size, String[] sort) {

		Sort.Direction direction = Sort.Direction.ASC;
		String sortBy = "id"; // Default sorting by ID and ascending

		if (sort.length > 0) {
			sortBy = sort[0]; // First element is the field name
			if (sort.length > 1) {
				direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
			}
		}

		return getAddressPage(PageRequest.of(page, size, Sort.by(direction, sortBy)));
	}

	public Page<Address> getAddressPage(Pageable pageable) {

		return addressRepository.findAll(pageable);
	}

	public List<Address> getAddressList() {

		return addressRepository.findAll();
	}

	public Address getAddressById(UUID id) {
		return requireAddress(id);
	}


	public Address updateAddress(UUID addressId, AddressDTO addressDTO) {
		Address address = requireAddress(addressId);
		address.updateUsingDTO(addressDTO);
		return addressRepository.save(address);
	}

	private Address requireAddress(UUID addressID){
		return addressRepository.findById(addressID)
				.orElseThrow(() -> new AddressNotFoundException(addressID));
	}

	public void deleteAddress(UUID addressId) {

		addressRepository.deleteById(addressId);
	}

	public void deleteAll() {

		addressRepository.deleteAll();
	}


}