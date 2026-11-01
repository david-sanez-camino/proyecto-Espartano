package com.espartano.app.ui.gimnasio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.espartano.app.data.repository.RutinaRepository
import com.espartano.app.data.repository.SesionRepository
import com.espartano.app.domain.model.Rutina
import com.espartano.app.domain.model.Sesion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GimnasioViewModel @Inject constructor(
    private val rutinaRepo: RutinaRepository,
    private val sesionRepo: SesionRepository
) : ViewModel() {

    val rutinas: StateFlow<List<Rutina>> = rutinaRepo.observarTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val sesiones: StateFlow<List<Sesion>> = sesionRepo.observarTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun eliminarRutina(rutinaId: Long) {
        viewModelScope.launch { rutinaRepo.eliminar(rutinaId) }
    }
}
