package com.espartano.app.ui.metas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.espartano.app.data.repository.MedicionRepository
import com.espartano.app.data.repository.PerfilRepository
import com.espartano.app.domain.model.Perfil
import com.espartano.app.domain.model.toDomain
import com.espartano.app.domain.model.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class MetasUiState(
    val perfil: Perfil? = null,
    val pesoObjetivo: String = "",
    val grasaObjetivo: String = "",
    val ultimoPeso: Float? = null,
    val ultimaGrasa: Float? = null,
    val snackMsg: String? = null
)

@HiltViewModel
class MetasViewModel @Inject constructor(
    private val perfilRepo: PerfilRepository,
    medicionRepo: MedicionRepository
) : ViewModel() {

    private val _pesoObjetivo = MutableStateFlow("")
    private val _grasaObjetivo = MutableStateFlow("")
    private val _snack = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            val perfil = perfilRepo.obtenerPerfil()?.toDomain()
            perfil?.let {
                _pesoObjetivo.value = it.pesoObjetivoKg?.toString() ?: ""
                _grasaObjetivo.value = it.grasaObjetivoPct?.toString() ?: ""
            }
        }
    }

    val uiState = combine(
        perfilRepo.observarPerfil(),
        medicionRepo.observarTodas(),
        _pesoObjetivo,
        _grasaObjetivo,
        _snack
    ) { perfilEntity, mediciones, pesoObj, grasaObj, snack ->
        val perfil = perfilEntity?.toDomain()
        val ultima = mediciones.firstOrNull()?.toDomain()
        MetasUiState(
            perfil = perfil,
            pesoObjetivo = pesoObj.ifEmpty { perfil?.pesoObjetivoKg?.toString() ?: "" },
            grasaObjetivo = grasaObj.ifEmpty { perfil?.grasaObjetivoPct?.toString() ?: "" },
            ultimoPeso = ultima?.pesoKg,
            ultimaGrasa = ultima?.grasaPct,
            snackMsg = snack
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MetasUiState())

    fun onPesoObjetivoChange(v: String) = _pesoObjetivo.update { v }
    fun onGrasaObjetivoChange(v: String) = _grasaObjetivo.update { v }

    fun guardarMetas() {
        viewModelScope.launch {
            val existente = perfilRepo.obtenerPerfil()?.toDomain()
            val base = existente ?: Perfil(
                alturaCm = 170f,
                fechaNacimiento = LocalDate.of(1990, 1, 1),
                pesoObjetivoKg = null,
                grasaObjetivoPct = null,
                frecuenciaRecordatorioDias = 7
            )
            val actualizado = base.copy(
                pesoObjetivoKg = _pesoObjetivo.value.toFloatOrNull(),
                grasaObjetivoPct = _grasaObjetivo.value.toFloatOrNull()
            )
            perfilRepo.guardarPerfil(actualizado.toEntity())
            _snack.update { "Metas guardadas" }
        }
    }

    fun dismissSnack() = _snack.update { null }
}
