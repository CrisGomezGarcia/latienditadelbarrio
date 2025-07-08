CREATE DATABASE IF NOT EXISTS latienditadelbarrio
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE latienditadelbarrio;

-- Tablas

CREATE TABLE tbl_categorias (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion VARCHAR(250),
  estado TINYINT DEFAULT 1,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE tbl_marcas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion VARCHAR(250),
  estado TINYINT DEFAULT 1,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE tbl_productos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  tipo_presentacion VARCHAR(100) NOT NULL,
  codigo_barras VARCHAR(50) UNIQUE,
  precio_sugerido DECIMAL(10,2) NOT NULL,
  stock INT DEFAULT 0,
  id_categoria INT NOT NULL,
  id_marca INT NOT NULL,
  estado TINYINT DEFAULT 1,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (id_categoria) REFERENCES tbl_categorias(id),
  FOREIGN KEY (id_marca) REFERENCES tbl_marcas(id)
);

CREATE TABLE tbl_proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL UNIQUE,
    rfc VARCHAR(20) UNIQUE,
    telefono VARCHAR(100),
    correo VARCHAR(100),
    direccion VARCHAR(255),
    estado TINYINT DEFAULT 1, -- 1: Activo, 0: Inactivo
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Vistas

CREATE VIEW vw_productos_detalle AS
SELECT
  tp.id,
  tp.nombre,
  tp.tipo_presentacion,
  tp.codigo_barras,
  tp.precio_sugerido,
  tp.stock,
  tp.estado,
  tc.id AS categoria_id,
  tc.nombre AS categoria_nombre,
  tm.id AS marca_id,
  tm.nombre AS marca_nombre
FROM tbl_productos tp
JOIN tbl_categorias tc ON tp.id_categoria = tc.id
JOIN tbl_marcas tm ON tp.id_marca = tm.id;