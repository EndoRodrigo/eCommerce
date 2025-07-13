# 🛒 Sistema de eCommerce | Spring Boot

Este proyecto es una solución completa de **comercio electrónico (eCommerce)** desarrollada en **Spring Boot**, que incluye funcionalidades tanto para el usuario final como para la administración del sistema (POS, gestión de inventarios, clientes, usuarios, etc).

---

## 🚀 Características principales

### 🧾 Módulo POS (Punto de Venta)
- Registro de ventas en tiempo real
- Selección rápida de productos
- Cálculo automático de totales
- Generación de tickets o comprobantes
- Control de stock en cada venta

### 👤 Gestión de clientes
- Registro de nuevos clientes
- Consulta y edición de clientes existentes
- Historial de compras

### 📦 Gestión de productos
- CRUD completo de productos
- Asignación de categorías
- Control de inventario y stock mínimo
- Subida de imágenes y detalles

### 🔐 Autenticación y roles (Spring Security)
- Registro e inicio de sesión
- Autenticación con base de datos
- Roles: `ADMIN`, `VENDEDOR`, `CLIENTE`
- Control de acceso por vistas

### 📈 Panel administrativo
- Dashboard con métricas
- Visualización de ventas y productos más vendidos
- Administración de usuarios y roles

---

## 🛠️ Tecnologías utilizadas

| Tecnología      | Descripción                              |
|----------------|------------------------------------------|
| Spring Boot     | Framework principal del backend          |
| Spring Security | Manejo de autenticación y autorización   |
| Spring Data JPA | Conexión y gestión de base de datos      |
| Thymeleaf       | Motor de plantillas para vistas web      |
| Bootstrap 5     | Diseño responsivo y componentes UI       |
| H2/MySQL        | Base de datos embebida o relacional      |
| Lombok          | Reducir el código boilerplate            |
| Maven           | Gestión de dependencias                  |

---

## 📸 Capturas de pantalla

> Puedes añadir aquí imágenes de las siguientes pantallas:
- Login personalizado
- Panel administrativo
- Vista de productos
- Punto de venta
- Registro de cliente

---

## 📂 Estructura del proyecto

´
src/
├── main/
│ ├── java/
│ │ └── com.ejemplo.ecommerce/
│ │ ├── controller/
│ │ ├── model/
│ │ ├── repository/
│ │ ├── service/
│ │ ├── security/
│ │ └── EcommerceApplication.java
│ └── resources/
│ ├── static/
│ ├── templates/
│ ├── application.properties
└── test/
´

