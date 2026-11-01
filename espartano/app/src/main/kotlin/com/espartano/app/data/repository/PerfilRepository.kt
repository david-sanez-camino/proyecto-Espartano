package com.espartano.app.data.repository

import com.espartano.app.data.db.dao.PerfilDao
import com.espartano.app.data.db.entity.PerfilEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerfilRepository @Inject constructor(private val dao: PerfilDao) {

    fun observarPerfil(): Flow<PerfilEntity?> = dao.observarPerfil()

    suspend fun obtenerPerfil(): PerfilEntity? = dao.obtenerPerfil()

    suspend fun guardarPerfil(perfil: PerfilEntity) = dao.guardarPerfil(perfil)
}
