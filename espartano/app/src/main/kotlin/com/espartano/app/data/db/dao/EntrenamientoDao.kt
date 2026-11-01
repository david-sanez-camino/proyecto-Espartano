package com.espartano.app.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.espartano.app.data.db.entity.EntrenamientoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EntrenamientoDao {

    @Query("SELECT * FROM entrenamientos ORDER BY fecha DESC")
    fun observarTodos(): Flow<List<EntrenamientoEntity>>

    @Query("SELECT * FROM entrenamientos WHERE ejercicio = :ejercicio ORDER BY fecha DESC")
    fun observarPorEjercicio(ejercicio: String): Flow<List<EntrenamientoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(entrenamiento: EntrenamientoEntity): Long

    @Delete
    suspend fun eliminar(entrenamiento: EntrenamientoEntity)

    @Query("SELECT * FROM entrenamientos ORDER BY fecha ASC")
    suspend fun obtenerTodos(): List<EntrenamientoEntity>
}
