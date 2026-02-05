-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-01-2026 a las 16:11:05
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gestiondeaveriatynsolution`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `averia`
--

DROP TABLE IF EXISTS `averia`;
CREATE TABLE IF NOT EXISTS `averia` (
  `codigoAveria` int(11) NOT NULL AUTO_INCREMENT,
  `descInicAveria` varchar(255) NOT NULL,
  `fechaInicioAver` datetime NOT NULL,
  `fechaAsigTecnico` datetime DEFAULT NULL,
  `fechaAcepTecnico` datetime DEFAULT NULL,
  `fechaFinalizTecnico` datetime DEFAULT NULL,
  `procRealizadoTecnico` varchar(255) DEFAULT NULL,
  `usuarioReportaFK` int(11) NOT NULL,
  `usuarioTecnicoFK` int(11) DEFAULT NULL COMMENT 'FK -> usuario (técnico); puede ser NULL',
  `maquinariaFK` int(11) NOT NULL,
  `tipoAveriaFK` int(11) NOT NULL,
  PRIMARY KEY (`codigoAveria`),
  KEY `fk_averia_usuario_reporta` (`usuarioReportaFK`),
  KEY `fk_averia_usuario_tecnico` (`usuarioTecnicoFK`),
  KEY `fk_averia_maquinaria` (`maquinariaFK`),
  KEY `fk_averia_tipo` (`tipoAveriaFK`)
) ;

--
-- RELACIONES PARA LA TABLA `averia`:
--   `usuarioReportaFK`
--       `usuario` -> `codigoUsuario`
--   `usuarioTecnicoFK`
--       `usuario` -> `codigoUsuario`
--   `maquinariaFK`
--       `maquinaria` -> `codigoMaquinaria`
--   `tipoAveriaFK`
--       `tipo_averia` -> `codigoTipoAveria`
--   `maquinariaFK`
--       `maquinaria` -> `codigoMaquinaria`
--   `tipoAveriaFK`
--       `tipo_averia` -> `codigoTipoAveria`
--   `usuarioReportaFK`
--       `usuario` -> `codigoUsuario`
--   `usuarioTecnicoFK`
--       `usuario` -> `codigoUsuario`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado`
--

DROP TABLE IF EXISTS `estado`;
CREATE TABLE IF NOT EXISTS `estado` (
  `codigoEstado` int(11) NOT NULL,
  `descripcionEstado` varchar(50) NOT NULL,
  PRIMARY KEY (`codigoEstado`),
  UNIQUE KEY `uq_estados_descripcion` (`descripcionEstado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELACIONES PARA LA TABLA `estado`:
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `maquinaria`
--

DROP TABLE IF EXISTS `maquinaria`;
CREATE TABLE IF NOT EXISTS `maquinaria` (
  `codigoMaquinaria` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `codigoEstadoFK` int(11) NOT NULL,
  `fechaAlta` date NOT NULL,
  `fechaBaja` date DEFAULT NULL,
  `tipoMaquinariaFK` int(11) NOT NULL,
  PRIMARY KEY (`codigoMaquinaria`),
  UNIQUE KEY `uq_maquinaria_nombre` (`nombre`),
  KEY `fk_maquinaria_estado` (`codigoEstadoFK`),
  KEY `fk_maquinaria_tipo` (`tipoMaquinariaFK`)
) ;

--
-- RELACIONES PARA LA TABLA `maquinaria`:
--   `codigoEstadoFK`
--       `estado` -> `codigoEstado`
--   `tipoMaquinariaFK`
--       `tipo_maquinaria` -> `codigoTipoMaquinaria`
--   `codigoEstadoFK`
--       `estado` -> `codigoEstado`
--   `tipoMaquinariaFK`
--       `tipo_maquinaria` -> `codigoTipoMaquinaria`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

DROP TABLE IF EXISTS `rol`;
CREATE TABLE IF NOT EXISTS `rol` (
  `codigoRol` int(11) NOT NULL,
  `descripcionRol` varchar(50) NOT NULL,
  PRIMARY KEY (`codigoRol`),
  UNIQUE KEY `uq_rol_descripcion` (`descripcionRol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELACIONES PARA LA TABLA `rol`:
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_averia`
--

DROP TABLE IF EXISTS `tipo_averia`;
CREATE TABLE IF NOT EXISTS `tipo_averia` (
  `codigoTipoAveria` int(11) NOT NULL,
  `descripcionTipoAv` varchar(100) NOT NULL,
  `tiempoPromRepar` float NOT NULL,
  PRIMARY KEY (`codigoTipoAveria`),
  UNIQUE KEY `uq_tipo_averia_descripcion` (`descripcionTipoAv`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELACIONES PARA LA TABLA `tipo_averia`:
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_maquinaria`
--

DROP TABLE IF EXISTS `tipo_maquinaria`;
CREATE TABLE IF NOT EXISTS `tipo_maquinaria` (
  `codigoTipoMaquinaria` int(11) NOT NULL,
  `descripcionMaq` varchar(100) NOT NULL,
  PRIMARY KEY (`codigoTipoMaquinaria`),
  UNIQUE KEY `uq_tipo_maquinaria_descripcion` (`descripcionMaq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- RELACIONES PARA LA TABLA `tipo_maquinaria`:
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE IF NOT EXISTS `usuario` (
  `codigoUsuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `codigoRolFK` int(11) NOT NULL,
  `telefono` varchar(17) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `intentos` int(11) NOT NULL DEFAULT 0,
  `ultimoAcceso` timestamp NULL DEFAULT NULL COMMENT 'puede ser NULL',
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`codigoUsuario`),
  UNIQUE KEY `uq_usuario_email` (`email`),
  KEY `fk_usuario_rol` (`codigoRolFK`)
) ;

--
-- RELACIONES PARA LA TABLA `usuario`:
--   `codigoRolFK`
--       `rol` -> `codigoRol`
--   `codigoRolFK`
--       `rol` -> `codigoRol`
--

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `averia`
--
ALTER TABLE `averia`
  ADD CONSTRAINT `averia_ibfk_1` FOREIGN KEY (`usuarioReportaFK`) REFERENCES `usuario` (`codigoUsuario`),
  ADD CONSTRAINT `averia_ibfk_2` FOREIGN KEY (`usuarioTecnicoFK`) REFERENCES `usuario` (`codigoUsuario`),
  ADD CONSTRAINT `averia_ibfk_3` FOREIGN KEY (`maquinariaFK`) REFERENCES `maquinaria` (`codigoMaquinaria`),
  ADD CONSTRAINT `averia_ibfk_4` FOREIGN KEY (`tipoAveriaFK`) REFERENCES `tipo_averia` (`codigoTipoAveria`),
  ADD CONSTRAINT `fk_averia_maquinaria` FOREIGN KEY (`maquinariaFK`) REFERENCES `maquinaria` (`codigoMaquinaria`),
  ADD CONSTRAINT `fk_averia_tipo` FOREIGN KEY (`tipoAveriaFK`) REFERENCES `tipo_averia` (`codigoTipoAveria`),
  ADD CONSTRAINT `fk_averia_usuario_reporta` FOREIGN KEY (`usuarioReportaFK`) REFERENCES `usuario` (`codigoUsuario`),
  ADD CONSTRAINT `fk_averia_usuario_tecnico` FOREIGN KEY (`usuarioTecnicoFK`) REFERENCES `usuario` (`codigoUsuario`);

--
-- Filtros para la tabla `maquinaria`
--
ALTER TABLE `maquinaria`
  ADD CONSTRAINT `fk_maquinaria_estado` FOREIGN KEY (`codigoEstadoFK`) REFERENCES `estado` (`codigoEstado`),
  ADD CONSTRAINT `fk_maquinaria_tipo` FOREIGN KEY (`tipoMaquinariaFK`) REFERENCES `tipo_maquinaria` (`codigoTipoMaquinaria`),
  ADD CONSTRAINT `maquinaria_ibfk_1` FOREIGN KEY (`codigoEstadoFK`) REFERENCES `estado` (`codigoEstado`),
  ADD CONSTRAINT `maquinaria_ibfk_2` FOREIGN KEY (`tipoMaquinariaFK`) REFERENCES `tipo_maquinaria` (`codigoTipoMaquinaria`);

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`codigoRolFK`) REFERENCES `rol` (`codigoRol`),
  ADD CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`codigoRolFK`) REFERENCES `rol` (`codigoRol`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
