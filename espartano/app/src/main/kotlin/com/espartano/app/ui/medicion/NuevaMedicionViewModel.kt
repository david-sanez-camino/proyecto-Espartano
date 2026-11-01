package com.espartano.app.ui.medicion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.espartano.app.data.repository.MedicionRepository
import com.espartano.app.data.repository.PerfilRepository
import com.espartano.app.domain.model.Medicion
import com.espartano.app.domain.model.toDomain
import com.espartano.app.domain.model.toEntity
import com.espartano.app.domain.usecase.MensajeMotivadorUseCase
import com.espartano.app.domain.usecase.TipoMensaje
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class NuevaMedicionUiState(
    val peso: String = "",
    val grasa: String = "",
    val musculo: String = "",
    val guardando: Boolean = false,
    val errorPeso: String? = null,
    val errorGrasa: String? = null,
    val errorMusculo: String? = null,
    val mensajeMotivador: String? = null,
    val tipoMensaje: TipoMensaje = TipoMensaje.PRIMERA_VEZ,
    val guardadoExitoso: Boolean = false
)

@HiltViewModel
class NuevaMedicionViewModel @Inject constructor(
    private val medicionRepo: MedicionRepository,
    private val perfilRepo: PerfilRepository,
    private val mensajeUseCase: MensajeMotivadorUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NuevaMedicionUiState())
    val state = _state.asStateFlow()

    fun onPesoChange(v: String) = _state.update { it.copy(peso = v, errorPeso = null) }
    fun onGrasaChange(v: String) = _state.update { it.copy(grasa = v, errorGrasa = null) }
    fun onMusculoChange(v: String) = _state.update { it.copy(musculo = v, errorMusculo = null) }

    fun guardar() {
        val pesoVal = _state.value.peso.toFloatOrNull()
        val grasaVal = _state.value.grasa.toFloatOrNull()
        val musculoVal = _state.value.musculo.toFloatOrNull()

        var valid = true
        if (pesoVal == null || pesoVal <= 0 || pesoVal > 500) {
            _state.update { it.copy(errorPeso = "Introduce un peso válido (kg)") }; valid = false
        }
        if (grasaVal == null || grasaVal < 0 || grasaVal > 100) {
            _state.update { it.copy(errorGrasa = "Introduce un % de grasa válido (0-100)") }; valid = false
        }
        if (musculoVal == null || musculoVal < 0 || musculoVal > 100) {
            _state.update { it.copy(errorMusculo = "Introduce un % de músculo válido (0-100)") }; valid = false
        }
        if (!valid) return

        viewModelScope.launch {
            _state.update { it.copy(guardando = true) }
            val anterior = medicionRepo.obtenerUltima()?.toDomain()
            val perfil = perfilRepo.obtenerPerfil()?.toDomain()
            medicionRepo.insertar(
                com.espartano.app.data.db.entity.MedicionEntity(
                    fecha = System.currentTimeMillis(),
                    pesoKg = pesoVal!!,
                    grasaPct = grasaVal!!,
                    musculoPct = musculoVal!!
                )
            )
            val nueva = Medicion(
                fecha = LocalDate.now(),
                pesoKg = pesoVal!!,
                grasaPct = grasaVal!!,
                musculoPct = musculoVal!!
            )
            val pesoObj = perfil?.pesoObjetivoKg
            val grasaObj = perfil?.grasaObjetivoPct
            val mensaje = mensajeUseCase(nueva, anterior, pesoObj, grasaObj)
            val tipo = mensajeUseCase.tipo(nueva, anterior, pesoObj, grasaObj)
            _state.update { it.copy(guardando = false, mensajeMotivador = mensaje, tipoMensaje = tipo) }
        }
    }

    fun dismissMensaje() = _state.update { it.copy(mensajeMotivador = null, guardadoExitoso = true) }

    fun resetGuardado() = _state.update { it.copy(guardadoExitoso = false, peso = "", grasa = "", musculo = "") }
}
