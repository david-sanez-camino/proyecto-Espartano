package com.espartano.app.data.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.espartano.app.data.db.entity.SesionEjercicioEntity
import com.espartano.app.data.db.entity.SesionEntity

data class SesionConEjercicios(
    @Embedded val sesion: SesionEntity,
    @Relation(parentColumn = "id", entityColumn = "sesion_id")
    val ejercicios: List<SesionEjercicioEntity>
)
