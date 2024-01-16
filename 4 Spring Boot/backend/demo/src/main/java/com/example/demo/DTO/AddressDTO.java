package com.example.demo.DTO;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressDTO {
    private UUID id;
    private String streetName;
    private String postalCode;
    private String state;

    public AddressDTO(String streetName, String postalCode, String state) {

        this.streetName = streetName;
        this.postalCode = postalCode;
        this.state = state;
    }

}
