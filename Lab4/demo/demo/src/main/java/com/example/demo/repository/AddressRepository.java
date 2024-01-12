package com.example.demo.repository;

import com.example.demo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    // Add custom query methods if needed
}