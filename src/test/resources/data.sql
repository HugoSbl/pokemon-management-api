INSERT INTO dresseur (nom, region) VALUES ('Sacha', 'Kanto');
INSERT INTO dresseur (nom, region) VALUES ('Ondine', 'Kanto');
INSERT INTO dresseur (nom, region) VALUES ('Flora', 'Sinnoh');

INSERT INTO pokemon (nom, type, niveau, dresseur_id) VALUES ('Pikachu', 'Électrik', 50, (SELECT id FROM dresseur WHERE nom = 'Sacha'));
INSERT INTO pokemon (nom, type, niveau, dresseur_id) VALUES ('Dracaufeu', 'Feu', 40, (SELECT id FROM dresseur WHERE nom = 'Sacha'));
INSERT INTO pokemon (nom, type, niveau, dresseur_id) VALUES ('Carapuce', 'Eau', 20, (SELECT id FROM dresseur WHERE nom = 'Sacha'));
INSERT INTO pokemon (nom, type, niveau, dresseur_id) VALUES ('Staross', 'Eau', 30, (SELECT id FROM dresseur WHERE nom = 'Ondine'));
