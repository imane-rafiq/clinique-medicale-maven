CREATE DATABASE IF NOT EXISTS clinique_db;
USE clinique_db;

DROP TABLE IF EXISTS rendezvous;
DROP TABLE IF EXISTS utilisateurs;
DROP TABLE IF EXISTS medecins;
DROP TABLE IF EXISTS patients;

CREATE TABLE utilisateurs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'MEDECIN', 'SECRETAIRE') NOT NULL,
    medecin_id INT NULL
);

CREATE TABLE patients (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    date_naissance DATE,
    telephone VARCHAR(20)
);

CREATE TABLE medecins (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    specialite VARCHAR(100),
    horaires VARCHAR(100)
);

CREATE TABLE rendezvous (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    medecin_id INT NOT NULL,
    date_rdv DATE NOT NULL,
    heure_rdv TIME NOT NULL,
    statut VARCHAR(30) DEFAULT 'PLANIFIE',
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (medecin_id) REFERENCES medecins(id) ON DELETE CASCADE
);

INSERT INTO patients(nom, prenom, date_naissance, telephone) VALUES
('Alami', 'Youssef', '2000-02-15', '0611111111'),
('Benali', 'Sara', '1998-07-20', '0622222222');

INSERT INTO medecins(nom, specialite, horaires) VALUES
('Dr Amrani', 'Cardiologie', '08:00-16:00'),
('Dr Saidi', 'Dermatologie', '09:00-17:00');

INSERT INTO utilisateurs(username, password, role, medecin_id) VALUES
('admin', 'admin123', 'ADMIN', NULL),
('med1', 'med123', 'MEDECIN', 1),
('sec1', 'sec123', 'SECRETAIRE', NULL);

INSERT INTO rendezvous(patient_id, medecin_id, date_rdv, heure_rdv, statut) VALUES
(1, 1, '2026-05-01', '10:00:00', 'PLANIFIE'),
(2, 2, '2026-05-01', '11:30:00', 'PLANIFIE');
