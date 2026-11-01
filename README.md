# 🛡️ ESPARTANO

App Android nativa para registrar tu evolución física y tus entrenamientos de gimnasio. Es 100 % privada y funciona sin conexión: no tiene login ni nube, y todos los datos se quedan en el móvil.

## ✨ Funcionalidades

### Mediciones corporales
- Registro de **peso**, **% de grasa** y **% de masa muscular**, con la fecha puesta automáticamente.
- Cálculo automático de **IMC** y **edad** a partir de la altura y la fecha de nacimiento del perfil.
- **Historial** completo de mediciones, con opción de borrar registros.
- **Mensaje motivador** con tono de "entrenador exigente" al guardar cada medición, según mejores, empeores o te estanques.

### Gimnasio
- Creación de **rutinas** a partir de un catálogo de **90 ejercicios** agrupados por músculo: pecho, espalda, hombros, bíceps, tríceps, piernas, abdomen, cardio y olímpicos.
- **Entrenamiento activo** con cronómetro y registro **serie a serie** de peso y repeticiones.
- Cada ejercicio se rellena con el peso y las repeticiones de la **última vez** que lo hiciste.
- **Historial de sesiones**, con la duración de cada entrenamiento.

### Progreso y metas
- **Gráficos** de evolución de peso, % grasa, % músculo e IMC, con filtros de 30 días, 3 meses, 6 meses o todo.
- **Metas** de peso y % de grasa objetivo, con barra de progreso.
- **Dashboard** con los últimos valores, un mini-gráfico y el avance hacia tus metas.

### Otros
- **Recordatorios** con notificaciones locales cada X días (configurable).
- **Exportación** del historial a **CSV** y **PDF**.
- Diseño en **modo oscuro** con estética negra y dorada.

## 🛠️ Tecnologías

| Área | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | MVVM + capas *data / domain / ui* |
| Persistencia | Room (SQLite local) |
| Inyección de dependencias | Hilt |
| Navegación | Navigation Compose |
| Tareas en segundo plano | WorkManager |
| PDF | iTextPDF |
| Asincronía | Kotlin Coroutines + StateFlow |

## 📂 Estructura del proyecto

```
espartano/app/src/main/kotlin/com/espartano/app/
├── data/
│   ├── db/            # Room: base de datos, entidades, DAOs y relaciones
│   ├── repository/    # Repositorios (perfil, mediciones, rutinas, sesiones…)
│   └── di/            # Módulos de Hilt
├── domain/
│   ├── model/         # Modelos de dominio y catálogo de ejercicios
│   └── usecase/       # Mensajes motivadores y exportación CSV/PDF
├── ui/
│   ├── dashboard/     # Pantalla de inicio
│   ├── medicion/      # Nueva medición
│   ├── historial/     # Historial de mediciones
│   ├── gimnasio/      # Rutinas, entrenamiento activo e historial de sesiones
│   ├── graficos/      # Gráficos de evolución
│   ├── metas/         # Objetivos
│   ├── ajustes/       # Perfil, recordatorios y exportación
│   ├── components/    # Componentes reutilizables
│   ├── navigation/    # Rutas y barra de navegación inferior
│   └── theme/         # Colores, tipografía y tema
└── notifications/     # Recordatorios con WorkManager
```

Más detalle en [`ARCHITECTURE.md`](ARCHITECTURE.md) y [`espartano-proyecto.md`](espartano-proyecto.md).

## 🚀 Cómo ejecutarlo

### Requisitos
- Android Studio (reciente)
- JDK 17
- Dispositivo o emulador con **Android 14 o superior** (minSdk 34)

### Pasos
1. Clona el repositorio:
   ```bash
   git clone https://github.com/david-sanez-camino/proyecto-Espartano.git
   ```
2. Abre la carpeta **`espartano/`** en Android Studio. Ojo: es esa subcarpeta, no la raíz del repositorio.
3. Espera a que Gradle sincronice y pulsa ▶️ **Run**.

También puedes generar el APK desde la terminal:
```bash
cd espartano
./gradlew assembleDebug        # En Windows: gradlew.bat assembleDebug
```
El APK queda en `espartano/app/build/outputs/apk/debug/`.

## 🔒 Privacidad

ESPARTANO no usa internet. No tiene cuentas, ni servidores, ni analíticas. Toda la información se guarda en una base de datos local del dispositivo.

## 👤 Autor

**David Sanez Camino** — [@david-sanez-camino](https://github.com/david-sanez-camino)
