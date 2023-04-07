package com.sofka.bingo.service;

import com.sofka.bingo.domain.Balotas;
import com.sofka.bingo.domain.Carton;
import com.sofka.bingo.domain.InfoJuego;
import com.sofka.bingo.domain.Juego;
import com.sofka.bingo.domain.Jugador;


import java.util.List;


public interface Ibingo {

        //jugador
        public Jugador createJugador(Jugador jugador);
        public Jugador searchJugadorId(Integer idJugador);


        //juego
        public Juego getJuegoById(Integer id);
        public Juego startJuego(Integer id);
        public Integer buscarEstado(String estado);
        public Juego buscarEstadoJuego(String estado);



        //carton

        public Carton createNumber(Jugador jugador);
        public  Carton updateCarton(Jugador jugador);
        public  List<String> getCartonesByJugador(Jugador jugador);



        //balotas
        public Juego createBalota(Integer id);
        public List<Balotas> buscarBalotasPorIdJuego(Juego idJuego);

        public boolean verificarGanador(Jugador idJugador);




        //InfoJuego
        public InfoJuego createInfo(Juego juegoEnEspera, Carton cartonById);
        public InfoJuego deleInfo(Integer idCarton);
}
