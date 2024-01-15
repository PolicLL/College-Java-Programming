package com.example.demo.DTO;

import com.example.demo.model.Device;

import com.example.demo.model.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClientDTO {
    private UUID id;
    private String name;
    private AddressDTO address;
    private DeviceDTO device;

    public ClientDTO(String name, AddressDTO address, DeviceDTO device) {
        this.name = name;
        this.address = address;
        this.device = device;
    }
}