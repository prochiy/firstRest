
CREATE TABLE rest_user
    (id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    family varchar(20) NOT NULL,
    imageURL varchar(100) NOT NULL,
    status TINYINT(1) NOT NULL DEFAULT 0,
    email varchar(20),
    age INTEGER,
    timestamp INTEGER NOT NULL DEFAULT -1,
    PRIMARY KEY (id)
    );




