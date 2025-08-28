-- ========================================
-- Esquema de base de datos para eCommerce
-- Adaptado a MariaDB
-- ========================================

-- Tabla de roles
CREATE TABLE IF NOT EXISTS role (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(50) NOT NULL UNIQUE
    ) ENGINE=InnoDB;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS user (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE RESTRICT ON UPDATE CASCADE
    ) ENGINE=InnoDB;

-- Tabla de clientes
CREATE TABLE IF NOT EXISTS customer (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        identification_document_id INT NOT NULL,
                                        identification VARCHAR(50) NOT NULL UNIQUE,
    names VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    legal_organization_id VARCHAR(50) NOT NULL,
    tribute_id VARCHAR(50) NOT NULL,
    municipality_id VARCHAR(50) NOT NULL
    ) ENGINE=InnoDB;

-- Tabla de carritos
CREATE TABLE IF NOT EXISTS cart (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    code_reference_code VARCHAR(100) UNIQUE,
    description VARCHAR(255) DEFAULT 'Factura de Venta',
    payment_method_code VARCHAR(50),
    numbering_range_id INT DEFAULT 8,
    customer_id INT,
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB;

-- Tabla de productos/items
CREATE TABLE IF NOT EXISTS item (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    code_reference VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    discount_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    price DECIMAL(12,2) NOT NULL,
    tax_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    unit_measure_id INT NOT NULL,
    standard_code_id INT NOT NULL,
    is_excluded TINYINT(1) NOT NULL DEFAULT 0,
    tribute_id INT NOT NULL,
    cart_id INT,
    FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB;

-- Tabla de pagos
CREATE TABLE IF NOT EXISTS payment (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       id_payment INT NOT NULL,
                                       description VARCHAR(255),
    price DECIMAL(12,2) NOT NULL,
    method VARCHAR(50),
    status VARCHAR(50)
    ) ENGINE=InnoDB;

-- ========================================
-- Tablas para Spring Session (MariaDB/MySQL)
-- ========================================
CREATE TABLE IF NOT EXISTS SPRING_SESSION (
                                              PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
    ) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE IF NOT EXISTS SPRING_SESSION_ATTRIBUTES (
                                                         SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BLOB NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID)
    REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
    ) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;
