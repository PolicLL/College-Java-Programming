package com.example.demo.service;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.exception.DeviceNotFoundException;
import com.example.demo.exception.MeasurementForThisMonthAlreadyExistsException;
import com.example.demo.mapper.DeviceMapper;
import com.example.demo.model.Device;
import com.example.demo.model.measurement.MeasurementConsumption;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Optional<Device> optionalDevice = getDeviceById(deviceId);

        Device device = optionalDevice.orElseThrow(() -> new DeviceNotFoundException("Device with this id not found."));

        device.measureConsumptionNow(1, 1000);

        return deviceRepository.save(device);
    }

    public void deleteAll(){ this.deviceRepository.deleteAll(); }


    public Device measureForMonth(UUID deviceId, int month) {
        Optional<Device> optionalDevice = getDeviceById(deviceId);

        Device device = optionalDevice.orElseThrow(() -> new DeviceNotFoundException("Device with this id not found."));

        if(!isThereMeasurementForMonth(deviceId, month)){
            device.measureConsumptionForMonth(month, 1, 1000);
            return deviceRepository.save(device);
        }

        throw new MeasurementForThisMonthAlreadyExistsException(month);

    }

    public Device measureForDate(UUID deviceId, Date measurementDate) {
        Optional<Device> optionalDevice = getDeviceById(deviceId);

        Device device = optionalDevice.orElseThrow(() -> new DeviceNotFoundException("Device with this id not found."));

        int month = DateUtils.getMonthFromDate(measurementDate);

        if(isThereMeasurementForMonth(deviceId, month)){
            device.measureConsumptionForMonth(month, 1, 1000);
            return deviceRepository.save(device);
        }

        throw new MeasurementForThisMonthAlreadyExistsException(month);
    }

    public boolean isThereMeasurementForMonth(UUID deviceId, int month){
        Optional<Device> optionalDevice = getDeviceById(deviceId);
        Device device = optionalDevice.orElseThrow(() -> new DeviceNotFoundException("Device with this id not found."));

        if(device.getConsumptionsHistory() == null) return false;

        for(MeasurementConsumption measurementConsumption : device.getConsumptionsHistory()){
            if(isDateInMonth(measurementConsumption.getMeasurementDate(), month)){
                return true;
            }
        }

        return false;
    }


    private boolean isDateInMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dateMonth = calendar.get(Calendar.MONTH) + 1; // Calendar months are zero-based (0 - January, 1 - February, etc.)

        return dateMonth == month;
    }

}