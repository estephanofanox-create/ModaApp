package com.gutierrez.modaapp.data

import android.content.ContentValues
import android.content.Context
import com.gutierrez.modaapp.ItemCarrito
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PedidoDao(context: Context) {

    private val db = DBHelper(context).writableDatabase

    fun registrar(idCliente: Int, items: List<ItemCarrito>): Long {
        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val total = items.sumOf { it.subtotal }

        db.beginTransaction()
        return try {
            val cvPedido = ContentValues().apply {
                put("id_cliente", idCliente)
                put("fecha", fecha)
                put("total", total)
                put("estado", "PENDIENTE")
                putNull("fecha_atencion")
            }
            val idPedido = db.insert(DBHelper.T_PEDIDO, null, cvPedido)
            if (idPedido <= 0) throw Exception("No se pudo crear el pedido")

            for (item in items) {
                val cvDet = ContentValues().apply {
                    put("id_pedido", idPedido)
                    put("id_ropa", item.ropa.id)
                    put("cantidad", item.cantidad)
                    put("precio_unit", item.ropa.precio)
                    put("subtotal", item.subtotal)
                }
                db.insert(DBHelper.T_DETALLE, null, cvDet)
            }

            db.setTransactionSuccessful()
            idPedido
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        } finally {
            db.endTransaction()
        }
    }
}