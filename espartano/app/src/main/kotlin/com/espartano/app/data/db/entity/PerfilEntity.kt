package com.espartano.app.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfil")
data class PerfilEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "altura_cm") val alturaCm: Float,
    @ColumnInfo(name = "fecha_nacimiento") val fechaNacimiento: Long,
    @ColumnInfo(name = "peso_objetivo_kg") val pesoObjetivoKg: Float?,
    @ColumnInfo(name = "grasa_objetivo_pct") val grasaObjetivoPct: Float?,
    @ColumnInfo(name = "frecuencia_recordatorio_dias") val frecuenciaRecordatorioDias: Int = 7
)
