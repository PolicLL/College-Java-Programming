-- Address Table
CREATE TABLE Address (
    id UUID PRIMARY KEY,
    street_name VARCHAR(255),
    postal_code VARCHAR(20),
    state VARCHAR(100)
);

-- Device Table
CREATE TABLE Device (
    id UUID PRIMARY KEY,
    name VARCHAR(255)
);

-- MeasurementConsumption Table
CREATE TABLE MeasurementConsumption (
    id UUID PRIMARY KEY,
    measurement_date TIMESTAMP,
    measuring_unit_energy_consumption VARCHAR(10),
    measurement_value DECIMAL,
    device_id UUID,
    FOREIGN KEY (device_id) REFERENCES Device(id)
);

-- Client Table
CREATE TABLE Client (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address_id UUID NOT NULL,
    device_id UUID NOT NULL,
    FOREIGN KEY (address_id) REFERENCES Address(id) ON DELETE CASCADE,
    FOREIGN KEY (device_id) REFERENCES Device(id) ON DELETE CASCADE
);

