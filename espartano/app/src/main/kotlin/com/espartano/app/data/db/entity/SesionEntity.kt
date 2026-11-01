package com.espartano.app.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sesiones")
data class SesionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "rutina_id") val rutinaId: Long?,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "fecha") val fecha: Long,
    @ColumnInfo(name = "duracion_segundos") val duracionSegundos: Int = 0
)
