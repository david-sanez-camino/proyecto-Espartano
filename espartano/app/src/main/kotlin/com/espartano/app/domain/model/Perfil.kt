package com.espartano.app.domain.model

import com.espartano.app.data.db.entity.PerfilEntity
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

data class Perfil(
    val alturaCm: Float,
    val fechaNacimiento: LocalDate,
    val pesoObjetivoKg: Float?,
    val grasaObjetivoPct: Float?,
    val frecuenciaRecordatorioDias: Int
) {
    val edad: Int get() = Period.between(fechaNacimiento, LocalDate.now()).years
}

fun PerfilEntity.toDomain(): Perfil = Perfil(
    alturaCm = alturaCm,
    fechaNacimiento = Instant.ofEpochMilli(fechaNacimiento).atZone(ZoneId.systemDefault()).toLocalDate(),
    pesoObjetivoKg = pesoObjetivoKg,
    grasaObjetivoPct = grasaObjetivoPct,
    frecuenciaRecordatorioDias = frecuenciaRecordatorioDias
)

fun Perfil.toEntity(): PerfilEntity = PerfilEntity(
    alturaCm = alturaCm,
    fechaNacimiento = fechaNacimiento.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
    pesoObjetivoKg = pesoObjetivoKg,
    grasaObjetivoPct = grasaObjetivoPct,
    frecuenciaRecordatorioDias = frecuenciaRecordatorioDias
)
