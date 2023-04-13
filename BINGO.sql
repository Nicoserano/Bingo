USE bingo;

Create Table jugador(
id_jugador INT NOT NULL AUTO_INCREMENT,
usuario VARCHAR(50) NOT NULL,
PRIMARY KEY (id_jugador)
);

CREATE TABLE carton(
	id_carton INT NOT NULL AUTO_INCREMENT,
    id_jugador INT NOT NULL,
	B Text, 
    I TEXT,
    N TEXT,
    G TEXT,
    O TEXT,
    estado varchar(45) NOT NULL,
    PRIMARY KEY (id_carton),
    FOREIGN KEY(id_jugador) REFERENCES jugador(id_jugador)
);

create Table juego(
	id_juego INT NOT NULL AUTO_INCREMENT,
    estado VarCHAR(50) NOT NULL,
    ganador VarCHAR(60) ,
    PRIMARY KEY (id_juego )
    );
    
CREATE TABLE infojuego(
    id_info INT NOT NULL AUTO_INCREMENT,
	id_juego INT NOT NULL ,
    id_carton INT NOT NULL ,
     PRIMARY KEY (id_info ),
    FOREIGN KEY(id_carton) REFERENCES carton(id_carton),
    FOREIGN KEY(id_juego) REFERENCES juego(id_juego)
);


CREATE TABLE balotas(
	id_balotas INT NOT NULL AUTO_INCREMENT,
    id_juego INT NOT NULL,
    balota INT,
    PRIMARY KEY (id_balotas ),
    FOREIGN KEY(id_juego) REFERENCES juego(id_juego)
);