package com.example.demo.repository;

import com.example.demo.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    // Add custom query methods if needed
}