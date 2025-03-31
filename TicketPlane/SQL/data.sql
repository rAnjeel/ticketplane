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