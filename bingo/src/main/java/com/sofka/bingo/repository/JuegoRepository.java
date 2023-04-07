package com.sofka.bingo.repository;

import com.sofka.bingo.domain.Juego;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface JuegoRepository extends CrudRepository<Juego, Integer> {
    /**
     Este método realiza una consulta a la base de datos para buscar el ID del juego cuyo estado se indica como
     parámetro.
     @param estado el estado del juego que se busca.
     @return el ID del juego cuyo estado coincide con el indicado, o null si no se encuentra ningún juego con
     ese estado.
     */
    @Query("SELECT j.idJuego FROM Juego j WHERE j.estado = :estado")
    Integer findIdByEstado(@Param("estado") String estado);

    @Query("SELECT j FROM Juego j WHERE j.estado != :estado")
    Juego findEstado(@Param("estado") String estado);



}
