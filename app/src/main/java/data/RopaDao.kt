package com.gutierrez.modaapp.data

import android.content.ContentValues
import android.content.Context
import com.gutierrez.modaapp.model.Ropa

class RopaDao(context: Context) {

    private val db = DBHelper(context).writableDatabase

    fun insertar(r: Ropa): Long {
        val cv = ContentValues().apply {
            put("modelo", r.modelo)
            put("id_categoria", r.idCategoria)
            put("talla", r.talla)
            put("marca", r.marca)
            put("color", r.color)
            put("precio", r.precio)
            put("cantidad", r.cantidad)
            put("foto", r.foto)
        }
        return db.insert(DBHelper.T_ROPA, null, cv)
    }

    fun listar(): List<Ropa> {
        val lista = mutableListOf<Ropa>()
        val cursor = db.rawQuery(
            "SELECT id, modelo, id_categoria, talla, marca, color, precio, cantidad, foto " +
                    "FROM ${DBHelper.T_ROPA} ORDER BY id DESC", null
        )
        while (cursor.moveToNext()) {
            lista.add(
                Ropa(
                    id = cursor.getInt(0),
                    modelo = cursor.getString(1),
                    idCategoria = cursor.getInt(2),
                    talla = cursor.getString(3),
                    marca = cursor.getString(4),
                    color = cursor.getString(5),
                    precio = cursor.getDouble(6),
                    cantidad = cursor.getInt(7),
                    foto = cursor.getString(8)
                )
            )
        }
        cursor.close()
        return lista
    }

    fun listarDisponibles(idCategoria: Int?): List<Ropa> {
        val lista = mutableListOf<Ropa>()
        val sql: String
        val args: Array<String>?

        if (idCategoria == null) {
            sql = "SELECT id, modelo, id_categoria, talla, marca, color, precio, cantidad, foto " +
                    "FROM ${DBHelper.T_ROPA} WHERE cantidad > 0 ORDER BY id DESC"
            args = null
        } else {
            sql = "SELECT id, modelo, id_categoria, talla, marca, color, precio, cantidad, foto " +
                    "FROM ${DBHelper.T_ROPA} WHERE cantidad > 0 AND id_categoria = ? ORDER BY id DESC"
            args = arrayOf(idCategoria.toString())
        }

        val cursor = db.rawQuery(sql, args)
        while (cursor.moveToNext()) {
            lista.add(
                Ropa(
                    id = cursor.getInt(0),
                    modelo = cursor.getString(1),
                    idCategoria = cursor.getInt(2),
                    talla = cursor.getString(3),
                    marca = cursor.getString(4),
                    color = cursor.getString(5),
                    precio = cursor.getDouble(6),
                    cantidad = cursor.getInt(7),
                    foto = cursor.getString(8)
                )
            )
        }
        cursor.close()
        return lista
    }
}