package com.sofka.bingo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name="juego")
public class Juego implements Serializable {
    // Indica que este atributo es el identificador de la entidad
    @Id
    // Indica que el valor del identificador se generará automáticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_juego")
    private Integer idJuego;

    // Atributo que indica el estado del juego
    @Column(name = "estado")
    private String estado;

    // Atributo que indica el jugador ganador del juego
    @Column(name = "ganador")
    private String ganador;

}
