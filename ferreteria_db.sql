-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 19, 2019 at 11:53 AM
-- Server version: 8.0.17
-- PHP Version: 7.3.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ferreteria_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `categoria`
--

CREATE TABLE `categoria` (
  `idcategoria` int(11) NOT NULL,
  `nombre` varchar(500) NOT NULL,
  `cantidad_disponible` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `categoria`
--

INSERT INTO `categoria` (`idcategoria`, `nombre`, `cantidad_disponible`) VALUES
(10001, 'Cat1', 0),
(10002, 'Categoria 2', 0),
(10003, 'Construccion', 0),
(10004, 'Herramientas', 0),
(10005, 'Mat Hogar', 0),
(10006, 'Mat Pesados', 0);

-- --------------------------------------------------------

--
-- Table structure for table `cliente`
--

CREATE TABLE `cliente` (
  `idCliente` int(11) NOT NULL,
  `nombre` varchar(500) NOT NULL,
  `direccion` text NOT NULL,
  `telefono` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cliente`
--

INSERT INTO `cliente` (`idCliente`, `nombre`, `direccion`, `telefono`) VALUES
(1069425652, 'Cliente Numero Uno', 'Calle 123456', '301333333');

-- --------------------------------------------------------

--
-- Table structure for table `detalle_factura`
--

CREATE TABLE `detalle_factura` (
  `Productos_idproducto` varchar(40) NOT NULL,
  `Facturas_idfactura` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `detalle_factura`
--

INSERT INTO `detalle_factura` (`Productos_idproducto`, `Facturas_idfactura`) VALUES
('1', 21123),
('1', 35588),
('12312', 100012),
('232425', 100012),
('151214', 100012),
('1', 190466),
('12312', 190466),
('1', 5283),
('12312', 5283);

-- --------------------------------------------------------

--
-- Table structure for table `facturas`
--

CREATE TABLE `facturas` (
  `idfactura` int(11) NOT NULL,
  `noventa` text NOT NULL,
  `cantidad_productos` int(11) NOT NULL,
  `total_venta` double NOT NULL,
  `fechahora_venta` datetime NOT NULL,
  `detalle_venta` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `facturas`
--

INSERT INTO `facturas` (`idfactura`, `noventa`, `cantidad_productos`, `total_venta`, `fechahora_venta`, `detalle_venta`) VALUES
(5283, 'c991bfac-cd34-488e-a116-35584c3ef08c', 5, 67900, '2019-12-19 14:51:21', '[Producto{idproducto=1, nombre=p1, precioVenta=37500.0, precioCompra=12000.0, fechaRegistro=2019-12-17 11:03:40, disponibilidad=3, categoria=10001}, Producto{idproducto=12312, nombre=producto 1, precioVenta=30400.0, precioCompra=10200.0, fechaRegistro=2019-12-19 08:08:09, disponibilidad=2, categoria=10001}]'),
(21123, '4cfdddcd-82be-4d2a-b687-99fa41a11157', 1, 12000, '2019-12-17 11:03:40', '[modelos.Producto@4904e1ad]'),
(35588, '97b8bd3e-917e-42fa-a9b5-f26e6065c97f', 2, 25000, '2019-12-17 15:11:49', '[modelos.Producto@73214d8b]'),
(99839, '75a72e0f-c27e-49fe-afca-f32184f6e30e', 0, 0, '2019-12-19 14:50:37', '[]'),
(100012, '41dea07f-71fc-43f6-8859-1db37cdbb759', 0, 0, '2019-12-19 08:08:08', '[]'),
(190466, '8d2439d4-f7a9-476b-827d-603e5b0f6e38', 6, 83100, '2019-12-19 14:50:03', '[Producto{idproducto=1, nombre=p1, precioVenta=37500.0, precioCompra=12000.0, fechaRegistro=2019-12-17 11:03:40, disponibilidad=3, categoria=10001}, Producto{idproducto=12312, nombre=producto 1, precioVenta=45600.0, precioCompra=10200.0, fechaRegistro=2019-12-19 08:08:09, disponibilidad=3, categoria=10001}]');

-- --------------------------------------------------------

--
-- Table structure for table `f_compra`
--

CREATE TABLE `f_compra` (
  `Factura_idfactura` int(11) NOT NULL,
  `Proveedor_idproveedor` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `f_compra`
--

INSERT INTO `f_compra` (`Factura_idfactura`, `Proveedor_idproveedor`) VALUES
(21123, '1000'),
(100012, '1000');

-- --------------------------------------------------------

--
-- Table structure for table `f_venta`
--

CREATE TABLE `f_venta` (
  `Factura_idfactura` int(11) NOT NULL,
  `Cliente_idCliente` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `f_venta`
--

INSERT INTO `f_venta` (`Factura_idfactura`, `Cliente_idCliente`) VALUES
(5283, 1069425652),
(35588, 1069425652);

-- --------------------------------------------------------

--
-- Table structure for table `productos`
--

CREATE TABLE `productos` (
  `idproducto` varchar(40) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `precio_venta` double NOT NULL,
  `precio_compra` double NOT NULL,
  `fecha_registro` datetime NOT NULL,
  `disponibilidad` int(11) NOT NULL,
  `categoria_idcategoria` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `productos`
--

INSERT INTO `productos` (`idproducto`, `nombre`, `precio_venta`, `precio_compra`, `fecha_registro`, `disponibilidad`, `categoria_idcategoria`) VALUES
('1', 'p1', 12500, 12000, '2019-12-17 11:03:40', 10, 10001),
('12312', 'producto 1', 15200, 10200, '2019-12-19 08:08:09', 20, 10001),
('151214', 'Producto 4', 15800, 10000, '2019-12-19 08:08:09', 10, 10001),
('232425', 'producto 2', 20400, 20000, '2019-12-19 08:08:09', 10, 10002);

-- --------------------------------------------------------

--
-- Table structure for table `proveedor`
--

CREATE TABLE `proveedor` (
  `idproveedor` varchar(100) NOT NULL,
  `nombre` varchar(500) NOT NULL,
  `ciudad` varchar(500) NOT NULL,
  `telefono` varchar(15) NOT NULL,
  `nombre_contacto` varchar(5000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `proveedor`
--

INSERT INTO `proveedor` (`idproveedor`, `nombre`, `ciudad`, `telefono`, `nombre_contacto`) VALUES
('1000', 'Proveedor1', 'Monteria', '30000000', 'Contacto 1'),
('232540', 'Proveedor El Paisa', 'Sahagun', '3013333131', 'Contacto el paisita');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`idcategoria`);

--
-- Indexes for table `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`idCliente`);

--
-- Indexes for table `detalle_factura`
--
ALTER TABLE `detalle_factura`
  ADD KEY `fk_Detalle_Factura_Productos1_idx` (`Productos_idproducto`),
  ADD KEY `fk_Detalle_Factura_Facturas1_idx` (`Facturas_idfactura`);

--
-- Indexes for table `facturas`
--
ALTER TABLE `facturas`
  ADD PRIMARY KEY (`idfactura`);

--
-- Indexes for table `f_compra`
--
ALTER TABLE `f_compra`
  ADD PRIMARY KEY (`Factura_idfactura`),
  ADD KEY `fk_F_Compra_Proveedor1_idx` (`Proveedor_idproveedor`);

--
-- Indexes for table `f_venta`
--
ALTER TABLE `f_venta`
  ADD PRIMARY KEY (`Factura_idfactura`),
  ADD KEY `fk_F_Venta_Cliente1_idx` (`Cliente_idCliente`);

--
-- Indexes for table `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`idproducto`),
  ADD KEY `fk_products_category1_idx` (`categoria_idcategoria`);

--
-- Indexes for table `proveedor`
--
ALTER TABLE `proveedor`
  ADD PRIMARY KEY (`idproveedor`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `detalle_factura`
--
ALTER TABLE `detalle_factura`
  ADD CONSTRAINT `fk_Detalle_Factura_Facturas1` FOREIGN KEY (`Facturas_idfactura`) REFERENCES `facturas` (`idfactura`),
  ADD CONSTRAINT `fk_Detalle_Factura_Productos1` FOREIGN KEY (`Productos_idproducto`) REFERENCES `productos` (`idproducto`);

--
-- Constraints for table `f_compra`
--
ALTER TABLE `f_compra`
  ADD CONSTRAINT `fk_F_Compra_Factura1` FOREIGN KEY (`Factura_idfactura`) REFERENCES `facturas` (`idfactura`),
  ADD CONSTRAINT `fk_F_Compra_Proveedor1` FOREIGN KEY (`Proveedor_idproveedor`) REFERENCES `proveedor` (`idproveedor`);

--
-- Constraints for table `f_venta`
--
ALTER TABLE `f_venta`
  ADD CONSTRAINT `fk_F_Venta_Cliente1` FOREIGN KEY (`Cliente_idCliente`) REFERENCES `cliente` (`idCliente`),
  ADD CONSTRAINT `fk_F_Venta_Factura1` FOREIGN KEY (`Factura_idfactura`) REFERENCES `facturas` (`idfactura`);

--
-- Constraints for table `productos`
--
ALTER TABLE `productos`
  ADD CONSTRAINT `fk_products_category1` FOREIGN KEY (`categoria_idcategoria`) REFERENCES `categoria` (`idcategoria`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
