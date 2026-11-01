package com.espartano.app.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.espartano.app.data.db.entity.MedicionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicionDao {

    @Query("SELECT * FROM mediciones ORDER BY fecha DESC, id DESC")
    fun observarTodasMediciones(): Flow<List<MedicionEntity>>

    @Query("SELECT * FROM mediciones ORDER BY fecha DESC, id DESC LIMIT 2")
    suspend fun obtenerUltimasDos(): List<MedicionEntity>

    @Query("SELECT * FROM mediciones ORDER BY fecha DESC, id DESC LIMIT 1")
    suspend fun obtenerUltima(): MedicionEntity?

    @Query("SELECT * FROM mediciones WHERE fecha >= :desde ORDER BY fecha ASC, id ASC")
    fun observarMedicionesDesde(desde: Long): Flow<List<MedicionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(medicion: MedicionEntity): Long

    @Update
    suspend fun actualizar(medicion: MedicionEntity)

    @Delete
    suspend fun eliminar(medicion: MedicionEntity)

    @Query("SELECT * FROM mediciones ORDER BY fecha ASC, id ASC")
    suspend fun obtenerTodas(): List<MedicionEntity>
}
