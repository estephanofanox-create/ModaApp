package com.gutierrez.modaapp.data

import android.content.Context
import com.gutierrez.modaapp.model.Usuario

class UsuarioDao(context: Context) {

    private val db = DBHelper(context).readableDatabase

    fun validarUsuario(usuario: String, clave: String): Usuario? {
        val cursor = db.rawQuery(
            "SELECT id, usuario, clave, rol, telefono FROM ${DBHelper.T_USUARIO} WHERE usuario = ? AND clave = ?",
            arrayOf(usuario, clave)
        )
        return if (cursor.moveToFirst()) {
            val u = Usuario(
                id = cursor.getInt(0),
                usuario = cursor.getString(1),
                clave = cursor.getString(2),
                rol = cursor.getString(3),
                telefono = cursor.getString(4)
            )
            cursor.close()
            u
        } else {
            cursor.close()
            null
        }
    }
}