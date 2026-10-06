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

    fun actualizar(r: Ropa): Int {
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
        return db.update(DBHelper.T_ROPA, cv, "id = ?", arrayOf(r.id.toString()))
    }

    fun eliminar(id: Int): Int {
        return db.delete(DBHelper.T_ROPA, "id = ?", arrayOf(id.toString()))
    }

    fun obtener(id: Int): Ropa? {
        val cursor = db.rawQuery(
            "SELECT id, modelo, id_categoria, talla, marca, color, precio, cantidad, foto " +
                    "FROM ${DBHelper.T_ROPA} WHERE id = ?", arrayOf(id.toString())
        )
        return if (cursor.moveToFirst()) {
            val r = Ropa(
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
            cursor.close()
            r
        } else {
            cursor.close()
            null
        }
    }

    fun listar(): List<Ropa> {
        return consultar(null, null)
    }

    fun listarDisponibles(idCategoria: Int?): List<Ropa> {
        return if (idCategoria == null) {
            consultar("cantidad > 0", null)
        } else {
            consultar("cantidad > 0 AND id_categoria = ?", arrayOf(idCategoria.toString()))
        }
    }

    fun listarConFiltro(filtro: String): List<Ropa> {
        val like = "%$filtro%"
        return consultar(
            "modelo LIKE ? OR marca LIKE ? OR color LIKE ?",
            arrayOf(like, like, like)
        )
    }

    private fun consultar(where: String?, args: Array<String>?): List<Ropa> {
        val lista = mutableListOf<Ropa>()
        val sql = StringBuilder(
            "SELECT id, modelo, id_categoria, talla, marca, color, precio, cantidad, foto FROM ${DBHelper.T_ROPA}"
        )
        if (!where.isNullOrEmpty()) sql.append(" WHERE $where")
        sql.append(" ORDER BY id DESC")

        val cursor = db.rawQuery(sql.toString(), args)
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