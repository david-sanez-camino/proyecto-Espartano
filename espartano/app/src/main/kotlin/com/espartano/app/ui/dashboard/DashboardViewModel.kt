package com.espartano.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.espartano.app.data.repository.MedicionRepository
import com.espartano.app.data.repository.PerfilRepository
import com.espartano.app.domain.model.Medicion
import com.espartano.app.domain.model.Perfil
import com.espartano.app.domain.model.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class DashboardUiState(
    val perfil: Perfil? = null,
    val ultimaMedicion: Medicion? = null,
    val penultimaMedicion: Medicion? = null,
    val recientes: List<Medicion> = emptyList(),
    val pesoObjetivoProgreso: Float? = null,
    val grasaObjetivoProgreso: Float? = null,
    val perfilConfigurado: Boolean = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val perfilRepo: PerfilRepository,
    private val medicionRepo: MedicionRepository
) : ViewModel() {

    val uiState = combine(
        perfilRepo.observarPerfil(),
        medicionRepo.observarTodas()
    ) { perfilEntity, medicionEntities ->
        val perfil = perfilEntity?.toDomain()
        val mediciones = medicionEntities.map { it.toDomain() }
        val ultima = mediciones.firstOrNull()
        val penultima = mediciones.getOrNull(1)
        val recientes = mediciones.take(7).reversed()

        val pesoProgreso = calcularProgresoPeso(perfil, ultima, mediciones)
        val grasaProgreso = calcularProgresoGrasa(perfil, ultima)

        DashboardUiState(
            perfil = perfil,
            ultimaMedicion = ultima,
            penultimaMedicion = penultima,
            recientes = recientes,
            pesoObjetivoProgreso = pesoProgreso,
            grasaObjetivoProgreso = grasaProgreso,
            perfilConfigurado = perfil != null
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

    private fun calcularProgresoPeso(
        perfil: Perfil?,
        ultima: Medicion?,
        todas: List<Medicion>
    ): Float? {
        val objetivo = perfil?.pesoObjetivoKg ?: return null
        val actual = ultima?.pesoKg ?: return null
        val inicio = todas.lastOrNull()?.pesoKg ?: return null
        if (inicio == objetivo) return 1f
        return ((inicio - actual) / (inicio - objetivo)).coerceIn(0f, 1f)
    }

    private fun calcularProgresoGrasa(perfil: Perfil?, ultima: Medicion?): Float? {
        val objetivo = perfil?.grasaObjetivoPct ?: return null
        val actual = ultima?.grasaPct ?: return null
        return if (actual <= objetivo) 1f
        else (1f - (actual - objetivo) / actual).coerceIn(0f, 1f)
    }
}
