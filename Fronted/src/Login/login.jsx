import React, {useState} from "react";
import "./login.css"
import { useNavigate } from "react-router";

const Login = () => {
    const [logueado, setLogueado] = useState(false)
    const Navigate = useNavigate()

    const enviarDatos = async (e) => {
      e.preventDefault()

      const {
        target: { usuario, contraseña },
      } = e;

      const response = await fetch("http://localhost:9090/inicio", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          usuario: usuario.value,
          contraseña: contraseña.value,
        }),
      })

      if (!response.ok) {
        return alert("Error al iniciar sesion")
      }
      const data = await response.json();
      const id_jugador = data.data.id_jugador;
      setLogueado(true)
      Navigate(`lobby/${id_jugador}/${usuario.value}/carton`)   
    }

    return (
          <div >
            <h1>Iniciar sesión</h1>
            <form onSubmit={enviarDatos} className="form-login" >
              <div className="container">
                <label htmlFor="username">Nombre de usuario</label>
                <input type="text" placeholder="Ingrese su nombre de usuario" name="usuario" required />
      
                <label htmlFor="password">Contraseña</label>
                <input type="password" placeholder="Ingrese su contraseña" name="contraseña" required />
      
                <button type="submit" className="ingresar">Ingresar</button>
              </div>
            </form>
            <button className="cancelbtn" type="button" onClick={() => Navigate("registro")}>Registrar</button>
          </div>

      );
}

export default Login;