package com.espartano.app.domain.model

object EjerciciosCatalogo {

    data class Ejercicio(val nombre: String, val grupoMuscular: String)

    val todos: List<Ejercicio> = listOf(
        // PECHO
        Ejercicio("Press de banca plano", "Pecho"),
        Ejercicio("Press de banca inclinado", "Pecho"),
        Ejercicio("Press de banca declinado", "Pecho"),
        Ejercicio("Press con mancuernas plano", "Pecho"),
        Ejercicio("Press con mancuernas inclinado", "Pecho"),
        Ejercicio("Aperturas con mancuernas", "Pecho"),
        Ejercicio("Cruces en polea", "Pecho"),
        Ejercicio("Fondos en paralelas (pecho)", "Pecho"),
        Ejercicio("Pec Deck / Mariposa", "Pecho"),
        Ejercicio("Pullover con mancuerna", "Pecho"),

        // ESPALDA
        Ejercicio("Peso muerto convencional", "Espalda"),
        Ejercicio("Dominadas", "Espalda"),
        Ejercicio("Remo con barra", "Espalda"),
        Ejercicio("Remo con mancuerna", "Espalda"),
        Ejercicio("Jalón al pecho", "Espalda"),
        Ejercicio("Remo en polea baja", "Espalda"),
        Ejercicio("Remo en T", "Espalda"),
        Ejercicio("Face Pull", "Espalda"),
        Ejercicio("Jalón tras nuca", "Espalda"),
        Ejercicio("Buenos días", "Espalda"),
        Ejercicio("Hiperextensiones", "Espalda"),

        // HOMBROS
        Ejercicio("Press militar con barra", "Hombros"),
        Ejercicio("Press con mancuernas sentado", "Hombros"),
        Ejercicio("Elevaciones laterales", "Hombros"),
        Ejercicio("Elevaciones frontales", "Hombros"),
        Ejercicio("Pájaros / Deltoides posterior", "Hombros"),
        Ejercicio("Arnold Press", "Hombros"),
        Ejercicio("Remo al cuello", "Hombros"),
        Ejercicio("Encogimientos de hombros", "Hombros"),
        Ejercicio("Press en máquina de hombros", "Hombros"),

        // BÍCEPS
        Ejercicio("Curl de bíceps con barra", "Bíceps"),
        Ejercicio("Curl de bíceps con mancuernas", "Bíceps"),
        Ejercicio("Curl martillo", "Bíceps"),
        Ejercicio("Curl en predicador (Scott)", "Bíceps"),
        Ejercicio("Curl concentrado", "Bíceps"),
        Ejercicio("Curl en polea baja", "Bíceps"),
        Ejercicio("Curl con barra Z", "Bíceps"),
        Ejercicio("Curl inverso", "Bíceps"),

        // TRÍCEPS
        Ejercicio("Fondos en paralelas (tríceps)", "Tríceps"),
        Ejercicio("Press francés / Skull Crushers", "Tríceps"),
        Ejercicio("Extensión en polea alta", "Tríceps"),
        Ejercicio("Extensión de tríceps sobre cabeza", "Tríceps"),
        Ejercicio("Press cerrado", "Tríceps"),
        Ejercicio("Patada de tríceps con mancuerna", "Tríceps"),
        Ejercicio("Extensión con mancuerna sentado", "Tríceps"),
        Ejercicio("Diamond Push-ups", "Tríceps"),

        // PIERNAS
        Ejercicio("Sentadilla libre", "Piernas"),
        Ejercicio("Prensa de piernas", "Piernas"),
        Ejercicio("Peso muerto rumano", "Piernas"),
        Ejercicio("Curl femoral tumbado", "Piernas"),
        Ejercicio("Extensión de cuádriceps", "Piernas"),
        Ejercicio("Zancadas / Lunges", "Piernas"),
        Ejercicio("Sentadilla búlgara", "Piernas"),
        Ejercicio("Elevaciones de talón de pie", "Piernas"),
        Ejercicio("Elevaciones de talón sentado", "Piernas"),
        Ejercicio("Hip Thrust con barra", "Piernas"),
        Ejercicio("Hack Squat", "Piernas"),
        Ejercicio("Sentadilla goblet", "Piernas"),
        Ejercicio("Abductores en máquina", "Piernas"),
        Ejercicio("Aductores en máquina", "Piernas"),
        Ejercicio("Peso muerto sumo", "Piernas"),
        Ejercicio("Step-ups", "Piernas"),

        // ABDOMEN
        Ejercicio("Plancha frontal", "Abdomen"),
        Ejercicio("Crunch abdominal", "Abdomen"),
        Ejercicio("Russian Twist", "Abdomen"),
        Ejercicio("Elevación de piernas en barra", "Abdomen"),
        Ejercicio("Crunch en polea", "Abdomen"),
        Ejercicio("Rueda abdominal", "Abdomen"),
        Ejercicio("Plancha lateral", "Abdomen"),
        Ejercicio("Mountain Climbers", "Abdomen"),
        Ejercicio("Sit-ups", "Abdomen"),
        Ejercicio("Dragon Flag", "Abdomen"),

        // CARDIO
        Ejercicio("Carrera en cinta", "Cardio"),
        Ejercicio("Remo (máquina)", "Cardio"),
        Ejercicio("Bicicleta estática", "Cardio"),
        Ejercicio("Comba / Jump Rope", "Cardio"),
        Ejercicio("Elíptica", "Cardio"),
        Ejercicio("Escaladora", "Cardio"),
        Ejercicio("Battle Ropes", "Cardio"),
        Ejercicio("Burpees", "Cardio"),
        Ejercicio("Sprint", "Cardio"),
        Ejercicio("Natación", "Cardio"),

        // OLIMPICOS / FUNCIONAL
        Ejercicio("Cargada (Clean)", "Olímpico"),
        Ejercicio("Arrancada (Snatch)", "Olímpico"),
        Ejercicio("Clean & Jerk", "Olímpico"),
        Ejercicio("Kettlebell Swing", "Olímpico"),
        Ejercicio("Turkish Get-Up", "Olímpico"),
        Ejercicio("Box Jump", "Olímpico"),
        Ejercicio("Thruster", "Olímpico"),
        Ejercicio("Wall Ball", "Olímpico")
    )

    val grupos: List<String> = todos.map { it.grupoMuscular }.distinct()

    fun porGrupo(grupo: String): List<Ejercicio> = todos.filter { it.grupoMuscular == grupo }
}
