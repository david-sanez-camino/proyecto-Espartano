package com.espartano.app.domain.model

import com.espartano.app.data.db.entity.RutinaEjercicioEntity
import com.espartano.app.data.db.entity.RutinaEntity
import com.espartano.app.data.db.relation.RutinaConEjercicios

data class RutinaEjercicio(
    val id: Long = 0,
    val rutinaId: Long,
    val ejercicio: String,
    val grupoMuscular: String,
    val orden: Int
) {
    fun toEntity() = RutinaEjercicioEntity(id, rutinaId, ejercicio, grupoMuscular, orden)
}

data class Rutina(
    val id: Long = 0,
    val nombre: String,
    val ejercicios: List<RutinaEjercicio> = emptyList()
)

fun RutinaConEjercicios.toDomain() = Rutina(
    id = rutina.id,
    nombre = rutina.nombre,
    ejercicios = ejercicios.map {
        RutinaEjercicio(it.id, it.rutinaId, it.ejercicio, it.grupoMuscular, it.orden)
    }
)
