-- src/test/resources/import.sql
MERGE INTO "User" (USE_id, USE_firstname, USE_lastname, USE_mail, USE_description, USE_password)
    KEY(USE_id) VALUES ('U1', 'Alice', 'Dupont', 'alice@mail.com', 'Admin', 'hash');

MERGE INTO "Group" (GRO_id, GRO_name)
    KEY(GRO_id) VALUES ('G1', 'Administrateurs');
-- etc.
