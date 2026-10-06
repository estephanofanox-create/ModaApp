package com.gutierrez.modaapp.data

import android.content.Context
import com.gutierrez.modaapp.model.Categoria

class CategoriaDao(context: Context) {

    private val db = DBHelper(context).readableDatabase

    fun listar(): List<Categoria> {
        val lista = mutableListOf<Categoria>()
        val cursor = db.rawQuery("SELECT id, nombre FROM ${DBHelper.T_CATEGORIA} ORDER BY id", null)
        while (cursor.moveToNext()) {
            lista.add(Categoria(cursor.getInt(0), cursor.getString(1)))
        }
        cursor.close()
        return lista
    }
}