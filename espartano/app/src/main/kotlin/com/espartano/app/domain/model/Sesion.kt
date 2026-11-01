package com.espartano.app.domain.model

import com.espartano.app.data.db.entity.SesionEjercicioEntity
import com.espartano.app.data.db.relation.SesionConEjercicios
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class SesionEjercicio(
    val id: Long = 0,
    val sesionId: Long,
    val ejercicio: String,
    val grupoMuscular: String,
    val numeroSerie: Int = 1,
    val pesoKg: Float = 0f,
    val repeticiones: Int = 10
) {
    fun toEntity() = SesionEjercicioEntity(id, sesionId, ejercicio, grupoMuscular, numeroSerie, pesoKg, repeticiones)
}

data class Sesion(
    val id: Long = 0,
    val rutinaId: Long?,
    val nombre: String,
    val fecha: LocalDate,
    val duracionSegundos: Int = 0,
    val ejercicios: List<SesionEjercicio> = emptyList()
)

fun SesionConEjercicios.toDomain() = Sesion(
    id = sesion.id,
    rutinaId = sesion.rutinaId,
    nombre = sesion.nombre,
    fecha = Instant.ofEpochMilli(sesion.fecha).atZone(ZoneId.systemDefault()).toLocalDate(),
    duracionSegundos = sesion.duracionSegundos,
    ejercicios = ejercicios.map {
        SesionEjercicio(it.id, it.sesionId, it.ejercicio, it.grupoMuscular, it.numeroSerie, it.pesoKg, it.repeticiones)
    }
)
