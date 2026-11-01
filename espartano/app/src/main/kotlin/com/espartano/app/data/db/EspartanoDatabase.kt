package com.espartano.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.espartano.app.data.db.dao.EntrenamientoDao
import com.espartano.app.data.db.dao.MedicionDao
import com.espartano.app.data.db.dao.PerfilDao
import com.espartano.app.data.db.dao.RutinaDao
import com.espartano.app.data.db.dao.SesionDao
import com.espartano.app.data.db.entity.EntrenamientoEntity
import com.espartano.app.data.db.entity.MedicionEntity
import com.espartano.app.data.db.entity.PerfilEntity
import com.espartano.app.data.db.entity.RutinaEjercicioEntity
import com.espartano.app.data.db.entity.RutinaEntity
import com.espartano.app.data.db.entity.SesionEjercicioEntity
import com.espartano.app.data.db.entity.SesionEntity

@Database(
    entities = [
        PerfilEntity::class,
        MedicionEntity::class,
        EntrenamientoEntity::class,
        RutinaEntity::class,
        RutinaEjercicioEntity::class,
        SesionEntity::class,
        SesionEjercicioEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class EspartanoDatabase : RoomDatabase() {
    abstract fun perfilDao(): PerfilDao
    abstract fun medicionDao(): MedicionDao
    abstract fun entrenamientoDao(): EntrenamientoDao
    abstract fun rutinaDao(): RutinaDao
    abstract fun sesionDao(): SesionDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS entrenamientos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        fecha INTEGER NOT NULL,
                        ejercicio TEXT NOT NULL,
                        grupo_muscular TEXT NOT NULL,
                        peso_kg REAL NOT NULL,
                        series INTEGER NOT NULL,
                        repeticiones INTEGER NOT NULL,
                        notas TEXT NOT NULL DEFAULT ''
                    )"""
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS rutinas (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT NOT NULL)")
                db.execSQL("""CREATE TABLE IF NOT EXISTS rutina_ejercicios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    rutina_id INTEGER NOT NULL,
                    ejercicio TEXT NOT NULL,
                    grupo_muscular TEXT NOT NULL,
                    orden INTEGER NOT NULL
                )""")
                db.execSQL("""CREATE TABLE IF NOT EXISTS sesiones (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    rutina_id INTEGER,
                    rutina_nombre TEXT NOT NULL,
                    fecha INTEGER NOT NULL
                )""")
                db.execSQL("""CREATE TABLE IF NOT EXISTS sesion_ejercicios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    sesion_id INTEGER NOT NULL,
                    ejercicio TEXT NOT NULL,
                    grupo_muscular TEXT NOT NULL,
                    peso_kg REAL NOT NULL DEFAULT 0,
                    series INTEGER NOT NULL DEFAULT 3,
                    repeticiones INTEGER NOT NULL DEFAULT 10
                )""")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Recreate sesiones with new column names and duracion_segundos
                db.execSQL("""CREATE TABLE IF NOT EXISTS sesiones_new (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    rutina_id INTEGER,
                    nombre TEXT NOT NULL DEFAULT '',
                    fecha INTEGER NOT NULL,
                    duracion_segundos INTEGER NOT NULL DEFAULT 0
                )""")
                db.execSQL("INSERT INTO sesiones_new (id, rutina_id, nombre, fecha) SELECT id, rutina_id, rutina_nombre, fecha FROM sesiones")
                db.execSQL("DROP TABLE sesiones")
                db.execSQL("ALTER TABLE sesiones_new RENAME TO sesiones")

                // Recreate sesion_ejercicios with per-set schema (numero_serie instead of series)
                db.execSQL("DROP TABLE IF EXISTS sesion_ejercicios")
                db.execSQL("""CREATE TABLE IF NOT EXISTS sesion_ejercicios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    sesion_id INTEGER NOT NULL,
                    ejercicio TEXT NOT NULL,
                    grupo_muscular TEXT NOT NULL,
                    numero_serie INTEGER NOT NULL DEFAULT 1,
                    peso_kg REAL NOT NULL DEFAULT 0,
                    repeticiones INTEGER NOT NULL DEFAULT 10
                )""")
            }
        }
    }
}
