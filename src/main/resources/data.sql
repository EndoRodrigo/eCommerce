-- Datos iniciales para roles de usuario
INSERT INTO role (id, name) VALUES (1, 'ADMIN') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO role (id, name) VALUES (2, 'MANAGER') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO role (id, name) VALUES (3, 'USER') ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Usuario administrador por defecto (password: Admin123!)
-- INSERT INTO user (email, password, role_id) VALUES ('admin@ecommerce.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 1) ON DUPLICATE KEY UPDATE email = VALUES(email);

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
