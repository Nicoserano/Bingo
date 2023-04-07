// Importamos la librería 'express'
const express = require('express');

// Creamos un objeto 'router' a partir de la función Router de 'express'
const router = express.Router();
const axios = require('axios');
const{crearUsuario,IniciarSesion, Loby, Info, EliminarInfo, Ganador}=require('../controllers/usuarioController')



router.post('/guardado',crearUsuario);
router.post('/inicio',IniciarSesion);
router.get(`/inicio/:id_jugador/:usuario/carton`,Loby);
router.post(`/inicio/:id_jugador/jugar`,Info);
router.delete(`/inicio/:id_jugador/info/eliminar`,EliminarInfo);
router.post(`/inicio/:id_jugador/info/eliminar`,Ganador)



module.exports = router;