package com.espartano.app.data.repository

import com.espartano.app.data.db.dao.EntrenamientoDao
import com.espartano.app.data.db.entity.EntrenamientoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntrenamientoRepository @Inject constructor(private val dao: EntrenamientoDao) {

    fun observarTodos(): Flow<List<EntrenamientoEntity>> = dao.observarTodos()

    fun observarPorEjercicio(ejercicio: String): Flow<List<EntrenamientoEntity>> =
        dao.observarPorEjercicio(ejercicio)

    suspend fun insertar(entrenamiento: EntrenamientoEntity): Long = dao.insertar(entrenamiento)

    suspend fun eliminar(entrenamiento: EntrenamientoEntity) = dao.eliminar(entrenamiento)
}
