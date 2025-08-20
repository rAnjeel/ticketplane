-- Insertion des modèles d'avions
INSERT INTO Modele (nom) VALUES 
    ('Air MAda'),
    ('Air france');

-- Insertion des avions
-- Note: Les immatriculations suivent le format numérique comme défini dans la table
-- Les dates de fabrication sont réalistes pour chaque type d'avion
INSERT INTO Avion (immatriculation, date_fabrication, id_modele, siege_economique, siege_business) VALUES 
    (101234, '2018-03-15', 1, 100, 30),
    (101235, '2019-06-22', 2, 100, 20);

-- ROLE
INSERT INTO Role (nom)
VALUES ('ADMIN');
INSERT INTO Role (nom)
VALUES ('CLIENT');
-- UTILISATEUR
INSERT INTO Utilisateur (email, mdp, id_role)
VALUES ('admin@gmail.com', 'admin', 1);
INSERT INTO Utilisateur (email, mdp, id_role)
VALUES ('client1@gmail.com', 'client123', 2);
-- VILLE
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Antananarivo', 'Madagascar', 123456);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Paris CDG', 'Paris', 123457);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Mauritius', 'Mauritius', 123458);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Addis Abeba', 'Addis', 123459);
-- TYPE_SIEGE
INSERT INTO TypeSiege (nom)
VALUES ('Business');
INSERT INTO TypeSiege (nom)
VALUES ('Economique');


-- SELECT prix
-- FROM placevol
-- WHERE id_vol = 5
-- AND id_type_siege =2
-- AND date_fin >= 2025-08-20
-- ORDER BY date_fin ASC
-- LIMIT 1;