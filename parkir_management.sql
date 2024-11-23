-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 22, 2024 at 10:57 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `parkir_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `history_parkir`
--

CREATE TABLE `history_parkir` (
  `idHistory` varchar(10) NOT NULL,
  `idSlot` varchar(10) DEFAULT NULL,
  `idUser` varchar(10) DEFAULT NULL,
  `platNomor` varchar(15) DEFAULT NULL,
  `entryTime` time DEFAULT NULL,
  `exitTime` time DEFAULT NULL,
  `durasiParkir` int(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ketersediaan`
--

CREATE TABLE `ketersediaan` (
  `idSlot` varchar(10) NOT NULL,
  `idUser` varchar(10) DEFAULT NULL,
  `tersedia` tinyint(1) NOT NULL,
  `platNomor` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ketersediaan`
--

INSERT INTO `ketersediaan` (`idSlot`, `idUser`, `tersedia`, `platNomor`) VALUES
('A1', NULL, 1, NULL),
('A2', NULL, 1, NULL),
('A3', NULL, 1, NULL),
('A4', NULL, 1, NULL),
('A5', NULL, 1, NULL),
('A6', 'PK000', 0, NULL),
('A7', NULL, 1, NULL),
('A8', NULL, 1, NULL),
('B1', NULL, 1, NULL),
('B2', NULL, 1, NULL),
('B3', NULL, 1, NULL),
('B4', NULL, 1, NULL),
('B5', NULL, 1, NULL),
('B6', NULL, 1, NULL),
('B7', NULL, 1, NULL),
('B8', NULL, 1, NULL),
('C1', NULL, 1, NULL),
('C2', NULL, 1, NULL),
('C3', NULL, 1, NULL),
('C4', NULL, 1, NULL),
('C5', NULL, 1, NULL),
('C6', NULL, 1, NULL),
('C7', NULL, 1, NULL),
('C8', NULL, 1, NULL),
('D1', NULL, 1, NULL),
('D2', NULL, 1, NULL),
('D3', NULL, 1, NULL),
('D4', NULL, 1, NULL),
('D5', NULL, 1, NULL),
('D6', NULL, 1, NULL),
('D7', NULL, 1, NULL),
('D8', NULL, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `idUser` varchar(10) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `status` enum('mahasiswa','dosen','admin','') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`idUser`, `username`, `password`, `status`) VALUES
('PK000', 'admin', 'admin', 'admin'),
('PK006', 'ariella', '2310631170006', 'mahasiswa'),
('PK012', 'dini', '2310631170012', 'mahasiswa'),
('PK018', 'gagas', '2310631170018', 'mahasiswa'),
('PK024', 'julia', '2310631170024', 'mahasiswa'),
('PK030', 'marsha', '2310631170030', 'mahasiswa'),
('PK036', 'najwa', '2310631170036', 'mahasiswa'),
('PK042', 'raihan', '2310631170042', 'mahasiswa'),
('PK048', 'salina', '2310631170048', 'mahasiswa'),
('PK054', 'teguh', '2310631170054', 'mahasiswa'),
('PK060', 'abdurrokhman', '2310631170060', 'mahasiswa'),
('PK066', 'akriz', '2310631170066', 'mahasiswa'),
('PK072', 'devi', '2310631170072', 'mahasiswa'),
('PK078', 'erika', '2310631170078', 'mahasiswa'),
('PK084', 'ferdi', '2310631170084', 'mahasiswa'),
('PK090', 'iman', '2310631170090', 'mahasiswa'),
('PK096', 'merylien', '2310631170096', 'mahasiswa'),
('PK102', 'muhammad', '2310631170102', 'mahasiswa'),
('PK108', 'naufal', '2310631170108', 'mahasiswa'),
('PK114', 'reynanda', '2310631170114', 'mahasiswa'),
('PK120', 'wahyu', '2310631170120', 'mahasiswa'),
('PK126', 'ayang', '2310631170126', 'mahasiswa'),
('PK132', 'david', '2310631170132', 'mahasiswa'),
('PK138', 'jonathan', '2310631170138', 'mahasiswa'),
('PK144', 'naufal', '2310631170144', 'mahasiswa'),
('PK150', 'rizkina', '2310631170150', 'mahasiswa'),
('PK156', 'alif', '2310631170156', 'mahasiswa');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `history_parkir`
--
ALTER TABLE `history_parkir`
  ADD PRIMARY KEY (`idHistory`),
  ADD KEY `idSlot` (`idSlot`),
  ADD KEY `idUser` (`idUser`);

--
-- Indexes for table `ketersediaan`
--
ALTER TABLE `ketersediaan`
  ADD PRIMARY KEY (`idSlot`),
  ADD KEY `idUser` (`idUser`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`idUser`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `history_parkir`
--
ALTER TABLE `history_parkir`
  ADD CONSTRAINT `history_parkir_ibfk_1` FOREIGN KEY (`idSlot`) REFERENCES `ketersediaan` (`idSlot`),
  ADD CONSTRAINT `history_parkir_ibfk_2` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`);

--
-- Constraints for table `ketersediaan`
--
ALTER TABLE `ketersediaan`
  ADD CONSTRAINT `ketersediaan_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
