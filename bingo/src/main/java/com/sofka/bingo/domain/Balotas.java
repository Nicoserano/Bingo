package com.sofka.bingo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
// Clase modelo para la tabla de Balotas
@Data
@Entity
@Table(name="balotas")
public class Balotas implements Serializable {
    // Id auto-generado para la tabla de Balotas
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_balotas")
    private Integer idBalotas;

    // Relación many-to-one con la tabla de Juego, usando lazy fetching
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_juego", referencedColumnName = "id_juego")
    private Juego juego;

    // Valor de la balota para ser registrado en la tabla
    @Column(name = "balota")
    private Integer balota;
}

