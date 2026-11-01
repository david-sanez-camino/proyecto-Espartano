package com.espartano.app.data.repository

import com.espartano.app.data.db.dao.SesionDao
import com.espartano.app.data.db.entity.SesionEntity
import com.espartano.app.domain.model.Sesion
import com.espartano.app.domain.model.SesionEjercicio
import com.espartano.app.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SesionRepository @Inject constructor(private val dao: SesionDao) {

    fun observarTodas(): Flow<List<Sesion>> =
        dao.observarTodasConEjercicios().map { list -> list.map { it.toDomain() } }

    suspend fun guardar(
        rutinaId: Long?,
        nombre: String,
        fecha: LocalDate,
        duracionSegundos: Int,
        ejercicios: List<SesionEjercicio>
    ) {
        val fechaMillis = fecha.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val sesionId = dao.insertarSesion(
            SesionEntity(rutinaId = rutinaId, nombre = nombre, fecha = fechaMillis, duracionSegundos = duracionSegundos)
        )
        dao.insertarEjercicios(ejercicios.map { it.copy(sesionId = sesionId).toEntity() })
    }

    suspend fun obtenerUltimoRegistro(ejercicio: String): SesionEjercicio? {
        val e = dao.obtenerUltimoRegistroEjercicio(ejercicio) ?: return null
        return SesionEjercicio(e.id, e.sesionId, e.ejercicio, e.grupoMuscular, e.numeroSerie, e.pesoKg, e.repeticiones)
    }
}
