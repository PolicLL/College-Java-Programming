package com.example.demo.mapper;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface DeviceMapper {

    DeviceMapper INSTANCE = Mappers.getMapper(DeviceMapper.class);

    @Mapping(target = "consumptionsHistory", ignore = true)
    Device toDevice(DeviceDTO deviceDTO);

    DeviceDTO toDeviceDTO(Device device);

    void updateDeviceFromDTO(DeviceDTO deviceDTO, @org.mapstruct.MappingTarget Device device);
}