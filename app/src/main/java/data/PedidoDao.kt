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

    fun obtenerMensajePedido(idPedido: Int, telefonoCliente: String): String {
        val sb = StringBuilder()
        sb.append("Pedido #$idPedido\n")

        val cursor = db.rawQuery(
            "SELECT r.modelo, r.talla, r.color, d.cantidad " +
                    "FROM ${DBHelper.T_DETALLE} d " +
                    "JOIN ${DBHelper.T_ROPA} r ON r.id = d.id_ropa " +
                    "WHERE d.id_pedido = ?", arrayOf(idPedido.toString())
        )
        while (cursor.moveToNext()) {
            sb.append("• ${cursor.getString(0)} (${cursor.getString(1)}) ${cursor.getString(2)} x${cursor.getInt(3)}\n")
        }
        cursor.close()

        val cTotal = db.rawQuery(
            "SELECT total FROM ${DBHelper.T_PEDIDO} WHERE id = ?", arrayOf(idPedido.toString())
        )
        if (cTotal.moveToFirst()) {
            sb.append("Total: S/ %.2f\n".format(cTotal.getDouble(0)))
        }
        cTotal.close()

        sb.append("Estado: PENDIENTE\n")
        sb.append("Cliente: $telefonoCliente")
        return sb.toString()
    }

    fun obtenerTelefonoAdmin(): String {
        val cursor = db.rawQuery(
            "SELECT telefono FROM ${DBHelper.T_USUARIO} WHERE rol = 'ADMIN' LIMIT 1", null
        )
        val tel = if (cursor.moveToFirst()) cursor.getString(0) else ""
        cursor.close()
        return tel
    }

    fun listarPorEstado(estado: String): List<Triple<Int, String, Double>> {
        val lista = mutableListOf<Triple<Int, String, Double>>()
        val cursor = db.rawQuery(
            "SELECT p.id, c.nombres || ' ' || c.apellidos, p.total " +
                    "FROM ${DBHelper.T_PEDIDO} p " +
                    "JOIN ${DBHelper.T_CLIENTE} c ON c.id = p.id_cliente " +
                    "WHERE p.estado = ? ORDER BY p.id DESC",
            arrayOf(estado)
        )
        while (cursor.moveToNext()) {
            lista.add(Triple(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2)))
        }
        cursor.close()
        return lista
    }

    data class DetalleItem(
        val modelo: String,
        val talla: String,
        val color: String,
        val cantidad: Int,
        val precioUnit: Double,
        val foto: String
    )

    fun listarDetalle(idPedido: Int): List<DetalleItem> {
        val lista = mutableListOf<DetalleItem>()
        val cursor = db.rawQuery(
            "SELECT r.modelo, r.talla, r.color, d.cantidad, d.precio_unit, r.foto " +
                    "FROM ${DBHelper.T_DETALLE} d " +
                    "JOIN ${DBHelper.T_ROPA} r ON r.id = d.id_ropa " +
                    "WHERE d.id_pedido = ?", arrayOf(idPedido.toString())
        )
        while (cursor.moveToNext()) {
            lista.add(
                DetalleItem(
                    modelo = cursor.getString(0),
                    talla = cursor.getString(1),
                    color = cursor.getString(2),
                    cantidad = cursor.getInt(3),
                    precioUnit = cursor.getDouble(4),
                    foto = cursor.getString(5)
                )
            )
        }
        cursor.close()
        return lista
    }

    fun datosPedido(idPedido: Int): Triple<String, String, String>? {
        val cursor = db.rawQuery(
            "SELECT c.nombres || ' ' || c.apellidos, c.telefono, p.fecha " +
                    "FROM ${DBHelper.T_PEDIDO} p " +
                    "JOIN ${DBHelper.T_CLIENTE} c ON c.id = p.id_cliente " +
                    "WHERE p.id = ?", arrayOf(idPedido.toString())
        )
        return if (cursor.moveToFirst()) {
            val t = Triple(cursor.getString(0), cursor.getString(1), cursor.getString(2))
            cursor.close()
            t
        } else {
            cursor.close()
            null
        }
    }

    fun atender(idPedido: Int): String? {
        db.beginTransaction()
        return try {
            val cursor = db.rawQuery(
                "SELECT d.id_ropa, d.cantidad, r.modelo, r.cantidad " +
                        "FROM ${DBHelper.T_DETALLE} d " +
                        "JOIN ${DBHelper.T_ROPA} r ON r.id = d.id_ropa " +
                        "WHERE d.id_pedido = ?", arrayOf(idPedido.toString())
            )
            val items = mutableListOf<Triple<Int, Int, String>>()
            while (cursor.moveToNext()) {
                val idRopa = cursor.getInt(0)
                val cantPedida = cursor.getInt(1)
                val modelo = cursor.getString(2)
                val stock = cursor.getInt(3)
                if (cantPedida > stock) {
                    cursor.close()
                    return "Stock insuficiente: $modelo"
                }
                items.add(Triple(idRopa, cantPedida, modelo))
            }
            cursor.close()

            for (it in items) {
                db.execSQL(
                    "UPDATE ${DBHelper.T_ROPA} SET cantidad = cantidad - ? WHERE id = ?",
                    arrayOf(it.second, it.first)
                )
            }

            val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            db.execSQL(
                "UPDATE ${DBHelper.T_PEDIDO} SET estado = 'ATENDIDO', fecha_atencion = ? WHERE id = ?",
                arrayOf(fecha, idPedido)
            )

            db.setTransactionSuccessful()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            "Error al atender el pedido"
        } finally {
            db.endTransaction()
        }
    }
}