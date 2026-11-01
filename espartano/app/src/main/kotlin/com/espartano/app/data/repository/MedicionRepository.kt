package com.espartano.app.data.repository

import com.espartano.app.data.db.dao.MedicionDao
import com.espartano.app.data.db.entity.MedicionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicionRepository @Inject constructor(private val dao: MedicionDao) {

    fun observarTodas(): Flow<List<MedicionEntity>> = dao.observarTodasMediciones()

    fun observarDesde(epochMs: Long): Flow<List<MedicionEntity>> = dao.observarMedicionesDesde(epochMs)

    suspend fun obtenerUltima(): MedicionEntity? = dao.obtenerUltima()

    suspend fun obtenerUltimasDos(): List<MedicionEntity> = dao.obtenerUltimasDos()

    suspend fun insertar(medicion: MedicionEntity): Long = dao.insertar(medicion)

    suspend fun actualizar(medicion: MedicionEntity) = dao.actualizar(medicion)

    suspend fun eliminar(medicion: MedicionEntity) = dao.eliminar(medicion)

    suspend fun obtenerTodas(): List<MedicionEntity> = dao.obtenerTodas()
}
