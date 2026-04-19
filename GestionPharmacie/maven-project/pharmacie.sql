CREATE DATABASE IF NOT EXISTS pharmacie CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pharmacie;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS login (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_login timestamp NOT NULL,
    date_logout timestamp,
    id_user INT,
    FOREIGN KEY (id_user) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS patient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    tel INT,
    adresse VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS medicament (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    qte INT,
    dateexpiration VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS listemedicaments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pat INT,
    id_med INT,
    FOREIGN KEY (id_pat) REFERENCES patient(id) ON DELETE CASCADE,
    FOREIGN KEY (id_med) REFERENCES medicament(id) ON DELETE CASCADE
);
