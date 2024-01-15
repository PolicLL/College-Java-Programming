package com.example.demo.service.implementation;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.exception.AddressNotFoundException;
import com.example.demo.mapper.AddressMapper;
import com.example.demo.model.Address;
import com.example.demo.repository.AddressRepository;
import com.example.demo.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;
	private final AddressMapper addressMapper;

	private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

	public AddressDTO createAddress(AddressDTO addressDTO) {

		logger.info("Creating new address: {}", addressDTO);
		Address newAddress = addressMapper.toAddress(addressDTO);
		addressRepository.save(newAddress);
		logger.info("Address created successfully: {}", newAddress);
		return addressMapper.toAddressDTO(newAddress);
	}

	public Page<AddressDTO> getAddressPageWithSorting(int page, int size, String[] sort) {

		Sort.Direction direction = Sort.Direction.ASC;
		String sortBy = "id"; // Default sorting by ID and ascending

		if (sort.length > 0) {
			sortBy = sort[0]; // First element is the field name
			if (sort.length > 1) {
				direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
			}
		}

		logger.info("Fetching paginated addresses with sorting. Page: {}, Size: {}, SortBy: {}, Direction: {}",
				page, size, sortBy, direction);

		Page<AddressDTO> result = getAddressPage(PageRequest.of(page, size, Sort.by(direction, sortBy)));

		logger.info("Fetched {} addresses successfully", result.getTotalElements());
		return result;
	}

	public Page<AddressDTO> getAddressPage(Pageable pageable) {

		logger.info("Fetching paginated addresses with Pageable: {}", pageable);

		Page<Address> result = addressRepository.findAll(pageable);
		Page<AddressDTO> resultDTO = AddressMapper.INSTANCE.toPageAddressDTO(result);

		logger.info("Fetched {} addresses successfully", result.getTotalElements());
		return resultDTO;
	}


	public List<AddressDTO> getAddressList() {

		logger.info("Fetching all addresses");

		List<Address> result = addressRepository.findAll();
		List<AddressDTO> resultDTO = result.stream()
				.map(AddressMapper.INSTANCE::toAddressDTO)
				.collect(Collectors.toList());

		logger.info("Fetched {} addresses successfully", resultDTO.size());
		return resultDTO;
	}


	public AddressDTO getAddressById(UUID id) {

		logger.info("Fetching address by ID: {}", id);
		Address result = requireAddress(id);
		logger.info("Fetched address successfully: {}", result);
		return addressMapper.toAddressDTO(result);
	}

	public AddressDTO updateAddress(UUID addressId, AddressDTO addressDTO) {

		logger.info("Updating address with ID {}: {}", addressId, addressDTO);
		Address existingAddress = requireAddress(addressId);
		Address updatedAddress = addressMapper.toAddress(addressDTO);
		updatedAddress.setId(existingAddress.getId());
		logger.info("Address updated successfully: {}", updatedAddress);
		return addressMapper.toAddressDTO(updatedAddress);
	}

	private Address requireAddress(UUID addressID) {

		logger.info("Fetching address with ID: {}", addressID);
		Address result = addressRepository.findById(addressID)
				.orElseThrow(() -> {
					logger.error("Address with ID {} not found", addressID);
					return new AddressNotFoundException(addressID);
				});
		logger.info("Fetched address successfully: {}", result);
		return result;
	}

	public void deleteAddress(UUID addressId) {

		logger.info("Deleting address with ID: {}", addressId);
		addressRepository.deleteById(addressId);
		logger.info("Address deleted successfully");
	}

	public void deleteAll() {

		logger.info("Deleting all addresses");
		addressRepository.deleteAll();
		logger.info("All addresses deleted successfully");
	}

}
