package com.gutierrez.modaapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "modaapp.db"
        const val DB_VERSION = 1

        const val T_USUARIO = "usuario"
        const val T_CATEGORIA = "categoria"
        const val T_ROPA = "ropa"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys = ON")

        db.execSQL("""
            CREATE TABLE $T_USUARIO (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT UNIQUE,
                clave TEXT,
                rol TEXT,
                telefono TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $T_CATEGORIA (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT UNIQUE
            )
        """)

        db.execSQL("""
            CREATE TABLE $T_ROPA (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                modelo TEXT NOT NULL,
                id_categoria INTEGER,
                talla TEXT,
                marca TEXT,
                color TEXT,
                precio REAL CHECK(precio > 0),
                cantidad INTEGER CHECK(cantidad >= 0),
                foto TEXT,
                FOREIGN KEY (id_categoria) REFERENCES $T_CATEGORIA(id)
            )
        """)

        db.execSQL("INSERT INTO $T_USUARIO (usuario, clave, rol, telefono) VALUES ('admin', '1234', 'ADMIN', '999888777')")

        val categorias = listOf("Polos", "Pantalones", "Vestidos", "Casacas", "Zapatillas")
        for (c in categorias) {
            db.execSQL("INSERT INTO $T_CATEGORIA (nombre) VALUES ('$c')")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Sprint 3 lo usará
    }
}