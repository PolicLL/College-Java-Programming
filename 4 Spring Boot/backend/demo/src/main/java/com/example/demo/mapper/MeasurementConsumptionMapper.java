package com.example.demo.mapper;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.DTO.MeasurementConsumptionDTO;
import com.example.demo.model.measurement.MeasurementConsumption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Mapper(componentModel = "spring")
public interface MeasurementConsumptionMapper {

	MeasurementConsumptionMapper INSTANCE = Mappers.getMapper(MeasurementConsumptionMapper.class);

	@Mapping(target = "device.id", source = "deviceID")
	MeasurementConsumption toMeasurementConsumption(MeasurementConsumptionDTO measurementDTO);

	@Mapping(target = "deviceID", source = "device.id")
	MeasurementConsumptionDTO toMeasurementConsumptionDTO(MeasurementConsumption measurementConsumption);

}
