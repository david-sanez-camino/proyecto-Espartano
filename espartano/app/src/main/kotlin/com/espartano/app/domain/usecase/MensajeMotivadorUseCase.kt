package com.espartano.app.domain.usecase

import com.espartano.app.domain.model.Medicion
import kotlin.math.abs
import javax.inject.Inject

enum class TipoMensaje { MEJORA, EMPEORA, ESTANCAMIENTO, PRIMERA_VEZ }

class MensajeMotivadorUseCase @Inject constructor() {

    private val mensajesVictoria = listOf(
        "¡VICTORIA! Te acercas a tu objetivo. El trabajo da sus frutos, soldado. Sigue.",
        "¡Más cerca de la meta! Cada décima que avanzas es una batalla ganada. No aflojes.",
        "¡Progreso real hacia el objetivo! Tu disciplina es visible en los números. Acelera.",
        "¡Te estás acercando! El espartano que hay en ti está funcionando. Más duro aún.",
        "¡Bien, soldado! Terreno ganado hacia tu meta. El camino espartano tiene resultados."
    )

    private val mensajesDerrota = listOf(
        "DERROTA. Te alejaste del objetivo. ¿Qué falló esta semana? Corrígelo ahora mismo.",
        "Retroceso detectado. Te alejas de la meta. Un espartano no repite este error: actúa.",
        "Los números no mienten: más lejos del objetivo. Levántate y redobla el esfuerzo.",
        "Alejamiento confirmado. Esto no es el fin, pero es una señal para cambiar algo YA.",
        "¿Esto es lo mejor que puedes dar? La meta se aleja. Necesito tu mejor versión."
    )

    private val mensajesEstancamiento = listOf(
        "Sin avance hacia el objetivo. El estancamiento es el enemigo silencioso. Rómpelo.",
        "Plano. Ni te acercas ni te alejas. ¿Tu cuerpo o tu esfuerzo? Revisa y actúa.",
        "La constancia es virtud, pero el conformismo es derrota. Cambia algo esta semana.",
        "Los espartanos no se estancaban. Evalúa qué puedes mejorar ahora mismo.",
        "Mismo punto, misma semana. El tiempo no espera. Tú tampoco deberías."
    )

    private val mensajesPrimeraVez = listOf(
        "Primera medición registrada. El camino empieza ahora. Sin excusas.",
        "Bienvenido, soldado. Esto es solo el punto de partida. El trabajo viene ahora.",
        "Datos guardados. El primer paso ya lo diste. No pares aquí."
    )

    operator fun invoke(
        actual: Medicion,
        anterior: Medicion?,
        pesoObjetivo: Float? = null,
        grasaObjetivo: Float? = null
    ): String {
        if (anterior == null) return mensajesPrimeraVez.random()
        return when (calcularTipo(actual, anterior, pesoObjetivo, grasaObjetivo)) {
            TipoMensaje.MEJORA -> mensajesVictoria.random()
            TipoMensaje.EMPEORA -> mensajesDerrota.random()
            TipoMensaje.ESTANCAMIENTO -> mensajesEstancamiento.random()
            TipoMensaje.PRIMERA_VEZ -> mensajesPrimeraVez.random()
        }
    }

    fun tipo(
        actual: Medicion,
        anterior: Medicion?,
        pesoObjetivo: Float? = null,
        grasaObjetivo: Float? = null
    ): TipoMensaje {
        if (anterior == null) return TipoMensaje.PRIMERA_VEZ
        return calcularTipo(actual, anterior, pesoObjetivo, grasaObjetivo)
    }

    private fun calcularTipo(
        actual: Medicion,
        anterior: Medicion,
        pesoObjetivo: Float?,
        grasaObjetivo: Float?
    ): TipoMensaje {
        val tieneObjetivos = pesoObjetivo != null || grasaObjetivo != null

        return if (tieneObjetivos) {
            // Distancia total al objetivo para ambas mediciones
            var distActual = 0f
            var distAnterior = 0f
            pesoObjetivo?.let { obj ->
                distActual += abs(actual.pesoKg - obj)
                distAnterior += abs(anterior.pesoKg - obj)
            }
            grasaObjetivo?.let { obj ->
                distActual += abs(actual.grasaPct - obj)
                distAnterior += abs(anterior.grasaPct - obj)
            }
            // diff negativo = se acercó al objetivo (VICTORIA)
            val diff = distActual - distAnterior
            when {
                diff < -0.1f -> TipoMensaje.MEJORA
                diff > 0.1f -> TipoMensaje.EMPEORA
                else -> TipoMensaje.ESTANCAMIENTO
            }
        } else {
            // Sin objetivos configurados: comparar grasa y músculo directamente
            val deltaGrasa = actual.grasaPct - anterior.grasaPct
            val deltaMusculo = actual.musculoPct - anterior.musculoPct
            when {
                deltaGrasa < -0.3f || deltaMusculo > 0.3f -> TipoMensaje.MEJORA
                deltaGrasa > 0.3f || deltaMusculo < -0.3f -> TipoMensaje.EMPEORA
                else -> TipoMensaje.ESTANCAMIENTO
            }
        }
    }
}
