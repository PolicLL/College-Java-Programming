INSERT INTO Address (id, street_name, postal_code, state)
VALUES
  ('d4e710ee-2e19-4c79-a5aa-74be2b93db01', '123 Main St', '12345', 'California'),
  ('5e54d65d-46b1-471f-ba32-f0d6e45d9a63', '456 Elm St', '23456', 'New York'),
  ('3a5b5e34-0fe5-4f6e-826d-8a6011f1b9b8', '789 Oak St', '34567', 'Texas'),
  ('bfe42035-8a37-4d21-9924-5412f136f3ab', '101 Pine St', '45678', 'Florida'),
  ('f4d76b09-f647-48b6-b7f6-d2ee1051986e', '210 Maple St', '56789', 'Washington'),
  ('e0e35f9c-76ae-4224-b5c3-42075de207c3', '555 Cedar St', '67890', 'Illinois'),
  ('9dca0943-ee83-4e69-8c89-7ff0b2d15cf1', '777 Birch St', '78901', 'Arizona'),
  ('c3f7a1ff-3e0f-42a6-aa7a-88138a724746', '888 Walnut St', '89012', 'Massachusetts'),
  ('78a90aa4-5a7b-4e5d-82f8-6a10094dbb8d', '999 Cherry St', '90123', 'Colorado'),
  ('246f759c-7ed8-47c0-a7fc-6a6cf219ad5a', '222 Spruce St', '01234', 'Oregon');

INSERT INTO Device (id, name)
VALUES
  ('2f2a71c4-7c17-45d8-96c5-87b823b87e98', 'Device1'),
  ('3e8f2ef1-9d8e-4f1d-ae2c-0d430289634d', 'Device2'),
  ('4d2856a1-98b2-49cb-a09a-7e8e8c3a2f88', 'Device3'),
  ('5c9d2684-3f3a-4b2a-8fb8-2b4e96f02b47', 'Device4'),
  ('6a1e23c5-2c82-4f3b-89f5-43710d9fcfc2', 'Device5');

-- INSERT statements for Measurement_Consumption table
INSERT INTO Measurement_Consumption (id, measurement_date, measuring_unit_energy_consumption, measurement_value, device_id) VALUES
  ('c6b8d9f5-42d9-4d4a-80c7-9f47a4853e54', '2023-01-15 08:00:00', 'kWh', 50.3, '2f2a71c4-7c17-45d8-96c5-87b823b87e98'),
  ('b287c7c2-7bfa-4327-8995-18f6f6be22bc', '2023-02-20 10:30:00', 'kWh', 45.8, '3e8f2ef1-9d8e-4f1d-ae2c-0d430289634d'),
  ('d8f5b4a6-6a87-4b3f-a051-3a030aa08789', '2023-03-10 12:15:00', 'kWh', 60.2, '4d2856a1-98b2-49cb-a09a-7e8e8c3a2f88'),
  ('f0e32a67-cbcd-4342-afe6-71912e4567d1', '2023-04-05 14:45:00', 'kWh', 55.6, '5c9d2684-3f3a-4b2a-8fb8-2b4e96f02b47'),
  ('a9c5f2d3-3c74-4090-8d9d-71b218ebc34c', '2023-05-18 16:20:00', 'kWh', 48.9, '6a1e23c5-2c82-4f3b-89f5-43710d9fcfc2');