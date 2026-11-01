package com.espartano.app.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mediciones")
data class MedicionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "fecha") val fecha: Long,
    @ColumnInfo(name = "peso_kg") val pesoKg: Float,
    @ColumnInfo(name = "grasa_pct") val grasaPct: Float,
    @ColumnInfo(name = "musculo_pct") val musculoPct: Float
)
