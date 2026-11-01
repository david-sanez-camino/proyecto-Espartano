package com.espartano.app.domain.model

import com.espartano.app.data.db.entity.MedicionEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class Medicion(
    val id: Long = 0,
    val fecha: LocalDate,
    val pesoKg: Float,
    val grasaPct: Float,
    val musculoPct: Float
) {
    fun imc(alturaCm: Float): Float {
        val alturaM = alturaCm / 100f
        return pesoKg / (alturaM * alturaM)
    }
}

fun MedicionEntity.toDomain(): Medicion = Medicion(
    id = id,
    fecha = Instant.ofEpochMilli(fecha).atZone(ZoneId.systemDefault()).toLocalDate(),
    pesoKg = pesoKg,
    grasaPct = grasaPct,
    musculoPct = musculoPct
)

fun Medicion.toEntity(): MedicionEntity = MedicionEntity(
    id = id,
    fecha = fecha.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
    pesoKg = pesoKg,
    grasaPct = grasaPct,
    musculoPct = musculoPct
)
