package com.espartano.app.domain.model

import com.espartano.app.data.db.entity.EntrenamientoEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class Entrenamiento(
    val id: Long = 0,
    val fecha: LocalDate,
    val ejercicio: String,
    val grupoMuscular: String,
    val pesoKg: Float,
    val series: Int,
    val repeticiones: Int,
    val notas: String = ""
)

fun EntrenamientoEntity.toDomain(): Entrenamiento = Entrenamiento(
    id = id,
    fecha = Instant.ofEpochMilli(fecha).atZone(ZoneId.systemDefault()).toLocalDate(),
    ejercicio = ejercicio,
    grupoMuscular = grupoMuscular,
    pesoKg = pesoKg,
    series = series,
    repeticiones = repeticiones,
    notas = notas
)

fun Entrenamiento.toEntity(): EntrenamientoEntity = EntrenamientoEntity(
    id = id,
    fecha = fecha.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
    ejercicio = ejercicio,
    grupoMuscular = grupoMuscular,
    pesoKg = pesoKg,
    series = series,
    repeticiones = repeticiones,
    notas = notas
)
