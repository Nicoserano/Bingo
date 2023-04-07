package com.sofka.bingo.repository;

import com.sofka.bingo.domain.Carton;
import com.sofka.bingo.domain.Jugador;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JugadorRepository extends CrudRepository<Jugador, Integer> {


    // Método para buscar un jugador por su nombre de usuario
    Jugador findByUsuario(String usuario);



}
