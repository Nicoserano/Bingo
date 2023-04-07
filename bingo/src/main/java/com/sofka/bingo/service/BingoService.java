package com.sofka.bingo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.bingo.domain.*;
import com.sofka.bingo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class BingoService implements Ibingo{
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

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> juegoTask;

    //jugador
    @Override
    public Jugador createJugador(Jugador jugador) {
        return jugadorRepository.save(jugador);
    }

    //juego
    @Override
    public Juego getJuegoById(Integer id) {
        Optional<Juego> juego = juegoRepository.findById(id);
        if (juego.isPresent()) {
            return juego.get();
        } else {
            throw new NoSuchElementException("Juego no encontrado");
        }
    }

    @Override
    public Juego startJuego(Integer id) {
        Juego juego = getJuegoById(id);
        juego.setEstado("en espera");
        juegoRepository.save(juego);

        // Iniciar juego después de 5 minutos
        juegoTask = scheduler.schedule(() -> {
            juego.setEstado("en juego");
            juegoRepository.save(juego);
            if (juego.getGanador()==null){
            createBalota(id);
            }

            // Finalizar juego después de 5 minutos
            scheduler.schedule(() -> {
                juego.setEstado("terminado");
                juegoRepository.save(juego);
            }, 5, TimeUnit.MINUTES);
        }, 5, TimeUnit.MINUTES);

        return juego;
    }

    @Override
    public Integer buscarEstado(String estado) {
        return juegoRepository.findIdByEstado(estado);
    }
    @Override
    public Juego buscarEstadoJuego(String estado) {
        return balotaRepository.findIdByEstadoJuego(estado);
    }

    @Override
    public Jugador searchJugadorId(Integer idJugador) {
        return cartonRepository.findByUsuarioId(idJugador);
    }


    //cartones
    @Override
    public Carton updateCarton(Jugador jugador) {
        Integer idcartonAntiguo = cartonRepository.findByIdJugador(jugador,"en uso");
        if (idcartonAntiguo == null) {
            return createNumber(jugador);
        }
        Carton  cartonAntiguo =cartonRepository.findByIdCarton(idcartonAntiguo);
        cartonAntiguo.setEstado("vencido");
        return createNumber(jugador);
    }

    @Override
    public Carton createNumber(Jugador jugador) {
        Carton carton=new Carton();
        carton.setIdJugador(jugador);
        Random random = new Random();
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


        carton.setB(cartonJSONB);
        carton.setI(cartonJSONI);
        carton.setN(cartonJSONN);
        carton.setG(cartonJSONG);
        carton.setO(cartonJSONO);
        carton.setEstado("en uso");

        return cartonRepository.save(carton);
    }

    @Override
    public List<String> getCartonesByJugador(Jugador jugador) {
        Integer cartones = cartonRepository.findByIdJugador(jugador,"en uso");
        List<String> B=  cartonRepository.findByIdB(cartones);
        return B;
    }

    //Balotas
    @Override
    public Juego createBalota(Integer id) {
        Juego juego = getJuegoById(id);
        List<Integer> numerosGenerados = new ArrayList<>();
        // Programar la creación de una balota cada 5 segundos
        ScheduledFuture<?> balotaTask = scheduler.scheduleAtFixedRate(() -> {
            Balotas balota = new Balotas();
            balota.setJuego(juego);
            Random random = new Random();

            // Verificar si ya se generó el número
            int numeroAleatorio;
            do {
                numeroAleatorio = random.nextInt(75) + 1;
            } while (numerosGenerados.contains(numeroAleatorio));
            numerosGenerados.add(numeroAleatorio);
            balota.setBalota(numeroAleatorio);
            balotaRepository.save(balota);
        }, 0, 5, TimeUnit.SECONDS);

        // Programar la cancelación de la tarea después de 5 minutos
        scheduler.schedule(() -> {
            balotaTask.cancel(true);
        }, 5, TimeUnit.MINUTES);

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
