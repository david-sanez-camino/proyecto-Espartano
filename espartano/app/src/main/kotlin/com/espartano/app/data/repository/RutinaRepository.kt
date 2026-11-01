package com.espartano.app.data.repository

import com.espartano.app.data.db.dao.RutinaDao
import com.espartano.app.data.db.entity.RutinaEntity
import com.espartano.app.domain.model.Rutina
import com.espartano.app.domain.model.RutinaEjercicio
import com.espartano.app.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RutinaRepository @Inject constructor(private val dao: RutinaDao) {

    fun observarTodas(): Flow<List<Rutina>> =
        dao.observarTodasConEjercicios().map { list -> list.map { it.toDomain() } }

    suspend fun obtener(id: Long): Rutina? =
        dao.obtenerConEjercicios(id)?.toDomain()

    suspend fun crear(nombre: String, ejercicios: List<RutinaEjercicio>) {
        val rutinaId = dao.insertarRutina(RutinaEntity(nombre = nombre))
        dao.insertarEjercicios(ejercicios.mapIndexed { idx, e ->
            e.copy(rutinaId = rutinaId, orden = idx).toEntity()
        })
    }

    suspend fun eliminar(rutinaId: Long) {
        dao.eliminarEjerciciosDeRutina(rutinaId)
        dao.eliminarRutina(rutinaId)
    }
}
