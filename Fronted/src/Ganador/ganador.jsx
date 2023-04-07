import React from "react";

import './ganador.css';
import { useParams } from "react-router";

const Ganador =()=>{
const {usuario}=useParams();
    return(
        <div>
            <form className="ganador-form">
                <h2>El Ganador es <span className="ganador-usuario">{usuario}</span><span className="signos">!!!</span></h2>
                <br></br>
                <h4>{usuario}</h4>
                <br></br>
                <br></br>
                <br></br>
                <h2>Felicitaciones {usuario}, sigue jugando para ganar maravillosos premios</h2>
            </form>
        </div>
    );
};

export default Ganador;