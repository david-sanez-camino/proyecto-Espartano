package com.espartano.app.ui.ajustes

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.espartano.app.data.repository.MedicionRepository
import com.espartano.app.data.repository.PerfilRepository
import com.espartano.app.domain.model.Perfil
import com.espartano.app.domain.model.toDomain
import com.espartano.app.domain.model.toEntity
import com.espartano.app.domain.usecase.ExportarDatosUseCase
import com.espartano.app.notifications.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AjustesUiState(
    val altura: String = "",
    val fechaNacimiento: LocalDate? = null,
    val frecuenciaRecordatorio: Int = 7,
    val perfil: Perfil? = null,
    val snackMsg: String? = null
)

@HiltViewModel
class AjustesViewModel @Inject constructor(
    private val perfilRepo: PerfilRepository,
    private val medicionRepo: MedicionRepository,
    private val exportarUseCase: ExportarDatosUseCase,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    private val _altura = MutableStateFlow("")
    private val _fechaNac = MutableStateFlow<LocalDate?>(null)
    private val _frecuencia = MutableStateFlow(7)
    private val _snack = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            val perfil = perfilRepo.obtenerPerfil()?.toDomain()
            perfil?.let {
                _altura.value = it.alturaCm.toString()
                _fechaNac.value = it.fechaNacimiento
                _frecuencia.value = it.frecuenciaRecordatorioDias
            }
        }
    }

    val uiState = combine(
        perfilRepo.observarPerfil(),
        _altura, _fechaNac, _frecuencia, _snack
    ) { perfilEntity, alt, fnac, freq, snack ->
        val perfil = perfilEntity?.toDomain()
        AjustesUiState(
            altura = alt.ifEmpty { perfil?.alturaCm?.toString() ?: "" },
            fechaNacimiento = fnac ?: perfil?.fechaNacimiento,
            frecuenciaRecordatorio = if (freq != 7) freq else (perfil?.frecuenciaRecordatorioDias ?: 7),
            perfil = perfil,
            snackMsg = snack
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AjustesUiState())

    fun onAlturaChange(v: String) = _altura.update { v }
    fun onFechaNacimientoChange(fecha: LocalDate) = _fechaNac.update { fecha }
    fun onFrecuenciaChange(v: Int) = _frecuencia.update { v }

    fun guardarPerfil() {
        viewModelScope.launch {
            val alturaVal = _altura.value.toFloatOrNull()
                ?: perfilRepo.obtenerPerfil()?.alturaCm
                ?: return@launch
            val fecha = _fechaNac.value
                ?: perfilRepo.obtenerPerfil()?.toDomain()?.fechaNacimiento
                ?: return@launch
            val existente = perfilRepo.obtenerPerfil()?.toDomain()
            val nuevo = Perfil(
                alturaCm = alturaVal,
                fechaNacimiento = fecha,
                pesoObjetivoKg = existente?.pesoObjetivoKg,
                grasaObjetivoPct = existente?.grasaObjetivoPct,
                frecuenciaRecordatorioDias = _frecuencia.value
            )
            perfilRepo.guardarPerfil(nuevo.toEntity())
            notificationScheduler.programar(_frecuencia.value)
            _snack.update { "Perfil guardado correctamente" }
        }
    }

    fun exportarCsv(context: Context) {
        viewModelScope.launch {
            val mediciones = medicionRepo.obtenerTodas().map { it.toDomain() }
            val perfil = perfilRepo.obtenerPerfil()?.toDomain()
            val file = exportarUseCase.exportarCsv(mediciones, perfil)
            compartirArchivo(context, file, "text/csv")
            _snack.update { "CSV exportado" }
        }
    }

    fun exportarPdf(context: Context) {
        viewModelScope.launch {
            val mediciones = medicionRepo.obtenerTodas().map { it.toDomain() }
            val perfil = perfilRepo.obtenerPerfil()?.toDomain()
            val file = exportarUseCase.exportarPdf(mediciones, perfil)
            compartirArchivo(context, file, "application/pdf")
            _snack.update { "PDF exportado" }
        }
    }

    private fun compartirArchivo(context: Context, file: java.io.File, mimeType: String) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Compartir").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    fun dismissSnack() = _snack.update { null }
}
