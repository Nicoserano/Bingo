package com.sofka.bingo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.bingo.domain.*;
import com.sofka.bingo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class BingoService implements Ibingo{

    // Repositorios para las entidades involucradas en el juego
    @Autowired
    private CartonRepository cartonRepository;
    @Autowired
    private JuegoRepository juegoRepository;
    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private BalotaRepository balotaRepository;
    @Autowired
    private InfoJuegoRepository infoJuegoRepository;


    // ScheduledExecutorService y ScheduledFuture para programar tareas
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> juegoTask;


    // Método para crear un nuevo jugador
    @Override
    public Jugador createJugador(Jugador jugador) {
        return jugadorRepository.save(jugador); // Guardar jugador en la base de datos
    }

    // Método para obtener un juego según su ID
    @Override
    public Juego getJuegoById(Integer id) {
        Optional<Juego> juego = juegoRepository.findById(id); // Buscar juego en la base de datos
        if (juego.isPresent()) {
            return juego.get(); // Devolver juego si está presente
        } else {
            throw new NoSuchElementException("Juego no encontrado"); // Lanzar excepción si el juego no está presente
        }
    }

    // Método para iniciar un juego según su ID
    @Override
    public Juego startJuego(Integer id) {
        Juego juego = getJuegoById(id); // Obtener juego según su ID
        juego.setEstado("en espera"); // Actualizar estado del juego a "en espera"
        juegoRepository.save(juego); // Guardar juego en la base de datos

        // Programar tarea para cambiar el estado del juego a "en juego" y crear una balota después de 5 minutos
        juegoTask = scheduler.schedule(() -> {
            juego.setEstado("en juego");
            juegoRepository.save(juego);
            if (juego.getGanador() == null) {
                createBalota(id);
            }
            // Programar tarea para cambiar el estado del juego a "terminado" después de otros 5 minutos
            scheduler.schedule(() -> {
                Juego juegoactual = juegoRepository.findEstadoJuego("en juego");
                if (juegoactual.getEstado().equals("en juego")){
                    juego.setEstado("terminado");
                    juegoRepository.save(juego);
                }
                else {
                    System.out.println("funcione");
                }
            }, 5, TimeUnit.MINUTES);
        }, 3, TimeUnit.MINUTES);

        return juego; // Devolver juego actualizado
    }

    // Método para buscar el ID de un juego según su estado
    @Override
    public Integer buscarEstado(String estado) {
        return juegoRepository.findIdByEstado(estado); // Buscar ID de juego en la base de datos según su estado
    }

    // Método para buscar un juego según su estado
    @Override
    public Juego buscarEstadoJuego(String estado) {
        return balotaRepository.findIdByEstadoJuego(estado); // Buscar juego en la base de datos según su estado
    }


    // Método para buscar un jugador según su ID
    @Override
    public Jugador searchJugadorId(Integer idJugador) {
        return cartonRepository.findByUsuarioId(idJugador); // Buscar jugador en la base de datos según su ID
    }

    // Método para actualizar el cartón de un jugador
    @Override
    public Carton updateCarton(Jugador jugador) {
        Integer idcartonAntiguo = cartonRepository.findByIdJugador(jugador, "en uso"); // Buscar ID del cartón antiguo del jugador
        if (idcartonAntiguo == null) {
            return createNumber(jugador); // Si el jugador no tiene cartón antiguo, crear uno nuevo
        }
        Carton cartonAntiguo = cartonRepository.findByIdCarton(idcartonAntiguo); // Obtener cartón antiguo del jugador según su ID
        cartonAntiguo.setEstado("vencido"); // Actualizar estado del cartón antiguo a "vencido"
        return createNumber(jugador); // Crear un nuevo cartón para el jugador
    }


    @Override
    public Carton createNumber(Jugador jugador) {
        // Se crea un objeto Carton y se le asigna el jugador pasado como parámetro
        Carton carton = new Carton();
        carton.setIdJugador(jugador);

        // Se crea un objeto Random para generar números aleatorios
        Random random = new Random();

        // Se crean listas para almacenar los números de cada columna del cartón
        List<Integer> numerosColumnaB = new ArrayList<>();
        List<Integer> numerosColumnaI = new ArrayList<>();
        List<Integer> numerosColumnaN = new ArrayList<>();
        List<Integer> numerosColumnaG = new ArrayList<>();
        List<Integer> numerosColumnaO = new ArrayList<>();

        // Generar números aleatorios para la columna B
        while (numerosColumnaB.size() < 5) {
            int numeroAleatorio = random.nextInt(15)+1 ;
            if (!numerosColumnaB.contains(numeroAleatorio)) {
                numerosColumnaB.add(numeroAleatorio);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String cartonJSONB;
        try {
            cartonJSONB = mapper.writeValueAsString(numerosColumnaB).replaceAll("\\[|\\]|\"", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            cartonJSONB = "[]"; // En caso de error, devolver un cartón vacío
        }

        // Generar números aleatorios para la columna I
        while (numerosColumnaI.size() < 5) {
            int numeroAleatorio = random.nextInt(15) + 16;
            if (!numerosColumnaI.contains(numeroAleatorio)) {
                numerosColumnaI.add(numeroAleatorio);
            }
        }

        String cartonJSONI;
        try {
            cartonJSONI = mapper.writeValueAsString(numerosColumnaI).replaceAll("\\[|\\]|\"", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            cartonJSONI = "[]"; // En caso de error, devolver un cartón vacío
        }

        // Generar números aleatorios para la columna N
        while (numerosColumnaN.size() < 5) {
            int numeroAleatorio = random.nextInt(15) + 31;
            if (!numerosColumnaN.contains(numeroAleatorio)) {
                numerosColumnaN.add(numeroAleatorio);
            }
        }
        String cartonJSONN;
        try {
            cartonJSONN = mapper.writeValueAsString(numerosColumnaN).replaceAll("\\[|\\]|\"", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            cartonJSONN = "[]"; // En caso de error, devolver un cartón vacío
        }

        // Generar números aleatorios para la columna G
        while (numerosColumnaG.size() < 5) {
            int numeroAleatorio = random.nextInt(15) + 46;
            if (!numerosColumnaG.contains(numeroAleatorio)) {
                numerosColumnaG.add(numeroAleatorio);
            }
        }
        String cartonJSONG;
        try {
            cartonJSONG = mapper.writeValueAsString(numerosColumnaG).replaceAll("\\[|\\]|\"", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            cartonJSONG = "[]"; // En caso de error, devolver un cartón vacío
        }

        // Generar números aleatorios para la columna O
        while (numerosColumnaO.size() < 5) {
            int numeroAleatorio = random.nextInt(15) + 61;
            if (!numerosColumnaO.contains(numeroAleatorio)) {
                numerosColumnaO.add(numeroAleatorio);
            }
        }
        String cartonJSONO;
        try {
            cartonJSONO = mapper.writeValueAsString(numerosColumnaO).replaceAll("\\[|\\]|\"", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            cartonJSONO = "[]";} // En caso de error, devolver un cartón vacío

        // Asignar los valores de los números generados aleatoriamente a cada columna del cartón
        carton.setB(cartonJSONB);
        carton.setI(cartonJSONI);
        carton.setN(cartonJSONN);
        carton.setG(cartonJSONG);
        carton.setO(cartonJSONO);

        // Establecer el estado del cartón como "en uso"
        carton.setEstado("en uso");

        // Guardar el cartón generado en la base de datos y devolverlo
        return cartonRepository.save(carton);

    }

    /**
     * Retorna una lista de los números del cartón actual de un jugador específico.
     *
     * @param jugador el jugador para el cual se desea obtener los números de su cartón
     * @return una lista de los números del cartón actual del jugador especificado
     */
    @Override
    public List<String> getCartonesByJugador(Jugador jugador) {
        // Obtiene el número de cartón actual del jugador
        Integer cartones = cartonRepository.findByIdJugador(jugador,"en uso");
        // Obtiene la lista de números correspondientes a la columna B del cartón
        List<String> bingo=  cartonRepository.findByIdB(cartones);
        // Retorna la lista de números de la columna bingo
        return bingo;
    }


    //Balotas
    /**

     Crea una nueva balota para el juego indicado cada 5 segundos, hasta que se completen
     75 balotas o se cumplan 5 minutos.

     @param id el identificador del juego.

     @return el juego actualizado.
     */
    @Override
    public Juego createBalota(Integer id) {
        // Obtener el juego correspondiente al id
        Juego juego = getJuegoById(id);

        // Crear una lista para almacenar los números generados hasta el momento
        List<Integer> numerosGenerados = new ArrayList<>();

        // Programar la creación de una balota cada 5 segundos
        ScheduledFuture<?> balotaTask = scheduler.scheduleAtFixedRate(() -> {
        // Crear una nueva balota y asignarle el juego correspondiente
            Balotas balota = new Balotas();
            balota.setJuego(juego);
            // Generar un número aleatorio entre 1 y 75, evitando que se repitan los números ya generados
            Random random = new Random();
            int numeroAleatorio;
            do {
                numeroAleatorio = random.nextInt(75) + 1;
            } while (numerosGenerados.contains(numeroAleatorio));

            // Agregar el número generado a la lista de números generados
            numerosGenerados.add(numeroAleatorio);

            // Asignar el número generado a la balota y guardarla en la base de datos
            balota.setBalota(numeroAleatorio);
            balotaRepository.save(balota);
        }, 0, 5, TimeUnit.SECONDS);

        // Programar la cancelación de la tarea después de 5 minutos
        scheduler.schedule(() -> {
            balotaTask.cancel(true);
        }, 5, TimeUnit.MINUTES);

        // Devolver el juego actualizado
        return juego;
    }

    @Override
    public boolean verificarGanador(Jugador idjugador){
        Juego juego = buscarEstadoJuego("en juego");
        List<Balotas> balotas = buscarBalotasPorIdJuego(juego);

        List<List<Balotas>> balotasSeparadas = new ArrayList<>();

        // Creamos las listas vacías para cada rango de números
        for (int i = 0; i < 5; i++) {
            balotasSeparadas.add(new ArrayList<Balotas>());
        }

        // Separamos las balotas según su número
        for (Balotas balota : balotas) {
            int numeroBalota = balota.getBalota();
            int indiceLista = (numeroBalota - 1) / 15; // Calculamos el índice de la lista correspondiente
            balotasSeparadas.get(indiceLista).add(balota); // Agregamos la balota a la lista correspondiente
        }

        List<List<Integer>> numeros = new ArrayList<>();
        List<String> carton = getCartonesByJugador(idjugador);

        // Recorremos cada string en la lista de carton
        for (String numerosCarton : carton) {
            String[] numerosArray = numerosCarton.split(",");
            List<Integer> numerosList = new ArrayList<>();

            // Convertimos cada string en la lista de números a Integer y los agregamos a una lista de números
            for (String numero : numerosArray) {
                numerosList.add(Integer.parseInt(numero));
            }

            // Separamos los números en cinco listas según su rango
            List<Integer> numerosB = new ArrayList<>();
            List<Integer> numerosI = new ArrayList<>();
            List<Integer> numerosN = new ArrayList<>();
            List<Integer> numerosG = new ArrayList<>();
            List<Integer> numerosO = new ArrayList<>();

            for (Integer numero : numerosList) {
                if (numero >= 1 && numero <= 15) {
                    numerosB.add(numero);
                } else if (numero >= 16 && numero <= 30) {
                    numerosI.add(numero);
                } else if (numero >= 31 && numero <= 45) {
                    numerosN.add(numero);
                } else if (numero >= 46 && numero <= 60) {
                    numerosG.add(numero);
                } else if (numero >= 61 && numero <= 75) {
                    numerosO.add(numero);
                }
            }

            // Agregamos las listas de números separadas por rango a la lista de números
            numeros.add(numerosB);
            numeros.add(numerosI);
            numeros.add(numerosN);
            numeros.add(numerosG);
            numeros.add(numerosO);
        }

        boolean ganador = false;
        for (int i = 0; i < 5; i++) {
            List<Balotas> lista = balotasSeparadas.get(i);
            List<Integer> columna = numeros.get(i);

            // Verificamos si todos los números de la columna están presentes en la lista de balotas correspondiente
            boolean ganadorColumna = lista.stream().map(Balotas::getBalota).collect(Collectors.toList()).containsAll(columna);

            if (ganadorColumna) {
                ganador = true;
                System.out.println("El jugador " + idjugador.getUsuario() + " ha ganado la columna " + (i+1)+juego.getGanador());
                juego.setGanador(idjugador.getUsuario());
                juego.setEstado("terminado");
                juegoRepository.save(juego);
            }
        }

        return ganador;
    }


    @Override
    public List<Balotas> buscarBalotasPorIdJuego(Juego idJuego) {
        return balotaRepository.findByJuegoId(idJuego);
    }

    //INFOJUEGO
    @Override
    public InfoJuego createInfo(Juego juegoEnEspera, Carton cartonById) {
        InfoJuego infoJuego = new InfoJuego();
        infoJuego.setJuego(juegoEnEspera);
        infoJuego.setCarton(cartonById);
        return infoJuegoRepository.save(infoJuego);
    }


}
