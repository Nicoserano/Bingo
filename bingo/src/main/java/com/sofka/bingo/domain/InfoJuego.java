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
@Table(name = "infojuego")
public class InfoJuego implements Serializable {
    // Identificador único de la información del juego
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_info")
    private int id_info;
    // Cartón asociado a la información del juego
    @ManyToOne
    @JoinColumn(name = "id_carton")
    private Carton carton;
    // Juego asociado a la información del juego
    @ManyToOne
    @JoinColumn(name = "id_juego")
    private Juego juego;
}