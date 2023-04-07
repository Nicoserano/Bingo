package com.sofka.bingo.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
@Data // Utiliza Lombok para generar getters, setters y otros métodos básicos automáticamente.
@Entity // Anotación para indicar que esta clase es una entidad que se mapeará en una tabla de la base de datos.
@Table(name="jugador")
public class Jugador implements Serializable {
    @Id // Indica que este campo es la clave primaria de la entidad.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que el valor de esta clave se generará automáticamente.
    @Column(name = "id_jugador") // Indica que este campo se mapeará a la columna "id_contacto" en la tabla.
    private Integer id ;

    @Column(name = "usuario") // Indica que este campo se mapeará a la columna "nombre" en la tabla.
    private String usuario;

}
