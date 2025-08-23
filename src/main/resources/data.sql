-- Datos iniciales para la base de datos

-- Insertar roles de usuario
INSERT INTO role (name) VALUES ('ADMIN');
INSERT INTO role (name) VALUES ('USER');
INSERT INTO role (name) VALUES ('MANAGER');

-- Insertar usuario administrador por defecto
INSERT INTO user (email, password, role) VALUES ('admin@ecommerce.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ROLE_ADMIN');

-- Insertar cliente de ejemplo
INSERT INTO customer (identification_document_id, identification, names, address, legal_organization_id, tribute_id, municipality_id) 
VALUES (1, '12345678', 'Cliente Ejemplo', 'Dirección Ejemplo', 'ORG001', 'TRIB001', 'MUN001');

-- Insertar productos de ejemplo
INSERT INTO item (code_reference, name, quantity, discount_rate, price, tax_rate, unit_measure_id, standard_code_id, is_excluded, tribute_id) 
VALUES ('PROD001', 'Laptop HP', 10, '0.05', 999.99, '0.13', 1, 1, 0, 1);

INSERT INTO item (code_reference, name, quantity, discount_rate, price, tax_rate, unit_measure_id, standard_code_id, is_excluded, tribute_id) 
VALUES ('PROD002', 'Mouse Inalámbrico', 50, '0.10', 29.99, '0.13', 1, 1, 0, 1);

INSERT INTO item (code_reference, name, quantity, discount_rate, price, tax_rate, unit_measure_id, standard_code_id, is_excluded, tribute_id) 
VALUES ('PROD003', 'Teclado Mecánico', 25, '0.15', 89.99, '0.13', 1, 1, 0, 1);
