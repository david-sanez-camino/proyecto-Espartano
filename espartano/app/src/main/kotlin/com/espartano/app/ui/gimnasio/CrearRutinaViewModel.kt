package com.espartano.app.ui.gimnasio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.espartano.app.data.repository.RutinaRepository
import com.espartano.app.domain.model.EjerciciosCatalogo
import com.espartano.app.domain.model.RutinaEjercicio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CrearRutinaUiState(
    val nombre: String = "",
    val ejerciciosSeleccionados: List<RutinaEjercicio> = emptyList(),
    val mostrarSelectorGrupo: Boolean = false,
    val mostrarSelectorEjercicio: Boolean = false,
    val grupoSeleccionado: String? = null,
    val guardado: Boolean = false,
    val errorNombre: Boolean = false
)

@HiltViewModel
class CrearRutinaViewModel @Inject constructor(
    private val rutinaRepo: RutinaRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CrearRutinaUiState())
    val uiState: StateFlow<CrearRutinaUiState> = _state

    fun onNombreChange(v: String) = _state.update { it.copy(nombre = v, errorNombre = false) }

    fun abrirSelectorGrupo() = _state.update { it.copy(mostrarSelectorGrupo = true) }
    fun cerrarSelectorGrupo() = _state.update { it.copy(mostrarSelectorGrupo = false) }

    fun seleccionarGrupo(grupo: String) =
        _state.update { it.copy(mostrarSelectorGrupo = false, mostrarSelectorEjercicio = true, grupoSeleccionado = grupo) }

    fun cerrarSelectorEjercicio() =
        _state.update { it.copy(mostrarSelectorEjercicio = false, grupoSeleccionado = null) }

    fun agregarEjercicio(ejercicio: EjerciciosCatalogo.Ejercicio) {
        _state.update { s ->
            val yaExiste = s.ejerciciosSeleccionados.any { it.ejercicio == ejercicio.nombre }
            if (yaExiste) s.copy(mostrarSelectorEjercicio = false, grupoSeleccionado = null)
            else s.copy(
                ejerciciosSeleccionados = s.ejerciciosSeleccionados + RutinaEjercicio(
                    rutinaId = 0,
                    ejercicio = ejercicio.nombre,
                    grupoMuscular = ejercicio.grupoMuscular,
                    orden = s.ejerciciosSeleccionados.size
                ),
                mostrarSelectorEjercicio = false,
                grupoSeleccionado = null
            )
        }
    }

    fun quitarEjercicio(ejercicio: RutinaEjercicio) =
        _state.update { it.copy(ejerciciosSeleccionados = it.ejerciciosSeleccionados.filter { e -> e != ejercicio }) }

    fun guardar() {
        val s = _state.value
        if (s.nombre.isBlank()) { _state.update { it.copy(errorNombre = true) }; return }
        if (s.ejerciciosSeleccionados.isEmpty()) return
        viewModelScope.launch {
            rutinaRepo.crear(s.nombre.trim(), s.ejerciciosSeleccionados)
            _state.update { it.copy(guardado = true) }
        }
    }
}
