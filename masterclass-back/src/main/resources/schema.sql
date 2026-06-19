DROP TABLE IF EXISTS `Event_Completion`;
DROP TABLE IF EXISTS `User_Group`;
DROP TABLE IF EXISTS `Comment`;
DROP TABLE IF EXISTS `Note`;
DROP TABLE IF EXISTS `Event`;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS `Group`;
DROP TABLE IF EXISTS `Subject`;

CREATE TABLE IF NOT EXISTS  `User`(
                       USE_id VARCHAR(50),
                       USE_firstname VARCHAR(50),
                       USE_lastname VARCHAR(50),
                       USE_mail VARCHAR(50),
                       USE_description VARCHAR(50),
                       USE_password VARCHAR(255),
                       PRIMARY KEY(USE_id)
);

CREATE TABLE IF NOT EXISTS `Group`(
                        GRO_id VARCHAR(50),
                        GRO_name VARCHAR(50),
                        PRIMARY KEY(GRO_id)
);

CREATE TABLE IF NOT EXISTS Subject(
                        SUB_id VARCHAR(50),
                        SUB_name VARCHAR(50),
                        PRIMARY KEY(SUB_id)
);

CREATE TABLE IF NOT EXISTS Event(
                      EVE_id VARCHAR(50),
                      EVE_type VARCHAR(50),
                      EVE_title VARCHAR(50),
                      EVE_date DATE,
                      EVE_starthour TIME,
                      EVE_endhour TIME,
                      EVE_description TEXT,
                      EVE_location VARCHAR(50),
                      EVE_submissionLink TEXT,
                      SUB_id VARCHAR(50),
                      USE_id VARCHAR(50) NOT NULL,
                      GRO_id VARCHAR(50) NOT NULL,
                      PRIMARY KEY(EVE_id),
                      FOREIGN KEY(SUB_id) REFERENCES Subject(SUB_id),
                      FOREIGN KEY(USE_id) REFERENCES `User`(USE_id),
                      FOREIGN KEY(GRO_id) REFERENCES `Group`(GRO_id)
);

CREATE TABLE IF NOT EXISTS Note(
                     NOT_id VARCHAR(50),
                     NOT_content TEXT,
                     NOT_date TIMESTAMP,
                     EVE_id VARCHAR(50) NOT NULL,
                     USE_id VARCHAR(50) NOT NULL,
                     PRIMARY KEY(NOT_id),
                     FOREIGN KEY(EVE_id) REFERENCES Event(EVE_id),
                     FOREIGN KEY(USE_id) REFERENCES `User`(USE_id)
);

CREATE TABLE IF NOT EXISTS Comment(
                        COM_id VARCHAR(50),
                        COM_content TEXT,
                        COM_date TIMESTAMP,
                        EVE_id VARCHAR(50) NOT NULL,
                        USE_id VARCHAR(50) NOT NULL,
                        PRIMARY KEY(COM_id),
                        FOREIGN KEY(EVE_id) REFERENCES Event(EVE_id),
                        FOREIGN KEY(USE_id) REFERENCES `User`(USE_id)
);

CREATE TABLE IF NOT EXISTS User_Group(
                           USE_id VARCHAR(50),
                           GRO_id VARCHAR(50),
                           PRIMARY KEY(USE_id, GRO_id),
                           FOREIGN KEY(USE_id) REFERENCES `User`(USE_id),
                           FOREIGN KEY(GRO_id) REFERENCES `Group`(GRO_id)
);

CREATE TABLE IF NOT EXISTS Event_Completion (
                           id VARCHAR(50) PRIMARY KEY,
                           USE_id VARCHAR(50) NOT NULL,
                           EVE_id VARCHAR(50) NOT NULL,
                           FOREIGN KEY(USE_id) REFERENCES `User`(USE_id),
                           FOREIGN KEY(EVE_id) REFERENCES Event(EVE_id),
                           UNIQUE(USE_id, EVE_id) -- Un étudiant ne peut valider un devoir qu'une seule fois
    );
