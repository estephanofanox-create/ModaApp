package com.gutierrez.modaapp.data

import android.content.Context

class ReporteDao(context: Context) {

    private val db = DBHelper(context).readableDatabase

    fun atendidosDelMes(): Pair<Int, Double> {
        val mes = "%" + java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault())
            .format(java.util.Date()) + "%"
        val cursor = db.rawQuery(
            "SELECT COUNT(*), IFNULL(SUM(total), 0) FROM ${DBHelper.T_PEDIDO} " +
                    "WHERE estado = 'ATENDIDO' AND fecha_atencion LIKE ?",
            arrayOf(mes)
        )
        val r = if (cursor.moveToFirst()) Pair(cursor.getInt(0), cursor.getDouble(1)) else Pair(0, 0.0)
        cursor.close()
        return r
    }

    data class StockItem(val modelo: String, val cantidad: Int)

    fun stockPorPrenda(): List<StockItem> {
        val lista = mutableListOf<StockItem>()
        val cursor = db.rawQuery(
            "SELECT modelo, cantidad FROM ${DBHelper.T_ROPA} ORDER BY cantidad ASC", null
        )
        while (cursor.moveToNext()) {
            lista.add(StockItem(cursor.getString(0), cursor.getInt(1)))
        }
        cursor.close()
        return lista
    }

    data class ClienteItem(
        val nombres: String,
        val apellidos: String,
        val telefono: String,
        val pedidos: Int
    )

    fun clientesConPedidos(): List<ClienteItem> {
        val lista = mutableListOf<ClienteItem>()
        val cursor = db.rawQuery(
            "SELECT c.nombres, c.apellidos, c.telefono, COUNT(p.id) " +
                    "FROM ${DBHelper.T_CLIENTE} c " +
                    "LEFT JOIN ${DBHelper.T_PEDIDO} p ON p.id_cliente = c.id " +
                    "GROUP BY c.id ORDER BY c.nombres",
            null
        )
        while (cursor.moveToNext()) {
            lista.add(
                ClienteItem(
                    nombres = cursor.getString(0),
                    apellidos = cursor.getString(1),
                    telefono = cursor.getString(2),
                    pedidos = cursor.getInt(3)
                )
            )
        }
        cursor.close()
        return lista
    }
}