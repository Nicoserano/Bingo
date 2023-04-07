import React, { useState, useEffect} from "react";
import { useParams } from 'react-router-dom';
import { useInterval } from 'react-use';
import { useNavigate } from "react-router";

import "./lobby.css";

const Lobby = () => {
const { id_jugador, usuario } = useParams();
const [balotas, setBalotas] = useState([]);
const [columnas, setColumnas] = useState([]);
const [estado,setEstado]=useState("");
const [id_juego,setidJuego]=useState("");
const [cancelarHabilitado, setCancelarHabilitado] = useState(false);
const Navigate = useNavigate();


useInterval(() => {
  const obtenerDatos = async () => {
    const response = await fetch(`http://localhost:9090/inicio/${id_jugador}/${usuario}/carton`);
    const data = await response.json();
    setBalotas(data.data.balotas);
    setColumnas(data.data.columnas);
    setEstado(data.data.estado);
    setidJuego(data.data.id_juego);
  };
  obtenerDatos();
}, 5000);
const manejarJugar = () => {
  setCancelarHabilitado(true);
};

return (
      <div>
          <h2 className="loby">Usuario: 
            <span className="usuario">{usuario}</span>
            <span >Estado:</span>
            <span className="usuario">{estado}</span>
            <span >Id Juego:</span>
            <span className="usuario">{id_juego}</span>
          </h2>
          
          <table className="balotas">
            <thead className="balota-t"><h3>Última balota:</h3></thead>
            <tbody>
              <tr>
                {balotas.reverse().map((balota, index) => {
                  if (index === 0) {
                    return <td key={index} className="nueva-balota">{balota}</td>;
                  } else {
                    return <td key={index}>{balota}</td>;
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
              {columnas.map((columna, index) => {
                return (
                  <tr key={index}>
                    {columna.map((numero, index) => {
                      return <td key={index}>{numero}</td>;
                    })}
                  </tr>
                );
              })}
            </tbody>
          </table>

          <form className="hidden">
            <input type="hidden" name="id_usuario" value={id_jugador} />
            <input type="hidden" name="usuario" value={usuario} />
            <button className="jugar" type="submit"  disabled={estado !== 'en espera' || cancelarHabilitado} onClick={manejarJugar} >
              Jugar!
            </button>
          </form>
          <form className="hidden">
            <input type="hidden" name="id_usuario" value={id_jugador} />
            <button className="bingo" type="submit">
              Bingo!
            </button>
          </form>
          <form className="hidden">
            <input type="hidden" name="id_usuario" value={id_jugador} />
            <button className="cancelar" type="submit" disabled={!cancelarHabilitado} >
              Cancelar!
            </button>
          </form>
            <button className="cerrar" type="submit" onClick={() => Navigate("/")}  >
              Cerrar Sesion
            </button>
         
      </div>
);
};

export default Lobby;