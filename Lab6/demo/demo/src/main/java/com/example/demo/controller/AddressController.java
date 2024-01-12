package com.example.demo.controller;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.exception.AddressNotFoundException;
import com.example.demo.exception.response.ErrorResponse;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.model.Address;
import com.example.demo.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

	private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

	private final AddressService addressService;

	@PostMapping
	public ResponseEntity<?> createAddress(@RequestBody AddressDTO addressDTO) {

		try {
			Address createdAddress = addressService.createAddress(addressDTO);
			logger.info("Address created successfully with ID: {}", createdAddress.getId());
			return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
		} catch (InvalidInputException e) {
			logger.error("Invalid input for creating address: {}", e.getMessage());
			ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	@GetMapping()
	public ResponseEntity<Page<Address>> getAddressList(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size,
			@RequestParam(defaultValue = "id,asc") String[] sort
	) {

		logger.info("Fetching address list with page={}, size={}, sort={}", page, size, String.join(",", sort));
		Page<Address> addresses = addressService.getAddressPageWithSorting(page, size, sort);

		if (addresses.isEmpty()) {
			logger.warn("No addresses found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(addresses);
		} else {
			logger.info("Returning {} addresses", addresses.getTotalElements());
			return ResponseEntity.ok(addresses);
		}
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<Address> getAddress(@PathVariable UUID addressId) {

		logger.info("Fetching address with ID: {}", addressId);
		Optional<Address> address = addressService.getAddressById(addressId);

		if (address.isEmpty()) {
			logger.warn("Address not found with ID: {}", addressId);
			throw new AddressNotFoundException(addressId);
		}

		return address.map(tempAddress -> {
			logger.info("Returning address with ID: {}", addressId);
			return ResponseEntity.ok().body(tempAddress);
		}).orElseGet(() -> {
			logger.warn("Address not found with ID: {}", addressId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		});
	}

	@PutMapping("/{addressId}")
	public ResponseEntity<?> updateAddress(@PathVariable UUID addressId, @RequestBody AddressDTO addressDTO) {

		try {
			Address updatedAddress = addressService.updateAddress(addressId, addressDTO);
			logger.info("Address updated successfully with ID: {}", addressId);
			return ResponseEntity.ok(updatedAddress);
		} catch (AddressNotFoundException addressNotFoundException) {
			logger.warn("Address not found for update with ID: {}", addressId);
			ErrorResponse errorResponse = new ErrorResponse(addressNotFoundException.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<?> deleteAddress(@PathVariable UUID addressId) {

		try {
			addressService.deleteAddress(addressId);
			logger.info("Address deleted successfully with ID: {}", addressId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (AddressNotFoundException addressNotFoundException) {
			logger.warn("Address not found for deletion with ID: {}", addressId);
			ErrorResponse errorResponse = new ErrorResponse(addressNotFoundException.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

}
