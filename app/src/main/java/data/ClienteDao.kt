package com.gutierrez.modaapp.data

import android.content.ContentValues
import android.content.Context
import com.gutierrez.modaapp.model.Cliente
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClienteDao(context: Context) {

    private val db = DBHelper(context).writableDatabase

    fun buscarPorTelefono(telefono: String): Cliente? {
        val cursor = db.rawQuery(
            "SELECT id, telefono, nombres, apellidos, fecha_registro FROM ${DBHelper.T_CLIENTE} WHERE telefono = ?",
            arrayOf(telefono)
        )
        return if (cursor.moveToFirst()) {
            val c = Cliente(
                id = cursor.getInt(0),
                telefono = cursor.getString(1),
                nombres = cursor.getString(2),
                apellidos = cursor.getString(3),
                fechaRegistro = cursor.getString(4)
            )
            cursor.close()
            c
        } else {
            cursor.close()
            null
        }
    }

    fun insertar(telefono: String, nombres: String, apellidos: String): Long {
        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val cv = ContentValues().apply {
            put("telefono", telefono)
            put("nombres", nombres)
            put("apellidos", apellidos)
            put("fecha_registro", fecha)
        }
        return db.insert(DBHelper.T_CLIENTE, null, cv)
    }
}