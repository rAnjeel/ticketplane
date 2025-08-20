-- Insertion des modèles d'avions
INSERT INTO Modele (nom) VALUES 
    ('Air Mad'),
    ('Air france');

-- Insertion des avions
-- Note: Les immatriculations suivent le format numérique comme défini dans la table
-- Les dates de fabrication sont réalistes pour chaque type d'avion
INSERT INTO Avion (immatriculation, date_fabrication, id_modele, siege_economique, siege_business) VALUES 
    (101234, '2018-03-15', 1, 100, 30),
    (101235, '2019-06-22', 2, 100, 20);


-- VILLE
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Antananarivo', 'Madagascar', 123456);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Paris CDG', 'Paris', 123457);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Mauritius', 'Mauritius', 123458);
INSERT INTO VilleDesservie (nom, pays, code_aeroport)
VALUES ('Addis Abeba', 'Addis', 123459);



-- SELECT prix
-- FROM placevol
-- WHERE id_vol = 5
-- AND id_type_siege =2
-- AND date_fin >= 2025-08-20
-- ORDER BY date_fin ASC
-- LIMIT 1;


-- Vider toutes les tables et réinitialiser les auto-increments
TRUNCATE TABLE 
    Reservation,
    TarifVol,
    placeVol,
    promotions,
    Vol,
    Avion,
    Modele,
    VilleDesservie,
RESTART IDENTITY CASCADE;


-- Vol 1
UPDATE Reservation SET date_reservation = '2025-08-20' WHERE id_reservation = '1';
UPDATE Reservation SET date_reservation = '2025-08-21' WHERE id_reservation = '2';
UPDATE Reservation SET date_reservation = '2025-08-21' WHERE id_reservation = '3';
UPDATE Reservation SET date_reservation = '2025-08-28' WHERE id_reservation = '4';
UPDATE Reservation SET date_reservation = '2025-08-29' WHERE id_reservation = '5';
UPDATE Reservation SET date_reservation = '2025-09-01' WHERE id_reservation = '6';
UPDATE Reservation SET date_reservation = '2025-09-02' WHERE id_reservation = '7';

-- Vol 2
UPDATE Reservation SET date_reservation = '2025-09-01' WHERE id_reservation = '8';
UPDATE Reservation SET date_reservation = '2025-09-02' WHERE id_reservation = '9';
UPDATE Reservation SET date_reservation = '2025-09-08' WHERE id_reservation = '10';
UPDATE Reservation SET date_reservation = '2025-09-10' WHERE id_reservation = '11';

