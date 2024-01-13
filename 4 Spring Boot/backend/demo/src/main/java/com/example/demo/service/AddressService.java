package com.example.demo.service;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AddressService {

	AddressDTO createAddress(AddressDTO addressDTO);

	Page<AddressDTO> getAddressPageWithSorting(int page, int size, String[] sort);

	Page<AddressDTO> getAddressPage(Pageable pageable);

	List<AddressDTO> getAddressList();

	AddressDTO getAddressById(UUID id);

	AddressDTO updateAddress(UUID addressId, AddressDTO addressDTO);

	void deleteAddress(UUID addressId);

	void deleteAll();
}
