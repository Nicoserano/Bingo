import React from 'react'
import ReactDOM from 'react-dom/client'
import Lobby from './Lobby/Lobby'
import Login from './Login/login'
import Registro from './Registro/registro'
import { BrowserRouter as Switch, Routes, Route } from 'react-router-dom'



ReactDOM.createRoot(document.getElementById('root')).render(
  <Switch>
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="registro" element={<Registro />} />
      <Route path={`lobby/:id_jugador/:usuario/carton`} element={<Lobby />} />
    </Routes>
  </Switch>
)
