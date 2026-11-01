package com.espartano.app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.espartano.app.data.db.entity.RutinaEjercicioEntity
import com.espartano.app.data.db.entity.RutinaEntity
import com.espartano.app.data.db.relation.RutinaConEjercicios
import kotlinx.coroutines.flow.Flow

@Dao
interface RutinaDao {

    @Transaction
    @Query("SELECT * FROM rutinas ORDER BY nombre ASC")
    fun observarTodasConEjercicios(): Flow<List<RutinaConEjercicios>>

    @Transaction
    @Query("SELECT * FROM rutinas WHERE id = :id")
    suspend fun obtenerConEjercicios(id: Long): RutinaConEjercicios?

    @Insert
    suspend fun insertarRutina(rutina: RutinaEntity): Long

    @Insert
    suspend fun insertarEjercicios(ejercicios: List<RutinaEjercicioEntity>)

    @Query("DELETE FROM rutinas WHERE id = :id")
    suspend fun eliminarRutina(id: Long)

    @Query("DELETE FROM rutina_ejercicios WHERE rutina_id = :rutinaId")
    suspend fun eliminarEjerciciosDeRutina(rutinaId: Long)
}
