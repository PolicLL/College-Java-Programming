package com.example.demo.controller;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.exception.AddressNotFoundException;
import com.example.demo.exception.response.ErrorResponse;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.model.Address;
import com.example.demo.service.AddressService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/address")
@AllArgsConstructor
public class AddressController {

	private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

	private final AddressService addressService;

	@PostMapping
	public ResponseEntity<?> createAddress(@RequestBody AddressDTO addressDTO) {

		AddressDTO createdAddress = addressService.createAddress(addressDTO);
		logger.info("Address created successfully with ID: {}", createdAddress.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
	}

	@GetMapping()
	public ResponseEntity<Page<AddressDTO>> getAddressList(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size,
			@RequestParam(defaultValue = "id,asc") String[] sort
	) {

		logger.info("Fetching address list with page={}, size={}, sort={}", page, size, String.join(",", sort));

		Page<AddressDTO> addresses = addressService.getAddressPageWithSorting(page, size, sort);

		return addresses.getContent().stream()
				.findFirst()
				.map(address -> {
					logger.info("Returning {} addresses", addresses.getTotalElements());
					return ResponseEntity.ok(addresses);
				})
				.orElseGet(() -> {
					logger.warn("No addresses found");
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(addresses);
				});
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<AddressDTO> getAddress(@PathVariable UUID addressId) {

		logger.info("Returning address with ID: {}", addressId);

		return Optional.ofNullable(addressService.getAddressById(addressId))
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}


	@PutMapping("/{addressId}")
	public ResponseEntity<?> updateAddress(@PathVariable UUID addressId, @RequestBody AddressDTO addressDTO) {

		AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);
		logger.info("Address updated successfully with ID: {}", addressId);
		return ResponseEntity.ok(updatedAddress);
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<?> deleteAddress(@PathVariable UUID addressId) {

		addressService.deleteAddress(addressId);
		logger.info("Address deleted successfully with ID: {}", addressId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
