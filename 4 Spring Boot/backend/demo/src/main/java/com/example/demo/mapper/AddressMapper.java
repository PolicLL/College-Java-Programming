package com.example.demo.mapper;

import com.example.demo.DTO.AddressDTO;
import com.example.demo.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address toAddress(AddressDTO addressDTO);

    AddressDTO toAddressDTO(Address address);

    void updateAddressFromDTO(AddressDTO addressDTO, @org.mapstruct.MappingTarget Address address);

    // New method to map Page<Address> to Page<AddressDTO>
    default Page<AddressDTO> toPageAddressDTO(Page<Address> page) {
        return page.map(this::toAddressDTO);
    }
}