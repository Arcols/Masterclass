INSERT INTO `User`
(USE_id, USE_firstname, USE_lastname, USE_mail, USE_description, USE_password)
VALUES
    ('4a357732-ad63-4e78-9bd6-8af6333c41fc', 'admin', 'admin', 'fil.masterclass@gmail.com', 'Lorem ipsum dolor sit amet...', '$2a$10$a0.lhDziyk13h8vHVnFobecAApTf7r6.v0L76roHCDlggKGLX/yIK');

-- GROUPS

INSERT INTO `Group`
(GRO_id, GRO_name)
VALUES
    ('FIL A1', 'FIL A1'),
    ('FIL A2', 'FIL A2'),
    ('FIL A3', 'FIL A3'),
    ('ANG G1', 'ANG G1'),
    ('ANG G2', 'ANG G2');

-- USER_GROUP

INSERT INTO User_Group
(USE_id, GRO_id)
VALUES
    ('4a357732-ad63-4e78-9bd6-8af6333c41fc', 'FIL A1');

-- SUBJECTS

INSERT INTO Subject
(SUB_id, SUB_name)
VALUES
    ('S1', 'Mathématiques'),
    ('S2', 'Mathématiques discrètes'),
    ('S3', 'Anglais'),
    ('S4', 'Web Dev'),
    ('S5', 'IHM'),
    ('S6', 'Java'),
    ('S7', 'Autre'),
    ('S8', 'Mathématiques de base'),
    ('S9', 'Informatique');

-- EVENTS

INSERT INTO Event
(EVE_id,EVE_type,EVE_title,EVE_date,EVE_starthour,EVE_endhour,EVE_description,EVE_location,EVE_submissionLink,SUB_id,USE_id,GRO_id)
VALUES

    ('E1','devoir','Devoir Analyse',
     '2026-06-22','08:00:00','09:00:00',
     'Faire les exercices 1 à 5 du chapitre sur les intégrales et réviser le cours sur les dérivées.',
     'J147',NULL,'S8','4a357732-ad63-4e78-9bd6-8af6333c41fc',
     'FIL A1'),

    ('E2','devoir','CV en anglais',
     '2026-06-11','19:00:00','20:30:00',
     'Rédiger et mettre en page un CV professionnel en anglais pour la future recherche de stage.',
     'J147',
     'https://moodle.imt-atlantique.fr/mod/assign/view.php?id=12345',
     'S3','4a357732-ad63-4e78-9bd6-8af6333c41fc',
     'ANG G1'),

    ('E3','activite','Showcase Yanis',
     '2026-06-12','12:00:00','13:00:00',
     'Concert exclusif de Yanis organisé par l''association étudiante pour fêter la fin des partiels.',
     'MDE',NULL,NULL,'4a357732-ad63-4e78-9bd6-8af6333c41fc',
     'FIL A1'),

    ('E4','sport','Tournoi BDE',
     '2026-06-13','17:00:00','23:59:00',
     'Tournoi inter-promos de volley-ball et futsal. Ramener une tenue de sport et une bouteille d''eau.',
     'Gymnase',NULL,NULL,'4a357732-ad63-4e78-9bd6-8af6333c41fc',
     'FIL A1'),

    ('E5','devoir','Projet final - Mathématiques',
     '2026-06-15','11:00:00','12:00:00',
     'Finaliser et rendre le projet de synthèse sur les intégrales et leurs applications.',
     'J147',NULL,'S8','4a357732-ad63-4e78-9bd6-8af6333c41fc',
     'FIL A1'),

    ('E6','devoir','Partiel Informatique',
     '2026-06-16','15:00:00','16:00:00',
     'Évaluation sur les bases de données et les structures de données.',
     'Amphi A',NULL,'S9','4a357732-ad63-4e78-9bd6-8af6333c41fc',
     'FIL A1'),

    ('E7','examen','Présentation orale Anglais',
     '2026-06-17','09:00:00','10:15:00',
     'Présentation individuelle sur un sujet d''actualité en anglais.',
     'J147',NULL,'S3','4a357732-ad63-4e78-9bd6-8af6333c41fc',
     'FIL A1'),

    ('E8','activite','Réunion projet de groupe',
     '2026-06-19','15:00:00','16:30:00',
     'Point d''avancement et répartition des tâches pour le projet final du semestre.',
     'Salle de réunion',NULL,NULL,'4a357732-ad63-4e78-9bd6-8af6333c41fc',
     'FIL A1'),

    ('E9','examen','DS Graphes',
     '2026-06-19','10:00:00','12:00:00',
     'Devoir surveillé sur les graphes et leurs applications.',
     'J147',NULL,'S1','4a357732-ad63-4e78-9bd6-8af6333c41fc',
     'FIL A1');

-- NOTES

INSERT INTO Note
(NOT_id, NOT_content, NOT_date, EVE_id, USE_id)
VALUES

    ('N1',
     'Penser à imprimer la feuille d''exercices avant le cours.',
     '2026-06-22 10:00:00',
     'E1','4a357732-ad63-4e78-9bd6-8af6333c41fc');


-- COMMENTS

INSERT INTO Comment
(COM_id, COM_content, COM_date, EVE_id, USE_id)
VALUES

    ('C2',
     'Merci Damien ! Est-ce que quelqu''un sait si la calculatrice est autorisée ?',
     '2026-06-18 08:15:00',
     'E1','4a357732-ad63-4e78-9bd6-8af6333c41fc'),

    ('C3',
     'Bonjour à tous, oui la calculatrice type collège est autorisée.',
     '2026-06-18 18:30:00',
     'E1','4a357732-ad63-4e78-9bd6-8af6333c41fc'),

    ('C4',
     'J''ai trouvé un super template de CV sur Canva, je peux le partager si vous voulez.',
     '2026-06-10 14:00:00',
     'E2','4a357732-ad63-4e78-9bd6-8af6333c41fc'),

    ('C5',
     'Le partiel portera uniquement sur SQL ou aussi sur NoSQL ?',
     '2026-06-15 10:00:00',
     'E6','4a357732-ad63-4e78-9bd6-8af6333c41fc');

UPDATE Event SET EVE_type = 'DEVOIR' WHERE EVE_type = 'devoir';
UPDATE Event SET EVE_type = 'EXAMEN' WHERE EVE_type = 'examen';
UPDATE Event SET EVE_type = 'ACTIVITE' WHERE EVE_type = 'activite';
UPDATE Event SET EVE_type = 'SPORT' WHERE EVE_type = 'sport';
