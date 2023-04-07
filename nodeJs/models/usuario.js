const mongoose = require('mongoose');
const bcrypt = require('bcrypt');

const usuarioSchema = new  mongoose.Schema({
    nombre: {
        type: String,
        required: true // el nombre del jugador es obligatorio
      },
    usuario:{
        type: String,
        required:true
    },
    email:{
      type:String,
      required:true
    },
    contraseña:{
        type:String,
        required:true
    }



});
usuarioSchema.pre('save', function(next) {
    const user = this;
    if (!user.isModified('contraseña')) {
      return next();
    }
    bcrypt.genSalt(10, function(err, salt) {
      if (err) {
        return next(err);
      }
      bcrypt.hash(user.contraseña, salt, function(err, hash) {
        if (err) {
          return next(err);
        }
        user.contraseña = hash;
        next();
      });
    });
  });

const Usuario = mongoose.model('Usuario', usuarioSchema);

module.exports = {Usuario};
  