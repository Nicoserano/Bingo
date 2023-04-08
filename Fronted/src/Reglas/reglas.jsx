import React from "react";
import './reglas.css'
const Reglas=()=>{
        return(
            <div>
                <h1>Reglas del Juego</h1>
                <br></br>
                <ol className="lista">
                    <li>Para jugar el bingo se debe tener una cuenta registrada y haber iniciado session previamente</li>
                    <li>Para ganar el bingo se debe tener todas las casillas de una fila seleccionada para poder hacer bingo!</li>
                    <li>El jugador tendra un carton por inicio de sesion, cada vez que inicie sesion el carton cambiara</li>
                </ol>
            </div>
        )
}
export default Reglas;
