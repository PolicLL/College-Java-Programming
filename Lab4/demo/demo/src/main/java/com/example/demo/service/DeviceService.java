package com.example.demo.service;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.exception.DeviceNotFoundException;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.mapper.DeviceMapper;
import com.example.demo.model.Client;
import com.example.demo.model.Device;
import com.example.demo.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    // CREATE

    public Device createDevice(DeviceDTO deviceDTO) {
        Device newDevice = DeviceMapper.toDevice(deviceDTO);
        deviceRepository.save(newDevice);
        return newDevice;
    }


    // READ

    public List<Device> getDeviceList(){
        return this.deviceRepository.findAll();
    }

    public Optional<Device> getDeviceById(UUID id){
        return deviceRepository.findById(id);
    }


    // UPDATE

    public Device updateDevice(UUID deviceId, DeviceDTO deviceDTO) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);

        if(optionalDevice.isPresent()){
            Device deviceToUpdate = optionalDevice.get();

            deviceToUpdate.updateUsingDTO(deviceDTO);

            return deviceRepository.save(deviceToUpdate);
        }
        else
            throw new DeviceNotFoundException("Device with this id not found.");
    }

    // DELETE

    public void deleteDevice(UUID deviceId) {
        deviceRepository.deleteById(deviceId);
    }

    // OTHER


    public Device measureNow(UUID deviceId) {
        return null;
    }

    public void deleteAll(){ this.deviceRepository.deleteAll(); }









}