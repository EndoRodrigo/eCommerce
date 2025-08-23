-- Esquema de base de datos para eCommerce

-- Tabla de roles
CREATE TABLE IF NOT EXISTS role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Tabla de clientes
CREATE TABLE IF NOT EXISTS customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    identification_document_id INT NOT NULL,
    identification VARCHAR(50) NOT NULL UNIQUE,
    names VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    legal_organization_id VARCHAR(50) NOT NULL,
    tribute_id VARCHAR(50) NOT NULL,
    municipality_id VARCHAR(50) NOT NULL
);

-- Tabla de carritos
CREATE TABLE IF NOT EXISTS cart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code_reference_code VARCHAR(100) UNIQUE,
    description VARCHAR(255) DEFAULT 'Factura de Venta',
    payment_method_code VARCHAR(50),
    numbering_range_id INT DEFAULT 8,
    identification INT,
    FOREIGN KEY (identification) REFERENCES customer(id)
);

-- Tabla de productos/items
CREATE TABLE IF NOT EXISTS item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code_reference VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    discount_rate VARCHAR(20) NOT NULL,
    price FLOAT NOT NULL,
    tax_rate VARCHAR(20) NOT NULL,
    unit_measure_id INT NOT NULL,
    standard_code_id INT NOT NULL,
    is_excluded INT NOT NULL,
    tribute_id INT NOT NULL,
    cart_id INT,
    FOREIGN KEY (cart_id) REFERENCES cart(id)
);

-- Tabla de pagos
CREATE TABLE IF NOT EXISTS payment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_payment INT NOT NULL,
    description VARCHAR(255),
    price DOUBLE NOT NULL,
    method VARCHAR(50),
    status VARCHAR(50)
);
