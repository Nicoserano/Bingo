package com.sofka.bingo.repository;

import com.sofka.bingo.domain.Carton;
import com.sofka.bingo.domain.InfoJuego;
import com.sofka.bingo.domain.Juego;
import com.sofka.bingo.domain.Jugador;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InfoJuegoRepository  extends CrudRepository<InfoJuego, Integer> {

    // Encuentra el cartón del jugador en un estado específico
    @Query("SELECT j FROM Carton j WHERE j.idJugador = :id AND j.estado = :estado")
    Carton findByIdCartonJugador(@Param("id") Jugador id, @Param("estado") String estado);

    // Encuentra el juego en un estado específico
    @Query("SELECT j FROM Juego j WHERE j.estado = :estado")
    Juego findIdByEstadoJuego(@Param("estado") String estado);

    // Encuentra el id de la información del juego por el cartón
    @Query("SELECT j.id_info FROM InfoJuego j WHERE j.carton = :id AND j.juego=:idJuego")
    Integer findByCartonIds(@Param("id") Carton id,@Param("idJuego") Juego idJuego);
    @Query("SELECT j FROM Juego j WHERE j.estado !=  'terminado'")
    Juego findByIdsAndEstadoNotTerminado();


    // Elimina la información del juego por el id del cartón
    @Transactional
    @Modifying
    @Query("DELETE  FROM InfoJuego i WHERE i.id_info = :id")
    void deleteByCartonId(@Param("id") Integer id);
}
