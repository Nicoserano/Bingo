package com.sofka.bingo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name="carton")
public class Carton implements Serializable {
    // Identificador único del cartón
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carton")
    private Integer idCarton;

    // Jugador dueño del cartón
    @ManyToOne
    @JoinColumn(name = "id_jugador")
    private Jugador idJugador;

    // Columna B del cartón, con los números correspondientes
    @Column(name = "B", columnDefinition = "TEXT")
    private String b;

    // Columna I del cartón, con los números correspondientes
    @Column(name = "I" ,columnDefinition = "TEXT")
    private String i;

    // Columna N del cartón, con los números correspondientes
    @Column(name = "N" ,columnDefinition = "TEXT")
    private String n;

    // Columna G del cartón, con los números correspondientes
    @Column(name = "G",columnDefinition = "TEXT")
    private String g;

    // Columna O del cartón, con los números correspondientes
    @Column(name = "O",columnDefinition = "TEXT")
    private String o;

    // Estado del cartón
    @Column(name = "estado",columnDefinition = "estado")
    private String estado;
}

