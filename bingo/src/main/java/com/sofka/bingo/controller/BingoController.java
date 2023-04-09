package com.sofka.bingo.controller;

import com.sofka.bingo.domain.Balotas;
import com.sofka.bingo.domain.Carton;
import com.sofka.bingo.domain.InfoJuego;
import com.sofka.bingo.domain.Juego;
import com.sofka.bingo.domain.Jugador;
import com.sofka.bingo.repository.CartonRepository;
import com.sofka.bingo.repository.InfoJuegoRepository;
import com.sofka.bingo.repository.JuegoRepository;
import com.sofka.bingo.repository.JugadorRepository;
import com.sofka.bingo.service.BingoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableScheduling // Habilita la programación de tareas en el contexto de Spring
@Controller // Indica que la clase es un controlador
public class BingoController {

    // Inyección de dependencias de los repositorios y del servicio correspondiente
    @Autowired
    private BingoService bingoService ;
    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private CartonRepository cartonRepository;
    @Autowired
    private JuegoRepository juegoRepository;
    @Autowired
    private InfoJuegoRepository infoJuegoRepository;


    // Endpoint para guardar un usuario en la base de datos
    @PostMapping("/guardar-usuario")
    public ResponseEntity<String> guardarUsuario(@RequestBody Jugador player) {
        String usuario=player.getUsuario(); // Obtiene el nombre de usuario del objeto jugador recibido en el request
        Jugador jugador= new Jugador(); // Crea un nuevo objeto jugador
        jugador.setUsuario(usuario); // Asigna el nombre de usuario al objeto jugador
        bingoService.createJugador(jugador); // Llama al método de servicio para guardar el jugador en la base de datos
        return ResponseEntity.ok("Usuario guardado exitosamente"); // Retorna una respuesta satisfactoria con un mensaje de éxito
    }

    // Endpoint para asignar un cartón al usuario
    @PostMapping ("/inicio")
    public ResponseEntity buscarUsuario(@RequestBody Jugador player){
        String usuario=player.getUsuario(); // Obtiene el nombre de usuario del objeto jugador recibido en el request
        Jugador jugadorId=jugadorRepository.findByUsuario(usuario); // Busca al jugador en la base de datos por su nombre de usuario
        bingoService.updateCarton(jugadorId); // Llama al método de servicio para asignar un cartón al jugador
        return  ResponseEntity.ok(jugadorId); // Retorna una respuesta satisfactoria con el objeto jugador actualizado
    }

    // Método para crear un juego automáticamente cada diez minutos
    @Scheduled(fixedRate = 600000) // Ejecutar cada 10 minutos (600000 milisegundos)
    public void crearJuegoAutomatico() {
        Juego juego =new Juego(); // Crea un nuevo objeto juego
        juego.setEstado("Creado"); // Asigna el estado "Creado" al objeto juego
        juegoRepository.save(juego); // Guarda el objeto juego en la base de datos
        bingoService.buscarEstado("Creado"); // Llama al método de servicio para buscar juegos en estado "Creado"
        bingoService.startJuego(juego.getIdJuego()); // Llama al método de servicio para iniciar el juego con el id del objeto juego recién creado
    }

    // Endpoint para buscar las balotas de un juego por su id
    @GetMapping("/juegos/balotas")
    public ResponseEntity<List> buscarBalotasPorIdJuego( ) {
        Juego juego=bingoService.buscarEstadoJuego("en juego"); // Llama al método de servicio para buscar el juego actual en estado "en juego"
        List<Balotas> balotas = bingoService.buscarBalotasPorIdJuego(juego); // Llama al método de servicio para buscar las balotas del juego actual
        if (balotas.isEmpty()) { // Verifica si la lista de balotas está vacía
            return ResponseEntity.notFound().build(); // Retorna una respuesta con un error 404
        }
        List balotaList = new ArrayList<>(); // Crea una nueva lista para almacenar las balotas
        for(Balotas balota : balotas) { // Itera sobre la lista de balotas
            balotaList.add(balota.getBalota()); // Agrega la balota actual a la nueva lista de balotas
        }
        return ResponseEntity.ok(balotaList); // Retorna una respuesta satisfactoria con la lista de balotas actualizada
    }


    @GetMapping("/inicio/{id_jugador}/carton")
    public ResponseEntity buscarCartonPorId(@PathVariable Integer id_jugador) {
        // Busca al jugador por su ID
        Jugador jugador = bingoService.searchJugadorId(id_jugador);
        // Obtiene la lista de cartones del jugador
        List<String> carton = bingoService.getCartonesByJugador(jugador);
        // Concatena los elementos de la lista con una coma
        return ResponseEntity.ok(carton);
    }

    @PostMapping("/inicio/{id_jugador}/info")
    public  ResponseEntity guardarInfo(@PathVariable Integer id_jugador){
        // Busca al jugador por su ID
        Jugador jugador = bingoService.searchJugadorId(id_jugador);
        // Busca el cartón que está en uso por el jugador
        Carton carton= infoJuegoRepository.findByIdCartonJugador(jugador,"en uso");
        // Busca el juego que está en espera
        Juego juego=infoJuegoRepository.findIdByEstadoJuego("en espera");
        // Si no hay juegos en espera, devuelve una respuesta sin contenido
        if (juego==null){
            return ResponseEntity.ok("no hay juego disponible");

        }
        // Crea un objeto InfoJuego con el juego en espera y el cartón en uso
        InfoJuego infoJuego=bingoService.createInfo(juego,carton);
        // Devuelve el objeto InfoJuego creado
        return ResponseEntity.ok(infoJuego);
    }

    @DeleteMapping("/inicio/{id_jugador}/info/eliminar")
    public ResponseEntity eliminarInfo(@PathVariable Integer id_jugador){
        // Busca al jugador por su ID
        Jugador jugador = bingoService.searchJugadorId(id_jugador);
        // Busca el cartón que está en uso por el jugador
        Carton carton= infoJuegoRepository.findByIdCartonJugador(jugador,"en uso");
        // Busca la información del juego correspondiente al cartón en uso

        Juego juego=infoJuegoRepository.findByIdsAndEstadoNotTerminado();
        Integer idsJuego=infoJuegoRepository.findByCartonIds(carton,juego);

        // Si no hay información de juego para el cartón en uso, devuelve una respuesta sin contenido
        if (idsJuego==null){
            return ResponseEntity.ok("no hay juego disponible");
        }
        // Elimina la información del juego correspondiente al cartón en uso
        infoJuegoRepository.deleteByCartonId(idsJuego);
        // Devuelve la información del juego eliminada
        return ResponseEntity.ok("Eliminado");

    }
    @PostMapping("/inicio/{id_jugador}/ganador")
    public ResponseEntity ganador(@PathVariable Integer id_jugador){
        // Busca al jugador por su ID
        Jugador jugador = bingoService.searchJugadorId(id_jugador);
        // Verifica si el jugador es un ganador
        boolean ganador= bingoService.verificarGanador(jugador);
        // Si el jugador no es un ganador, devuelve una respuesta sin contenido
        if(ganador==false){
            return ResponseEntity.ok("no hay ganador");
        }
        // Devuelve el nombre de usuario del jugador
        return  ResponseEntity.ok(jugador.getUsuario());

    }

    /**
     * Devuelve el juego en estado diferente de "terminado".
     * @return El juego en estado diferente de "terminado".
     */
    @GetMapping("/estado")
    public ResponseEntity<Juego> estado(){
      Juego estado= juegoRepository.findEstado("terminado");
      return ResponseEntity.ok(estado);
    }

    /**
     * Devuelve el juego en estado diferente de "terminado" con un ganador.
     * @return El juego en estado diferente de "terminado" con un ganador.
     */
    @GetMapping("/ganador")
    public ResponseEntity<Juego> ganador(){
        Juego ganador =juegoRepository.ganadorFindEstado("terminado");
        return ResponseEntity.ok(ganador);
    }




}
