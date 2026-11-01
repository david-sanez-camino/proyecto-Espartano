package com.espartano.app.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rutina_ejercicios")
data class RutinaEjercicioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "rutina_id") val rutinaId: Long,
    @ColumnInfo(name = "ejercicio") val ejercicio: String,
    @ColumnInfo(name = "grupo_muscular") val grupoMuscular: String,
    @ColumnInfo(name = "orden") val orden: Int
)
