package com.sofka.bingo.repository;

import com.sofka.bingo.domain.Carton;
import com.sofka.bingo.domain.Jugador;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface CartonRepository extends CrudRepository<Carton, Integer> {
    // Encontrar Jugador por su ID de usuario
    @Query("SELECT j FROM Jugador j WHERE j.id = :id")
    Jugador findByUsuarioId(@Param("id") Integer id);

    // Encontrar los valores de las columnas B, I, N, G y O de un Cartón por su ID
    @Query("SELECT j.b,j.i,j.n,j.g,j.o FROM Carton j WHERE j.idCarton = :id")
    List<String> findByIdB(@Param("id") Integer id);

    // Encontrar el ID del Cartón de un Jugador según su estado
    @Query("SELECT j.idCarton FROM Carton j WHERE j.idJugador = :id AND j.estado = :estado" )
    Integer findByIdJugador(@Param("id") Jugador id, @Param("estado") String estado);

    // Encontrar un Cartón por su ID
    @Query("SELECT j FROM Carton j WHERE j.idCarton = :id")
    Carton findByIdCarton(@Param("id") Integer id);


}
