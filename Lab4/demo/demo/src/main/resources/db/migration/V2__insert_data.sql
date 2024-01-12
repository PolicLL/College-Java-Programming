CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


INSERT INTO Address (id, street_name, postal_code, state)
VALUES
  (uuid_generate_v4(), '123 Main St', '12345', 'California'),
  (uuid_generate_v4(), '456 Elm St', '23456', 'New York'),
  (uuid_generate_v4(), '789 Oak St', '34567', 'Texas'),
  (uuid_generate_v4(), '101 Pine St', '45678', 'Florida'),
  (uuid_generate_v4(), '210 Maple St', '56789', 'Washington'),
  (uuid_generate_v4(), '555 Cedar St', '67890', 'Illinois'),
  (uuid_generate_v4(), '777 Birch St', '78901', 'Arizona'),
  (uuid_generate_v4(), '888 Walnut St', '89012', 'Massachusetts'),
  (uuid_generate_v4(), '999 Cherry St', '90123', 'Colorado'),
  (uuid_generate_v4(), '222 Spruce St', '01234', 'Oregon');
