import React, { useState} from "react";
import { useParams ,useNavigate} from 'react-router-dom';
import { useInterval } from 'react-use';

import "./lobby.css";

const Lobby = () => {

// Obtenemos los parámetros de la URL
const { id_jugador, usuario } = useParams();

// Estado para almacenar las balotas del juego
const [balotas, setBalotas] = useState([]);

// Estado para almacenar las columnas del cartón
const [columnas, setColumnas] = useState([]);

// Estado para almacenar el estado actual del juego (en espera, jugando, etc.)
const [estado,setEstado]=useState("");

// Estado para almacenar el id del juego
const [id_juego,setidJuego]=useState("");

// Estado para habilitar o deshabilitar el botón "Cancelar"
const [cancelarHabilitado, setCancelarHabilitado] = useState(false);

// Estado para habilitar o deshabilitar el botón "Bingo"
const [bingoHabilitado, setBingoHabilitado] = useState(true);

// Estado para almacenar el nombre del ganador del juego
const [ganador,setganador]=useState("");

// Estado para contar el número de casillas marcadas en cada columna del cartón
const [contadorColumnas, setContadorColumnas] = useState([0, 0, 0, 0, 0]);

// Obtenemos la última balota del juego
const ultimaBalota = balotas[balotas.length - 1];

// Estado para almacenar las casillas seleccionadas del cartón
const [casillaSeleccionada,setCasillaSeleccionada]=useState([])

// Hook de navegación de React Router
const Navigate = useNavigate();

// Hook de intervalo de React para actualizar los datos del juego cada cierto tiempo
useInterval(() => {
const obtenerDatos = async () => {
const response = await fetch(`http://localhost:9090/inicio/${id_jugador}/${usuario}/carton`);
const data = await response.json();
setBalotas(data.data.balotas);
setColumnas(data.data.columnas);
setEstado(data.data.estado);
setidJuego(data.data.id_juego);
setganador(data.data.ganador);
if (ganador){
Navigate( `/ganador/${ganador}`)
}
};
obtenerDatos();
}, 5000);

// Función para manejar el evento "Jugar" del botón correspondiente
const manejarJugar = () => {
setCancelarHabilitado(true);
};

// Función que maneja el evento de hacer clic en una casilla del tablero
const handleCasillaClick = (event, numero) => {
  event.stopPropagation();
  let className ;
  let ver =Number(numero)  
  const casilla = event.target;  
  const columna = casilla.cellIndex;

  // Si el estado del juego es "en juego" y el botón de cancelar está habilitado
  if(estado=="en juego" && cancelarHabilitado==true ){
    // Si el número de la casilla se encuentra en la lista de balotas
    if (balotas.includes(ver)) {
      className = "selected";
      // Crear una copia del arreglo de contadores de columna
      const nuevosContadores = [...contadorColumnas];
      // Incrementar el contador de la columna correspondiente
      nuevosContadores[columna]++;
      // Actualizar el estado con los nuevos contadores
      setContadorColumnas(nuevosContadores);
      // Establecer la casilla seleccionada en el estado
      setCasillaSeleccionada([ columna]);
    } else {
      className ="noselected";
    }
    // Establecer la clase de la casilla seleccionada
    event.target.className = className; 
  }
  // Comprobar si alguna columna está llena
  const columnaLlena = contadorColumnas.some(contador => contador === 5);
  // Si alguna columna está llena, deshabilitar el botón de bingo
  if (columnaLlena) {
    setBingoHabilitado(false);
  } else {
    setBingoHabilitado(true);
  }
}

// Función que envía una solicitud para unirse a la partida
const guardarInfo = async () => {
  try {
    const response = await fetch(`http://localhost:9090/inicio/${id_jugador}/jugar`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        id_jugador
      }),
    })
    if (!response.ok) {
      throw new Error("No se ha podido unir a la partida")
    }
    alert("Se ha unido a la partida")
  } catch (error) {
    alert(error.message)
  }
}

// Función que envía una solicitud para informar al servidor que un jugador ha ganado
const guardarGanador = async () =>  { 
  try {
    const response = await fetch(`http://localhost:9090/ganador/${id_jugador}/${usuario}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        id_jugador
      }),
    })
    if (!response.ok) {
      throw new Error("No se ha podido informar al servidor que ha ganado")
    }
    alert("Se ha informado al servidor que ha ganado")
  } catch (error) {
    alert(error.message)
  }
}

// Función asincrónica para eliminar información del jugador en la partida
const eliminarInfo = async () => {
  // Realiza una petición DELETE a la URL correspondiente
  const response = await fetch(`http://localhost:9090/inicio/${id_jugador}/info/eliminar`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      id_jugador
    }),
  })
  // Imprime la respuesta en la consola para debugging
  console.log(response);
  // Si la respuesta no es ok, muestra un mensaje de error y retorna
  if (!response.ok) {
    return alert("No se ha podido eliminar la partida");
  }
  // Si la respuesta es ok, muestra un mensaje de éxito y retorna
  return alert("Ha salido de la partida")
}

return (
      <div>
          <h2 className="loby">Usuario: 
            <span className="usuario">{usuario}</span>
            <span >Estado:</span>
            <span className="usuario">{estado}</span>
            <span >Id Juego:</span>
            <span className="usuario">{id_juego}</span>
            <span >Ganador:</span>
            <span className="usuario">{ganador}</span>
          </h2>
          <table className="balotas">
            <thead className="balota-t">
              <tr> 
                <th>Última balota:</th>
                <th className="ultima-balota"> {ultimaBalota}</th>
              </tr>
             
            </thead>
            
            <tbody>
              <tr>
              {balotas.map((balota, bindex) => {
                if (bindex === balotas.length - 1) {
                  return null; 
                } else if (bindex === balotas.length - 2) {
                  return <td key={bindex} className="nueva-balota">{balota}</td>;
                } else {
                  return <td key={bindex}>{balota}</td>;
                }
})}
              </tr>
            </tbody>
          </table>
          <table className="carton">
            <thead>
              <tr>
                <th>B</th>
                <th>I</th>
                <th>N</th>
                <th>G</th>
                <th>O</th>
              </tr>
            </thead>
            <tbody>
              {columnas.map((columna, cindex) => {
                return (
                  <tr key={cindex}>
                    {columna.map((numero, cindex) => {
                      return <td key={cindex} className=""
                      onClick={(event) => handleCasillaClick(event, numero) }>{numero}</td>;
                    })}
                  </tr>
                );
              })}
            </tbody>
          </table>

          <form className="hidden">
            <input type="hidden" name="id_usuario" value={id_jugador} />
            <input type="hidden" name="usuario" value={usuario} />
            <button className="jugar" type="submit"  disabled={estado !== 'en espera' || cancelarHabilitado} 
            onClick={(event)=>{event.preventDefault();guardarInfo();manejarJugar()}}  >
              Jugar!
            </button>
          </form>
          <form className="hidden">
            <input type="hidden" name="id_usuario" value={id_jugador} />
            <button className="bingo" type="submit" disabled={bingoHabilitado} onClick={(event)=>{event.preventDefault();guardarGanador()}}>
              Bingo!
            </button>
          </form>
          <form className="hidden">
            <input type="hidden" name="id_usuario" value={id_jugador} />
            <button className="cancelar" type="submit" disabled={!cancelarHabilitado}
             onClick={(event) =>{event.preventDefault();eliminarInfo();setCancelarHabilitado(false)}}>
              Cancelar!
            </button>
          </form>
            <button className="cerrar" type="submit" onClick={() => Navigate("/")}  >
              Cerrar Sesion
            </button>
            <button className="reglas" type="submit" onClick={() => Navigate("/reglas")}  >
              Reglas
            </button>
      </div>
);
};

export default Lobby;