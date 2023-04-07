import React from "react";
import "./registro.css";
import { useNavigate } from "react-router-dom";

const Registro = () => {
  const [nombre, setNombre] = React.useState("");
  const [usuario, setUsuario] = React.useState("");
  const [email, setEmail] = React.useState("");
  const [contraseña, setContraseña] = React.useState("");
  const Navigate = useNavigate();

  const enviarDatos = async (e) => {
    e.preventDefault();

    const {
      target: { nombre, usuario, email, contraseña },
    } = e;

    const values = {
      nombre: nombre.value,
      usuario: usuario.value,
      email: email.value,
      contraseña: contraseña.value,
    }

    // console.log(values)
    
    const data = await fetch("http://localhost:9090/guardado", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    })

    if(!data.ok) {
      alert("Error al guardar los datos")
    }
    
    Navigate("/")
  }

    return (
        <div>
          <head>
            <title>Registro de usuario</title>
          </head>
          <body>
            <form onSubmit={enviarDatos} className="form-registro">
              <h2 className="h2-form">Registro de usuario</h2>
              <div className='container-registro'>
                <label htmlFor='nombre'>Nombre:</label>
                <input type='text' placeholder='Ingrese su nombre' name='nombre' required />
                <label htmlFor='usuario'>Usuario:</label>
                <input type='text' placeholder='Ingrese su usuario' name='usuario' required />
                <label htmlFor='email'>Email:</label>
                <input type='email' placeholder='Ingrese su email' name='email' required />
                <label htmlFor='contraseña'>Contraseña:</label>
                <input type='password' placeholder='Ingrese su contraseña' name='contraseña' required />
                <button type='submit' className="buton-registro">Registrarse</button>
              </div>
            </form>
          </body>
        </div>
      );
}

export default Registro;