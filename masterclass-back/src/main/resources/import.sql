INSERT IGNORE INTO `User` (USE_id, USE_firstname, USE_lastname, USE_mail, USE_description, USE_password) VALUES
                                                                                                      ('U1', 'Alice', 'Dupont', 'alice@mail.com', 'Admin système', 'hash_admin123'),
                                                                                                      ('U2', 'Bob', 'Martin', 'bob@mail.com', 'Professeur', 'hash_prof456'),
                                                                                                      ('U3', 'Charlie', 'Durand', 'charlie@mail.com', 'Étudiant', 'hash_etu789');

INSERT IGNORE INTO `Group` (GRO_id, GRO_name) VALUES
                                           ('G1', 'Administrateurs'),
                                           ('G2', 'Professeurs'),
                                           ('G3', 'Étudiants'),
                                           ('FIL A1', 'FIL A1'),
                                           ('ANG G1', 'ANG G1'),
                                           ('ANG G2', 'ANG G2');

INSERT IGNORE INTO User_Group (USE_id, GRO_id) VALUES
                                            ('U1', 'G1'),
                                            ('U1', 'G2'),
                                            ('U2', 'G2'),
                                            ('U3', 'G3');

INSERT IGNORE INTO Subject (SUB_id, SUB_name) VALUES
                                           ('S1', 'Mathématiques'),
                                           ('S2', 'Informatique'),
                                           ('S3', 'Gestion de projet');

INSERT IGNORE INTO Event (EVE_id, EVE_type, EVE_title, EVE_date, EVE_starthour, EVE_endhour, EVE_description, EVE_location, EVE_submissionLink, SUB_id, USE_id, GRO_id) VALUES
                                                                                                                                                       ('E1', 'Cours', 'Introduction à PostgreSQL', '2024-06-20', '09:00:00', '11:00:00', 'Bases de données relationnelles', 'Salle 101', 'http://visio.link/pg', 'S2', 'U2', 'FIL A1'),
                                                                                                                                                       ('E2', 'Examen', 'Partiel de Maths', '2024-06-25', '14:00:00', '16:00:00', 'Algèbre linéaire', 'Amphi B', NULL, 'S1', 'U2', 'FIL A1');

INSERT IGNORE INTO Note (NOT_id, NOT_content, NOT_date, EVE_id, USE_id) VALUES
    ('N1', 'Ne pas oublier de réviser les jointures externes', '2024-06-20 11:15:00', 'E1', 'U3');

INSERT IGNORE INTO Comment (COM_id, COM_content, COM_date, EVE_id, USE_id) VALUES
    ('C1', 'Est-ce que le support de cours sera en ligne ?', '2024-06-20 12:00:00', 'E1', 'U3');
