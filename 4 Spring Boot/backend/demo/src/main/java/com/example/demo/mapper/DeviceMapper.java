package com.example.demo.mapper;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.model.Device;

public class DeviceMapper {

    public static Device toDevice(DeviceDTO deviceRequest) {
        return new Device(deviceRequest.getName());
    }
}