package com.example.demo.model;


import com.example.demo.DTO.AddressDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "state")
    private String state;

    public Address(String streetName, String postalCode, String state) {
        this.streetName = streetName;
        this.postalCode = postalCode;
        this.state = state;

        this.id = UUID.randomUUID();
    }

    public Address() {
        this.id = UUID.randomUUID();
    }

    public void updateUsingDTO(AddressDTO addressDTO) {
        if(addressDTO.getPostalCode() != null) this.setPostalCode(addressDTO.getPostalCode());
        if(addressDTO.getState() != null) this.setState(addressDTO.getState());
        if(addressDTO.getStreetName() != null) this.setStreetName(addressDTO.getStreetName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) ||  (Objects.equals(streetName, address.streetName) && Objects.equals(postalCode, address.postalCode) && Objects.equals(state, address.state));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, streetName, postalCode, state);
    }

    @Override
    public String toString() {
        return String.format("Street name : %s | Postal code : %s | State : %s",
                this.streetName, this.postalCode, this.state);
    }

}
