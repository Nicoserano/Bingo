package com.sofka.bingo.repository;

import com.sofka.bingo.domain.Balotas;
import com.sofka.bingo.domain.Juego;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
/*
Interfaz BalotaRepository
Extiende de la interfaz CrudRepository para proveer operaciones básicas de CRUD en la entidad Balotas
*/

public interface BalotaRepository extends CrudRepository<Balotas, Integer> {
        /*
         * Consulta personalizada para buscar todas las balotas de un juego específico
         * @param idJuego el identificador del juego del que se desean obtener las balotas
         * @return una lista de todas las balotas pertenecientes al juego con el id especificado
         */
        @Query("SELECT b FROM Balotas b WHERE b.juego = :idJuego")
        List<Balotas> findByJuegoId(@Param("idJuego") Juego idJuego);

        /*
         * Consulta personalizada para buscar el juego que coincide con el estado especificado
         * @param estado el estado del juego que se desea buscar
         * @return el juego que coincide con el estado especificado
         */
        @Query("SELECT j FROM Juego j WHERE j.estado = :estado")
        Juego findIdByEstadoJuego(@Param("estado") String estado);
}
