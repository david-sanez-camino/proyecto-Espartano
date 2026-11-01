package com.espartano.app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.espartano.app.data.db.entity.SesionEjercicioEntity
import com.espartano.app.data.db.entity.SesionEntity
import com.espartano.app.data.db.relation.SesionConEjercicios
import kotlinx.coroutines.flow.Flow

@Dao
interface SesionDao {

    @Transaction
    @Query("SELECT * FROM sesiones ORDER BY fecha DESC")
    fun observarTodasConEjercicios(): Flow<List<SesionConEjercicios>>

    @Insert
    suspend fun insertarSesion(sesion: SesionEntity): Long

    @Insert
    suspend fun insertarEjercicios(ejercicios: List<SesionEjercicioEntity>)

    @Query("SELECT * FROM sesion_ejercicios WHERE ejercicio = :ejercicio ORDER BY sesion_id DESC, numero_serie DESC LIMIT 1")
    suspend fun obtenerUltimoRegistroEjercicio(ejercicio: String): SesionEjercicioEntity?
}
