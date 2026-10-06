package com.gutierrez.modaapp.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FotoUtils {

    fun copiarAInterno(context: Context, uri: Uri): String? {
        return try {
            val nombre = "ropa_${System.currentTimeMillis()}.jpg"
            val destino = File(context.filesDir, nombre)
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destino).use { output ->
                    input.copyTo(output)
                }
            }
            destino.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}