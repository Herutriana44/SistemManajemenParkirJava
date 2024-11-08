-- Membuat database
CREATE DATABASE parkir_management;
USE parkir_management;

-- Tabel untuk mencatat ketersediaan slot parkir
CREATE TABLE ketersediaan (
    slotID INT PRIMARY KEY AUTO_INCREMENT,
    isAvailable BOOLEAN NOT NULL DEFAULT TRUE,
    licensePlate VARCHAR(15) DEFAULT NULL,
    entryTime DATETIME DEFAULT NULL
);

-- Tabel untuk mencatat riwayat parkir
CREATE TABLE history_parkir (
    historyID INT PRIMARY KEY AUTO_INCREMENT,
    slotID INT,
    licensePlate VARCHAR(15) NOT NULL,
    entryTime DATETIME NOT NULL,
    exitTime DATETIME,
    parkingDuration INT GENERATED ALWAYS AS (TIMESTAMPDIFF(MINUTE, entryTime, exitTime)) STORED,
    FOREIGN KEY (slotID) REFERENCES ketersediaan(slotID)
);
