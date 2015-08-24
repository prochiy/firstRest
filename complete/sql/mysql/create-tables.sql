CREATE TABLE image
   (id INTEGER NOT NULL AUTO_INCREMENT,
   PRIMARY KEY (id));

CREATE TABLE rest_user
    (id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    family varchar(20) NOT NULL,
    image_id INTEGER NOT NULL,
        CONSTRAINT image_fk FOREIGN KEY (image_id) REFERENCES image (id),
    status TINYINT(1) NOT NULL DEFAULT 0,
    email varchar(20),
    age INTEGER,
    timestamp INTEGER NOT NULL DEFAULT -1,
    PRIMARY KEY (id)
    );




