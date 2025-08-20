-- Insertion des modèles d'avions
INSERT INTO Modele (nom) VALUES 
    ('Boeing 737-800'),
    ('Airbus A320'),
    ('Airbus A350-900'),
    ('Boeing 787-9'),
    ('Airbus A330-300'),
    ('Embraer E190');

-- Insertion des avions
-- Note: Les immatriculations suivent le format numérique comme défini dans la table
-- Les dates de fabrication sont réalistes pour chaque type d'avion
INSERT INTO Avion (immatriculation, date_fabrication, id_modele, siege_economique, siege_business) VALUES 
    (101234, '2018-03-15', 1, 100, 30),
    (101235, '2019-06-22', 2, 100, 20),
    (101236, '2020-01-10', 1, 100, 30),
    (101237, '2017-11-30', 3, 100, 22),
    (101238, '2019-09-05', 2, 100, 20),
    (101239, '2021-04-18', 3, 100, 22),
    (101240, '2022-02-28', 1, 100, 30),
    (101241, '2020-07-14', 2, 100, 20);

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
VALUES ('Toamasina', 'Madagascar', 123457);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Antsiranana', 'Madagascar', 123458);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Mahajanga', 'Madagascar', 123459);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Fianarantsoa', 'Madagascar', 123460);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Paris', 'France', 123461);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Lyon', 'France', 123462);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Marseille', 'France', 123463);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Brest', 'France', 123464);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Lille', 'France', 123465);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Nanterre', 'France', 123466);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Toulouse', 'France', 123467);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Nice', 'France', 123468);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Bordeaux', 'France', 123469);
-- TYPE_SIEGE
INSERT INTO TypeSiege (nom)
VALUES ('Business');
INSERT INTO TypeSiege (nom)
VALUES ('Economique');


            SELECT prix
            FROM placevol
            WHERE id_vol = 5
            AND id_type_siege = 1
            AND date_fin >= 2025-08-20
            ORDER BY date_fin ASC
            LIMIT 1;