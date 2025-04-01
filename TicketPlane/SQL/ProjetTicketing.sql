-- Création de la base de données
CREATE DATABASE ticketing;
-- Se connecter à la base de données
\ c ticketing CREATE TABLE Role(
   id_role SERIAL,
   nom VARCHAR(150) NOT NULL,
   PRIMARY KEY(id_role)
);
CREATE TABLE VilleDesservie(
   id_ville SERIAL,
   nom VARCHAR(250) NOT NULL,
   pays VARCHAR(150) NOT NULL,
   code_aeroport INTEGER,
   PRIMARY KEY(id_ville)
);
CREATE TABLE Modele(
   id_modele SERIAL,
   nom VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_modele),
   UNIQUE(nom)
);
CREATE TABLE TypeSiege(
   id_type SERIAL,
   nom VARCHAR(150) NOT NULL,
   PRIMARY KEY(id_type),
   UNIQUE(nom)
);
CREATE TABLE Vol(
   id_vol SERIAL,
   date_depart TIMESTAMP NOT NULL,
   date_arrivee TIMESTAMP NOT NULL,
   id_ville_depart INTEGER NOT NULL,
   id_ville_arrivee INTEGER NOT NULL,
   PRIMARY KEY(id_vol),
   FOREIGN KEY(id_ville_depart) REFERENCES VilleDesservie(id_ville),
   FOREIGN KEY(id_ville_arrivee) REFERENCES VilleDesservie(id_ville)
);
CREATE TABLE Utilisateur(
   id_utilisateur SERIAL,
   email VARCHAR(150) NOT NULL,
   mdp VARCHAR(50) NOT NULL,
   id_role INTEGER,
   PRIMARY KEY(id_utilisateur),
   UNIQUE(email),
   FOREIGN KEY(id_role) REFERENCES Role(id_role)
);
CREATE TABLE Avion(
   id_avion SERIAL,
   immatriculation INTEGER NOT NULL,
   date_fabrication DATE NOT NULL,
   id_ville_base INTEGER,
   id_modele INTEGER NOT NULL,
   PRIMARY KEY(id_avion),
   UNIQUE(immatriculation),
   FOREIGN KEY(id_ville_base) REFERENCES VilleDesservie(id_ville),
   FOREIGN KEY(id_modele) REFERENCES Modele(id_modele)
);
CREATE TABLE AvionTypeSiege(
   id_avion INTEGER,
   id_type INTEGER,
   nb_sieges VARCHAR(50),
   PRIMARY KEY(id_avion, id_type),
   FOREIGN KEY(id_avion) REFERENCES Avion(id_avion),
   FOREIGN KEY(id_type) REFERENCES TypeSiege(id_type)
);
CREATE TABLE PassagersVol(
   id_utilisateur INTEGER,
   id_vol INTEGER,
   PRIMARY KEY(id_utilisateur, id_vol),
   FOREIGN KEY(id_utilisateur) REFERENCES Utilisateur(id_utilisateur),
   FOREIGN KEY(id_vol) REFERENCES Vol(id_vol)
);
-- Table pour les tarifs selon le type de siège
CREATE TABLE TarifVol(
   id_tarif SERIAL,
   id_vol INTEGER NOT NULL,
   id_type_siege INTEGER NOT NULL,
   prix DECIMAL(10, 2) NOT NULL,
   PRIMARY KEY(id_tarif),
   FOREIGN KEY(id_vol) REFERENCES Vol(id_vol),
   FOREIGN KEY(id_type_siege) REFERENCES TypeSiege(id_type)
);
-- Table pour les statuts de réservation (ex: En attente, Confirmé, Annulé)
CREATE TABLE StatutReservation(
   id_statut SERIAL,
   nom VARCHAR(50) NOT NULL,
   PRIMARY KEY(id_statut),
   UNIQUE(nom)
);
-- Table principale des réservations
CREATE TABLE Reservation(
   id_reservation SERIAL,
   date_reservation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   id_vol INTEGER NOT NULL,
   id_utilisateur INTEGER NOT NULL,
   id_statut INTEGER NOT NULL,
   id_type_siege INTEGER NOT NULL,
   prix_total DECIMAL(10, 2) NOT NULL,
   code_reservation VARCHAR(10) NOT NULL,
   photo_passeport VARCHAR(255) NOT NULL,
   PRIMARY KEY(id_reservation),
   FOREIGN KEY(id_vol) REFERENCES Vol(id_vol),
   FOREIGN KEY(id_utilisateur) REFERENCES Utilisateur(id_utilisateur),
   FOREIGN KEY(id_statut) REFERENCES StatutReservation(id_statut),
   FOREIGN KEY(id_type_siege) REFERENCES TypeSiege(id_type),
   UNIQUE(code_reservation)
);

CREATE TABLE parametres_systeme (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    valeur VARCHAR(255) NOT NULL,
    description TEXT,
    date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertion des paramètres par défaut
INSERT INTO parametres_systeme (code, valeur, description) VALUES 
('HEURES_AVANT_VOL_RESERVATION', '72', 'Nombre d''heures minimum avant le départ du vol pour effectuer une réservation'),
('HEURES_AVANT_VOL_ANNULATION', '24', 'Nombre d''heures minimum avant le départ du vol pour annuler une réservation');

-- Suppression de l'ancienne table PassagersVol
DROP TABLE IF EXISTS PassagersVol;
-- Insertion des statuts de réservation de base
INSERT INTO StatutReservation (nom)
VALUES ('Confirme'),
   ('En attente'),
   ('Annule'),
   ('Rembourse');
-- Modification de la table Vol pour ajouter les places disponibles et l'avion
ALTER TABLE Vol
ADD COLUMN places_economique INTEGER DEFAULT 0,
   ADD COLUMN places_affaire INTEGER DEFAULT 0,
   ADD COLUMN places_premiere INTEGER DEFAULT 0,
   ADD COLUMN id_avion INTEGER NOT NULL,
   ADD FOREIGN KEY(id_avion) REFERENCES Avion(id_avion);
-- Index pour améliorer les performances
CREATE INDEX idx_reservation_vol ON Reservation(id_vol);
CREATE INDEX idx_reservation_utilisateur ON Reservation(id_utilisateur);
CREATE INDEX idx_reservation_code ON Reservation(code_reservation);
CREATE INDEX idx_passager_reservation ON PassagerReservation(id_reservation);