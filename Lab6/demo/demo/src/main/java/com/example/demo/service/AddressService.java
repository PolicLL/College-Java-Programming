package com.example.demo.service;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.exception.AddressNotFoundException;
import com.example.demo.mapper.AddressMapper;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
			throw new AddressNotFoundException(addressId);
		}
	}

	public void deleteAddress(UUID addressId) {

		addressRepository.deleteById(addressId);
	}

	public void deleteAll() {

		addressRepository.deleteAll();
	}


}