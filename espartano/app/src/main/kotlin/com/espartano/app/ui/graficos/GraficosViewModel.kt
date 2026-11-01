package com.espartano.app.ui.graficos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.espartano.app.data.repository.MedicionRepository
import com.espartano.app.data.repository.PerfilRepository
import com.espartano.app.domain.model.Medicion
import com.espartano.app.domain.model.Perfil
import com.espartano.app.domain.model.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

enum class RangoFechas(val label: String) {
    TREINTA_DIAS("30 días"),
    TRES_MESES("3 meses"),
    SEIS_MESES("6 meses"),
    TODO("Todo")
}

enum class MetricaGrafico(val label: String) {
    PESO("Peso"),
    GRASA("% Grasa"),
    MUSCULO("% Músculo"),
    IMC("IMC")
}

data class GraficosUiState(
    val mediciones: List<Medicion> = emptyList(),
    val perfil: Perfil? = null,
    val rango: RangoFechas = RangoFechas.TREINTA_DIAS,
    val metrica: MetricaGrafico = MetricaGrafico.PESO
) {
    val medicionesFiltradas: List<Medicion> get() {
        val desde = when (rango) {
            RangoFechas.TREINTA_DIAS -> LocalDate.now().minusDays(30)
            RangoFechas.TRES_MESES -> LocalDate.now().minusMonths(3)
            RangoFechas.SEIS_MESES -> LocalDate.now().minusMonths(6)
            RangoFechas.TODO -> LocalDate.MIN
        }
        return mediciones.filter { !it.fecha.isBefore(desde) }.reversed()
    }

    val valoresGrafico: List<Float> get() = medicionesFiltradas.map { m ->
        when (metrica) {
            MetricaGrafico.PESO -> m.pesoKg
            MetricaGrafico.GRASA -> m.grasaPct
            MetricaGrafico.MUSCULO -> m.musculoPct
            MetricaGrafico.IMC -> perfil?.let { m.imc(it.alturaCm) } ?: 0f
        }
    }
}

@HiltViewModel
class GraficosViewModel @Inject constructor(
    medicionRepo: MedicionRepository,
    perfilRepo: PerfilRepository
) : ViewModel() {

    private val _rango = MutableStateFlow(RangoFechas.TREINTA_DIAS)
    private val _metrica = MutableStateFlow(MetricaGrafico.PESO)

    val uiState = combine(
        medicionRepo.observarTodas(),
        perfilRepo.observarPerfil(),
        _rango,
        _metrica
    ) { entities, perfilEntity, rango, metrica ->
        GraficosUiState(
            mediciones = entities.map { it.toDomain() },
            perfil = perfilEntity?.toDomain(),
            rango = rango,
            metrica = metrica
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GraficosUiState())

    fun setRango(r: RangoFechas) = _rango.update { r }
    fun setMetrica(m: MetricaGrafico) = _metrica.update { m }
}
