package com.example.demo.mapper;

import com.example.demo.DTO.ClientDTO;
import com.example.demo.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "device", ignore = true)
    Client toClient(ClientDTO clientDTO);

    ClientDTO toClientDTO(Client client);

    void updateClientFromDTO(ClientDTO clientDTO, @org.mapstruct.MappingTarget Client client);
}