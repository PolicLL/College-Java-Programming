package com.example.demo.service;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.exception.DeviceNotFoundException;
import com.example.demo.exception.MeasurementForThisMonthInYearExistsException;
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

    public Device getDeviceById(UUID id){
        return retrieveDevice(id);
    }

    public Device retrieveDevice(UUID id){
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }

    // UPDATE

    public Device updateDevice(UUID deviceID, DeviceDTO deviceDTO) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceID);

        if(optionalDevice.isPresent()){
            Device deviceToUpdate = optionalDevice.get();

            deviceToUpdate.updateUsingDTO(deviceDTO);

            return deviceRepository.save(deviceToUpdate);
        }
        else
            throw new DeviceNotFoundException(deviceID);
    }

    // DELETE

    public void deleteDevice(UUID deviceId) {
        deviceRepository.deleteById(deviceId);
    }

    // OTHER


    public Device measureNow(UUID deviceID) {
        Device device = retrieveDevice(deviceID);

        device.measureConsumptionNow(1, 1000);

        return deviceRepository.save(device);
    }

    public void deleteAll(){ this.deviceRepository.deleteAll(); }


    public Device measureForMonth(UUID deviceID, int month, int year) {
        Device device = retrieveDevice(deviceID);

        if(!isThereMeasurementForMonthInYear(deviceID, month, year)){
            device.measureConsumptionForMonth(month, 1, 1000);
            return deviceRepository.save(device);
        }

        throw new MeasurementForThisMonthInYearExistsException(month);

    }

    public Device measureForDate(UUID deviceID, MeasurementConsumptionDTO requestDTO) {
        Device device = retrieveDevice(deviceID);

        int month = DateUtils.getMonthFromDate(requestDTO.getMeasurementDate());
        int year = DateUtils.getYearFromDate(requestDTO.getMeasurementDate());

        if(isThereMeasurementForMonthInYear(deviceID, month, year)){
            throw new MeasurementForThisMonthInYearExistsException(month);
        }

        device.createMeasurement(requestDTO, getDeviceById(deviceID));
        return deviceRepository.save(device);
    }


    public boolean isThereMeasurementForMonthInYear(UUID deviceID, int month, int year){
        Device device = retrieveDevice(deviceID);

        if(device.getConsumptionsHistory() == null) return false;

        for(MeasurementConsumption measurementConsumption : device.getConsumptionsHistory()){
            if(isDateInMonth(measurementConsumption.getMeasurementDate(), month) && isSameYear(measurementConsumption.getMeasurementDate(), year)){
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

    private boolean isSameYear(Date date, int year) {
        return DateUtils.getYearFromDate(date) == year;
    }

}