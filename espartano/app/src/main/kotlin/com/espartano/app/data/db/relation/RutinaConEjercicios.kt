package com.espartano.app.data.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.espartano.app.data.db.entity.RutinaEjercicioEntity
import com.espartano.app.data.db.entity.RutinaEntity

data class RutinaConEjercicios(
    @Embedded val rutina: RutinaEntity,
    @Relation(parentColumn = "id", entityColumn = "rutina_id")
    val ejercicios: List<RutinaEjercicioEntity>
)
