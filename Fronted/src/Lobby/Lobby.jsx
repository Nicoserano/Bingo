import React, { useState, useEffect} from "react";
import { useParams ,useNavigate} from 'react-router-dom';
import { useInterval } from 'react-use';


import "./lobby.css";

const Lobby = () => {

const { id_jugador, usuario } = useParams();
const [balotas, setBalotas] = useState([]);
const [columnas, setColumnas] = useState([]);
const [estado,setEstado]=useState("");
const [id_juego,setidJuego]=useState("");
const [cancelarHabilitado, setCancelarHabilitado] = useState(false);
const [bingoHabilitado, setBingoHabilitado] = useState(true);
const [ganador,setganador]=useState("");
const [contadorColumnas, setContadorColumnas] = useState([0, 0, 0, 0, 0]);
const ultimaBalota = balotas[balotas.length - 1];
const [casillaSeleccionada,setCasillaSeleccionada]=useState([])





const Navigate = useNavigate();



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


const manejarJugar = () => {
  setCancelarHabilitado(true);
};

const handleCasillaClick = (event, numero) => {
  event.stopPropagation();
  let className ;
  let ver =Number(numero)  
  const casilla = event.target;  
  const columna = casilla.cellIndex;

  if(estado=="en juego" && cancelarHabilitado==true ){
    if (balotas.includes(ver)) {
      className = "selected";
      const nuevosContadores = [...contadorColumnas];
      nuevosContadores[columna]++; // incrementar el contador de la columna correspondiente
      setContadorColumnas(nuevosContadores); // actualizar los contadores en el estado
      setCasillaSeleccionada([ columna]);
    } else {
      className ="noselected";
    }
    event.target.className = className; 
  }
    const columnaLlena = contadorColumnas.some(contador => contador === 5);
    if (columnaLlena) {
      setBingoHabilitado(false);
    } else {
      setBingoHabilitado(true);
    }
  



}



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
    throw new Error("No se ha podido unir a la partida")
  }
  alert("Se ha unido a la partida")
} catch (error) {
  alert(error.message)
}
}



const eliminarInfo = async ()=>{
  const response = await fetch(`http://localhost:9090/inicio/${id_jugador}/info/eliminar`, {
  method: "DELETE",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    id_jugador
  }),
})
console.log(response);
if (!response.ok) {
  return alert("No se ha podido eliminar la partida");
}
return alert("Ha salido de  la partida")
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