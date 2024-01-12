package com.example.lab6secondpart.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Device {
	private UUID id;
	private String name;
	private Client client;
}
