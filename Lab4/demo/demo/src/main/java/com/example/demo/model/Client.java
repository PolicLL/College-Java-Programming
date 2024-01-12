package com.example.demo.model;

import com.example.demo.DTO.ClientDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    public Client(String name, Address address, Device device) {
        this.name = name;
        this.address = address;
        this.device = device;

        this.id = UUID.randomUUID();
    }

    public void updateUsingDTO(ClientDTO clientDTO) {
        if(clientDTO.getName() != null) this.setName(clientDTO.getName());
        if(clientDTO.getAddress() != null) this.setAddress(clientDTO.getAddress());
        if(clientDTO.getDevice() != null) this.setDevice(clientDTO.getDevice());
    }

    @Override
    public String toString() {
        return String.format("ID : %s | Name : %s | Address : (%s) | Device : %s",
                this.id, this.name, this.address, this.device);
    }


}
