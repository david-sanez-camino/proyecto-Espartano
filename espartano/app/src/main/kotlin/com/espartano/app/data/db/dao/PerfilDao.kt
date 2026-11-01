package com.espartano.app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.espartano.app.data.db.entity.PerfilEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfilDao {

    @Query("SELECT * FROM perfil WHERE id = 1 LIMIT 1")
    fun observarPerfil(): Flow<PerfilEntity?>

    @Query("SELECT * FROM perfil WHERE id = 1 LIMIT 1")
    suspend fun obtenerPerfil(): PerfilEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarPerfil(perfil: PerfilEntity)

    @Update
    suspend fun actualizarPerfil(perfil: PerfilEntity)
}
