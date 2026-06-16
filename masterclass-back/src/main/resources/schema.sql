DROP TABLE IF EXISTS `User_Group`;
DROP TABLE IF EXISTS `Comment`;
DROP TABLE IF EXISTS `Note`;
DROP TABLE IF EXISTS `Event`;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS `Group`;
DROP TABLE IF EXISTS `Subject`;

CREATE TABLE `User`(
                       USE_id VARCHAR(50),
                       USE_firstname VARCHAR(50),
                       USE_lastname VARCHAR(50),
                       USE_mail VARCHAR(50),
                       USE_description VARCHAR(50),
                       USE_password VARCHAR(50),
                       PRIMARY KEY(USE_id)
);

CREATE TABLE `Group`(
                        GRO_id VARCHAR(50),
                        GRO_name VARCHAR(50),
                        PRIMARY KEY(GRO_id)
);

CREATE TABLE Subject(
                        SUB_id VARCHAR(50),
                        SUB_name VARCHAR(50),
                        PRIMARY KEY(SUB_id)
);

CREATE TABLE Event(
                      EVE_id VARCHAR(50),
                      EVE_type VARCHAR(50),
                      EVE_title VARCHAR(50),
                      EVE_date DATE,
                      EVE_starthour TIME,
                      EVE_endhour TIME,
                      EVE_description TEXT,
                      EVE_localisation VARCHAR(50),
                      EVE_lien VARCHAR(50),
                      SUB_id VARCHAR(50),
                      USE_id VARCHAR(50) NOT NULL,
                      PRIMARY KEY(EVE_id),
                      FOREIGN KEY(SUB_id) REFERENCES Subject(SUB_id),
                      FOREIGN KEY(USE_id) REFERENCES `User`(USE_id)
);

CREATE TABLE Note(
                     NOT_id VARCHAR(50),
                     NOT_content TEXT,
                     NOT_date TIMESTAMP,
                     EVE_id VARCHAR(50) NOT NULL,
                     USE_id VARCHAR(50) NOT NULL,
                     PRIMARY KEY(NOT_id),
                     FOREIGN KEY(EVE_id) REFERENCES Event(EVE_id),
                     FOREIGN KEY(USE_id) REFERENCES `User`(USE_id)
);

CREATE TABLE Comment(
                        COM_id VARCHAR(50),
                        COM_content TEXT,
                        COM_date TIMESTAMP,
                        EVE_id VARCHAR(50) NOT NULL,
                        USE_id VARCHAR(50) NOT NULL,
                        PRIMARY KEY(COM_id),
                        FOREIGN KEY(EVE_id) REFERENCES Event(EVE_id),
                        FOREIGN KEY(USE_id) REFERENCES `User`(USE_id)
);

CREATE TABLE User_Group(
                           USE_id VARCHAR(50),
                           GRO_id VARCHAR(50),
                           PRIMARY KEY(USE_id, GRO_id),
                           FOREIGN KEY(USE_id) REFERENCES `User`(USE_id),
                           FOREIGN KEY(GRO_id) REFERENCES `Group`(GRO_id)
);