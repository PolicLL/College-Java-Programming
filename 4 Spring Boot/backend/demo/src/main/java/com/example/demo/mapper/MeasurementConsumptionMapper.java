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

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "device", ignore = true)
	@Mapping(target = "device.id", source = "deviceID")
	MeasurementConsumption toMeasurementConsumption(MeasurementConsumptionDTO measurementDTO);

	MeasurementConsumptionDTO toMeasurementConsumptionDTO(MeasurementConsumption measurementConsumption);

	default UUID mapDeviceID(DeviceDTO deviceDTO) {
		return deviceDTO != null ? deviceDTO.getId() : null;
	}
}
