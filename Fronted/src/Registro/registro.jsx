import React from "react";
import "./registro.css";
import { useNavigate } from "react-router-dom";

const Registro = () => {
  const enviarDatos = async (e) => {
    e.preventDefault();
  
    const {
      target: { nombre, usuario, email, contraseña },
    } = e;
   // Se almacenan los valores de los campos del formulario en un objeto
    const values = {
      nombre: nombre.value,
      usuario: usuario.value,
      email: email.value,
      contraseña: contraseña.value,
    }
  
   
    // Se envía una petición POST al servidor con los valores del formulario
    const data = await fetch("http://localhost:9090/guardado", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    })
  
    
  
    if(!data.ok) {
      alert("Error al guardar los datos")// Si la respuesta del servidor es incorrecta se muestra una alerta
    }
  
    
    // Esta función se ejecuta al enviar el formulario y guarda los datos en el servidor
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