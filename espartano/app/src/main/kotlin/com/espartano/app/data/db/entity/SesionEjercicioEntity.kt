package com.espartano.app.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sesion_ejercicios")
data class SesionEjercicioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "sesion_id") val sesionId: Long,
    @ColumnInfo(name = "ejercicio") val ejercicio: String,
    @ColumnInfo(name = "grupo_muscular") val grupoMuscular: String,
    @ColumnInfo(name = "numero_serie") val numeroSerie: Int = 1,
    @ColumnInfo(name = "peso_kg") val pesoKg: Float = 0f,
    @ColumnInfo(name = "repeticiones") val repeticiones: Int = 10
)
