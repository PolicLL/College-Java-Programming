package com.example.demo.utils;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.DTO.ClientDTO;
import com.example.demo.DTO.DeviceDTO;
import com.example.demo.service.AddressService;
import com.example.demo.service.ClientService;
import com.example.demo.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
public class DTOUtils {

	private final ClientService clientService;
	private final DeviceService deviceService;
	private final AddressService addressService;

	public DTOUtils(ClientService clientService, DeviceService deviceService, AddressService addressService) {

		this.clientService = clientService;
		this.deviceService = deviceService;
		this.addressService = addressService;
	}

	private int addressNumber = 0;
	private int deviceNumber = 0;

	public ClientDTO getClientDTO(){
		AddressDTO createdAddressDTO = addressService.createAddress(getAddressDTO());
		DeviceDTO createdDeviceDTO = deviceService.createDevice(getDeviceDTO());

		return new ClientDTO("Name 1", createdAddressDTO, createdDeviceDTO);
	}

	public AddressDTO getAddressDTO(){
		++addressNumber;
		return new AddressDTO("Street Name " + addressNumber, "Postal Code " + addressNumber, "State " + addressNumber);
	}

	public DeviceDTO getDeviceDTO(){
		++deviceNumber;
		return new DeviceDTO("Device Name " + deviceNumber);
	}

}
