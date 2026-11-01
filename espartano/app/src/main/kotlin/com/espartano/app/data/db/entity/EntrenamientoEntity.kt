package com.espartano.app.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entrenamientos")
data class EntrenamientoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fecha") val fecha: Long,
    @ColumnInfo(name = "ejercicio") val ejercicio: String,
    @ColumnInfo(name = "grupo_muscular") val grupoMuscular: String,
    @ColumnInfo(name = "peso_kg") val pesoKg: Float,
    @ColumnInfo(name = "series") val series: Int,
    @ColumnInfo(name = "repeticiones") val repeticiones: Int,
    @ColumnInfo(name = "notas") val notas: String = ""
)
