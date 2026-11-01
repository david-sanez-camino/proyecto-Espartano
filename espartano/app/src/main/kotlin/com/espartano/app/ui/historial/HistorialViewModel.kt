package com.espartano.app.ui.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.espartano.app.data.repository.MedicionRepository
import com.espartano.app.data.repository.PerfilRepository
import com.espartano.app.domain.model.Medicion
import com.espartano.app.domain.model.Perfil
import com.espartano.app.domain.model.toDomain
import com.espartano.app.domain.model.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistorialUiState(
    val mediciones: List<Medicion> = emptyList(),
    val perfil: Perfil? = null
)

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val medicionRepo: MedicionRepository,
    private val perfilRepo: PerfilRepository
) : ViewModel() {

    val uiState = combine(
        medicionRepo.observarTodas(),
        perfilRepo.observarPerfil()
    ) { entities, perfilEntity ->
        HistorialUiState(
            mediciones = entities.map { it.toDomain() },
            perfil = perfilEntity?.toDomain()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistorialUiState())

    fun eliminar(medicion: Medicion) {
        viewModelScope.launch {
            medicionRepo.eliminar(medicion.toEntity())
        }
    }
}
