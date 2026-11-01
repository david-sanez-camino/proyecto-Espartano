package com.espartano.app.ui.gimnasio

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.espartano.app.data.repository.RutinaRepository
import com.espartano.app.data.repository.SesionRepository
import com.espartano.app.domain.model.SesionEjercicio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class SerieActivaState(
    val numero: Int,
    val pesoKg: String = "",
    val repeticiones: String = "10",
    val completada: Boolean = false
)

data class EjercicioActivoState(
    val ejercicio: String,
    val grupoMuscular: String,
    val series: List<SerieActivaState> = listOf(SerieActivaState(numero = 1))
)

data class EntrenamientoActivoUiState(
    val nombre: String = "",
    val ejercicios: List<EjercicioActivoState> = emptyList(),
    val cargando: Boolean = true,
    val finalizado: Boolean = false,
    val tiempoSegundos: Int = 0
)

@HiltViewModel
class EntrenamientoActivoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val rutinaRepo: RutinaRepository,
    private val sesionRepo: SesionRepository
) : ViewModel() {

    private val rutinaId: Long = checkNotNull(savedStateHandle["rutinaId"])

    private val _state = MutableStateFlow(EntrenamientoActivoUiState())
    val uiState: StateFlow<EntrenamientoActivoUiState> = _state

    init {
        viewModelScope.launch {
            val rutina = rutinaRepo.obtener(rutinaId)
            if (rutina != null) {
                val ejerciciosState = rutina.ejercicios.map { e ->
                    val ultimo = sesionRepo.obtenerUltimoRegistro(e.ejercicio)
                    EjercicioActivoState(
                        ejercicio = e.ejercicio,
                        grupoMuscular = e.grupoMuscular,
                        series = listOf(
                            SerieActivaState(
                                numero = 1,
                                pesoKg = ultimo?.pesoKg?.takeIf { it > 0f }?.toString() ?: "",
                                repeticiones = ultimo?.repeticiones?.toString() ?: "10"
                            )
                        )
                    )
                }
                _state.update { it.copy(nombre = rutina.nombre, ejercicios = ejerciciosState, cargando = false) }
            } else {
                _state.update { it.copy(cargando = false) }
            }
        }

        // Cronómetro — empieza cuando se crea el ViewModel
        viewModelScope.launch {
            while (true) {
                delay(1000)
                if (!_state.value.finalizado) {
                    _state.update { it.copy(tiempoSegundos = it.tiempoSegundos + 1) }
                }
            }
        }
    }

    fun onNombreChange(v: String) = _state.update { it.copy(nombre = v) }

    fun addSerie(ejercicioIdx: Int) = _state.update { s ->
        val ejercicios = s.ejercicios.toMutableList()
        val ej = ejercicios[ejercicioIdx]
        val ultima = ej.series.lastOrNull()
        val nuevaSerie = SerieActivaState(
            numero = ej.series.size + 1,
            pesoKg = ultima?.pesoKg ?: "",
            repeticiones = ultima?.repeticiones ?: "10"
        )
        ejercicios[ejercicioIdx] = ej.copy(series = ej.series + nuevaSerie)
        s.copy(ejercicios = ejercicios)
    }

    fun removeSerie(ejercicioIdx: Int, serieIdx: Int) = _state.update { s ->
        val ejercicios = s.ejercicios.toMutableList()
        val ej = ejercicios[ejercicioIdx]
        if (ej.series.size <= 1) return@update s
        val newSeries = ej.series.toMutableList().also { it.removeAt(serieIdx) }
            .mapIndexed { i, serie -> serie.copy(numero = i + 1) }
        ejercicios[ejercicioIdx] = ej.copy(series = newSeries)
        s.copy(ejercicios = ejercicios)
    }

    fun onPesoChange(ejercicioIdx: Int, serieIdx: Int, v: String) =
        updateSerie(ejercicioIdx, serieIdx) { it.copy(pesoKg = v) }

    fun onRepsChange(ejercicioIdx: Int, serieIdx: Int, v: String) =
        updateSerie(ejercicioIdx, serieIdx) { it.copy(repeticiones = v) }

    fun toggleCompletada(ejercicioIdx: Int, serieIdx: Int) =
        updateSerie(ejercicioIdx, serieIdx) { it.copy(completada = !it.completada) }

    private fun updateSerie(ejercicioIdx: Int, serieIdx: Int, transform: (SerieActivaState) -> SerieActivaState) =
        _state.update { s ->
            val ejercicios = s.ejercicios.toMutableList()
            val ej = ejercicios[ejercicioIdx]
            val series = ej.series.toMutableList()
            series[serieIdx] = transform(series[serieIdx])
            ejercicios[ejercicioIdx] = ej.copy(series = series)
            s.copy(ejercicios = ejercicios)
        }

    fun finalizar() {
        val s = _state.value
        viewModelScope.launch {
            val ejerciciosDomain = mutableListOf<SesionEjercicio>()
            s.ejercicios.forEach { ej ->
                ej.series.forEach { serie ->
                    ejerciciosDomain.add(
                        SesionEjercicio(
                            sesionId = 0,
                            ejercicio = ej.ejercicio,
                            grupoMuscular = ej.grupoMuscular,
                            numeroSerie = serie.numero,
                            pesoKg = serie.pesoKg.toFloatOrNull() ?: 0f,
                            repeticiones = serie.repeticiones.toIntOrNull() ?: 10
                        )
                    )
                }
            }
            sesionRepo.guardar(
                rutinaId = rutinaId,
                nombre = s.nombre,
                fecha = LocalDate.now(),
                duracionSegundos = s.tiempoSegundos,
                ejercicios = ejerciciosDomain
            )
            _state.update { it.copy(finalizado = true) }
        }
    }
}
