-- Création de la base de données
CREATE DATABASE ticketing WITH ENCODING 'UTF8';
-- Se connecter à la base de données
\c ticketing


CREATE TABLE Role(
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
CREATE TABLE Avion(
   id_avion SERIAL,
   immatriculation INTEGER NOT NULL,
   date_fabrication DATE NOT NULL,
   id_modele INTEGER NOT NULL,
   siege_economique INTEGER NOT NULL,
   siege_business INTEGER NOT NULL,
   PRIMARY KEY(id_avion),
   UNIQUE(immatriculation),
   FOREIGN KEY(id_modele) REFERENCES Modele(id_modele)
);
CREATE TABLE Vol(
   id_vol SERIAL,
   id_avion INTEGER NOT NULL,
   date_depart TIMESTAMP NOT NULL,
   date_arrivee TIMESTAMP NOT NULL,
   id_ville_depart INTEGER NOT NULL,
   id_ville_arrivee INTEGER NOT NULL,
   PRIMARY KEY(id_vol),
   FOREIGN KEY(id_ville_depart) REFERENCES VilleDesservie(id_ville),
   FOREIGN KEY(id_ville_arrivee) REFERENCES VilleDesservie(id_ville),
   FOREIGN KEY(id_avion) REFERENCES Avion(id_avion)
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
   est_enfant BOOLEAN DEFAULT FALSE,
   est_promotion BOOLEAN DEFAULT FALSE,
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

CREATE TABLE promotions (
   id SERIAL PRIMARY KEY,
   id_type_siege INTEGER NOT NULL,
   date_debut DATE NOT NULL,
   date_fin DATE NOT NULL,
   pourcentage DECIMAL(10, 2) NOT NULL,
   nombre INTEGER NOT NULL,
   FOREIGN KEY(id_type_siege) REFERENCES TypeSiege(id_type)
);
-- Ajouter les nouveaux paramètres système
INSERT INTO parametres_systeme (code, valeur, description)
VALUES 
   ('AGE_MAX_ENFANT', '12', 'Age max enfant'),
   (
      'NB_SIEGES_PROMO',
      '10',
      'Nb sieges promo par vol'
   ),
   (
      'TAUX_REDUCTION_ENFANT',
      '50',
      'Reduction enfant (%)'
   ),
   (
      'TAUX_REDUCTION_PROMO',
      '25',
      'Reduction promo (%)'
   );
-- Insertion des statuts de réservation de base
INSERT INTO StatutReservation (nom)
VALUES ('Confirme'),
   ('En attente'),
   ('Annule'),
   ('Rembourse');

