package com.espartano.app.ui.entrenamiento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.espartano.app.data.repository.EntrenamientoRepository
import com.espartano.app.domain.model.EjerciciosCatalogo
import com.espartano.app.domain.model.Entrenamiento
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

data class EntrenamientoUiState(
    val historial: List<Entrenamiento> = emptyList(),
    val grupoSeleccionado: String? = null,
    val ejercicioSeleccionado: EjerciciosCatalogo.Ejercicio? = null,
    val pesoKg: String = "",
    val series: String = "3",
    val repeticiones: String = "10",
    val mostrarSelectorGrupo: Boolean = false,
    val mostrarSelectorEjercicio: Boolean = false,
    val snackMsg: String? = null,
    val errorPeso: String? = null
)

@HiltViewModel
class EntrenamientoViewModel @Inject constructor(
    private val repo: EntrenamientoRepository
) : ViewModel() {

    private val _grupoSeleccionado = MutableStateFlow<String?>(null)
    private val _ejercicioSeleccionado = MutableStateFlow<EjerciciosCatalogo.Ejercicio?>(null)
    private val _pesoKg = MutableStateFlow("")
    private val _series = MutableStateFlow("3")
    private val _repeticiones = MutableStateFlow("10")
    private val _mostrarSelectorGrupo = MutableStateFlow(false)
    private val _mostrarSelectorEjercicio = MutableStateFlow(false)
    private val _snack = MutableStateFlow<String?>(null)
    private val _errorPeso = MutableStateFlow<String?>(null)

    val uiState = combine(
        repo.observarTodos(),
        _grupoSeleccionado,
        _ejercicioSeleccionado,
        _pesoKg,
        _series
    ) { historialEntities, grupo, ejercicio, peso, series ->
        EntrenamientoUiState(
            historial = historialEntities.map { it.toDomain() },
            grupoSeleccionado = grupo,
            ejercicioSeleccionado = ejercicio,
            pesoKg = peso,
            series = series
        )
    }.combine(_repeticiones) { state, reps ->
        state.copy(repeticiones = reps)
    }.combine(_mostrarSelectorGrupo) { state, show ->
        state.copy(mostrarSelectorGrupo = show)
    }.combine(_mostrarSelectorEjercicio) { state, show ->
        state.copy(mostrarSelectorEjercicio = show)
    }.combine(_snack) { state, snack ->
        state.copy(snackMsg = snack)
    }.combine(_errorPeso) { state, err ->
        state.copy(errorPeso = err)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EntrenamientoUiState())

    fun onPesoChange(v: String) { _pesoKg.update { v }; _errorPeso.update { null } }
    fun onSeriesChange(v: String) = _series.update { v }
    fun onRepeticionesChange(v: String) = _repeticiones.update { v }

    fun abrirSelectorGrupo() = _mostrarSelectorGrupo.update { true }
    fun cerrarSelectorGrupo() = _mostrarSelectorGrupo.update { false }

    fun seleccionarGrupo(grupo: String) {
        _grupoSeleccionado.update { grupo }
        _ejercicioSeleccionado.update { null }
        _mostrarSelectorGrupo.update { false }
        _mostrarSelectorEjercicio.update { true }
    }

    fun seleccionarEjercicio(ejercicio: EjerciciosCatalogo.Ejercicio) {
        _ejercicioSeleccionado.update { ejercicio }
        _mostrarSelectorEjercicio.update { false }
    }

    fun cerrarSelectorEjercicio() = _mostrarSelectorEjercicio.update { false }

    fun guardar() {
        val ejercicio = _ejercicioSeleccionado.value ?: run {
            _snack.update { "Selecciona un ejercicio primero" }; return
        }
        val peso = _pesoKg.value.toFloatOrNull()
        if (peso == null || peso < 0) {
            _errorPeso.update { "Introduce un peso válido" }; return
        }
        val series = _series.value.toIntOrNull() ?: 1
        val reps = _repeticiones.value.toIntOrNull() ?: 1

        viewModelScope.launch {
            repo.insertar(
                Entrenamiento(
                    fecha = LocalDate.now(),
                    ejercicio = ejercicio.nombre,
                    grupoMuscular = ejercicio.grupoMuscular,
                    pesoKg = peso,
                    series = series,
                    repeticiones = reps
                ).toEntity()
            )
            _pesoKg.update { "" }
            _series.update { "3" }
            _repeticiones.update { "10" }
            _ejercicioSeleccionado.update { null }
            _snack.update { "Registro guardado ⚔️" }
        }
    }

    fun eliminar(entrenamiento: Entrenamiento) {
        viewModelScope.launch { repo.eliminar(entrenamiento.toEntity()) }
    }

    fun dismissSnack() = _snack.update { null }
}
