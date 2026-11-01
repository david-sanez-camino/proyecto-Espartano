# ESPARTANO — Arquitectura y estructura del proyecto

## Diagrama de arquitectura (MVVM + Clean)

```
┌─────────────────────────────────────────────────────────────┐
│                        UI LAYER                             │
│                                                             │
│  ┌─────────┐ ┌──────────┐ ┌─────────┐ ┌──────┐ ┌────────┐ │
│  │Dashboard│ │NuevaMed. │ │Historial│ │Gráf. │ │Ajustes │ │
│  │Screen   │ │Screen    │ │Screen   │ │Screen│ │Screen  │ │
│  └────┬────┘ └────┬─────┘ └────┬────┘ └──┬───┘ └───┬────┘ │
│       │           │            │          │         │       │
│  ┌────▼────┐ ┌────▼─────┐ ┌───▼────┐ ┌──▼───┐ ┌───▼────┐ │
│  │Dashboard│ │NuevaMed. │ │Histor. │ │Gráf. │ │Ajustes │ │
│  │ViewModel│ │ViewModel │ │ViewMod.│ │ViewM.│ │ViewMod.│ │
│  └────┬────┘ └────┬─────┘ └───┬────┘ └──┬───┘ └───┬────┘ │
└───────┼───────────┼───────────┼──────────┼─────────┼───────┘
        │           │           │          │         │
┌───────┼───────────┼───────────┼──────────┼─────────┼───────┐
│       │      DOMAIN LAYER     │          │         │        │
│  ┌────▼──────────────────────▼──────────▼─────────▼──────┐ │
│  │  MensajeMotivadorUseCase  │  ExportarDatosUseCase     │ │
│  └───────────────────────────┬───────────────────────────┘ │
│  ┌────────────────────────┐  │                              │
│  │  Models: Perfil        │  │                              │
│  │         Medicion       │  │                              │
│  └────────────────────────┘  │                              │
└──────────────────────────────┼──────────────────────────────┘
                               │
┌──────────────────────────────┼──────────────────────────────┐
│              DATA LAYER      │                               │
│  ┌──────────────────┐  ┌────▼─────────────────┐            │
│  │ PerfilRepository │  │ MedicionRepository    │            │
│  └────────┬─────────┘  └──────────┬────────────┘            │
│           │                       │                          │
│  ┌────────▼─────────┐  ┌──────────▼────────────┐            │
│  │  PerfilDao        │  │  MedicionDao           │            │
│  └────────┬─────────┘  └──────────┬────────────┘            │
│           │                       │                          │
│  ┌────────▼───────────────────────▼────────────┐            │
│  │          EspartanoDatabase (Room/SQLite)     │            │
│  │   tabla: perfil  │  tabla: mediciones        │            │
│  └─────────────────────────────────────────────┘            │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│                   NOTIFICATIONS                              │
│   NotificationScheduler → WorkManager → ReminderWorker       │
└──────────────────────────────────────────────────────────────┘
```

---

## Diagrama de navegación

```
                    ┌──────────────────────────────┐
                    │    Bottom Navigation Bar      │
                    │  Inicio│Historial│Gráf│Metas│Aj.│
                    └───┬───────┬────────┬────┬────┬──┘
                        │       │        │    │    │
              ┌─────────▼─┐ ┌───▼──┐ ┌──▼─┐ │ ┌──▼────┐
              │ Dashboard │ │Histo.│ │Graf│ │ │Ajustes│
              │  Screen   │ │Screen│ │Scr.│ │ │Screen │
              └─────┬─────┘ └──────┘ └────┘ │ └───────┘
                    │ FAB (+)                │
              ┌─────▼──────────┐    ┌────────▼──┐
              │ NuevaMedicion  │    │   Metas   │
              │    Screen      │    │   Screen  │
              └────────────────┘    └───────────┘
```

---

## Estructura de archivos

```
espartano/
├── settings.gradle.kts               Módulos del proyecto
├── build.gradle.kts                  Plugins raíz
├── gradle.properties                 Propiedades JVM y AndroidX
├── gradle/
│   └── libs.versions.toml            Version catalog (deps centralizadas)
│
└── app/
    ├── build.gradle.kts              Dependencias y configuración del módulo
    ├── proguard-rules.pro            Reglas ProGuard para release
    └── src/main/
        ├── AndroidManifest.xml       Permisos, actividad, FileProvider
        ├── res/
        │   ├── values/
        │   │   ├── strings.xml       Nombre de la app
        │   │   ├── colors.xml        Color de fondo del launcher
        │   │   └── themes.xml        Tema XML para el splash/sistema
        │   ├── drawable/
        │   │   └── ic_launcher_foreground.xml   Icono escudo + E dorado
        │   ├── mipmap-anydpi-v26/
        │   │   ├── ic_launcher.xml
        │   │   └── ic_launcher_round.xml
        │   └── xml/
        │       └── file_paths.xml    Rutas FileProvider para exportación
        │
        └── kotlin/com/espartano/app/
            │
            ├── EspartanoApp.kt       Application: Hilt + canal notificaciones
            ├── MainActivity.kt       Entry point: edge-to-edge + Compose
            │
            ├── data/
            │   ├── db/
            │   │   ├── EspartanoDatabase.kt     Room @Database (v1)
            │   │   ├── entity/
            │   │   │   ├── PerfilEntity.kt      Tabla perfil (id=1 siempre)
            │   │   │   └── MedicionEntity.kt    Tabla mediciones (PK autoincr)
            │   │   └── dao/
            │   │       ├── PerfilDao.kt         CRUD perfil + Flow
            │   │       └── MedicionDao.kt       CRUD mediciones + Flow + queries
            │   ├── repository/
            │   │   ├── PerfilRepository.kt      Wrapper del DAO, inyectable
            │   │   └── MedicionRepository.kt    Wrapper del DAO, inyectable
            │   └── di/
            │       └── DatabaseModule.kt        Hilt: provee DB, DAOs
            │
            ├── domain/
            │   ├── model/
            │   │   ├── Perfil.kt    Data class dominio + mappers to/from Entity
            │   │   └── Medicion.kt  Data class dominio + imc() + mappers
            │   └── usecase/
            │       ├── MensajeMotivadorUseCase.kt   Lógica frases (mejora/empeora/estanc.)
            │       └── ExportarDatosUseCase.kt       CSV y PDF con iTextPDF
            │
            ├── ui/
            │   ├── theme/
            │   │   ├── Color.kt     Paleta completa (Oro, Rojo, Verde, Azul…)
            │   │   ├── Type.kt      Tipografía Espartano (display→label)
            │   │   └── Theme.kt     EspartanoTheme (solo dark scheme)
            │   │
            │   ├── components/      Componentes reutilizables
            │   │   ├── MetricCard.kt       Card métrica con accentColor + delta
            │   │   ├── GoalProgressBar.kt  Barra progreso animada hacia meta
            │   │   ├── EspartanoTextField.kt  OutlinedTextField estilo Oro
            │   │   ├── SectionTitle.kt     Label uppercase dorado
            │   │   ├── SimpleLineChart.kt  Gráfico de línea con Canvas
            │   │   └── MotivadorDialog.kt  Dialog con mensaje del entrenador
            │   │
            │   ├── navigation/
            │   │   ├── Screen.kt           Rutas selladas de navegación
            │   │   └── EspartanoNavHost.kt Scaffold + BottomBar + NavHost
            │   │
            │   ├── dashboard/
            │   │   ├── DashboardScreen.kt  Home: métricas, mini-chart, progreso metas
            │   │   └── DashboardViewModel.kt  StateFlow: perfil + mediciones combinados
            │   │
            │   ├── medicion/
            │   │   ├── NuevaMedicionScreen.kt  Formulario peso/grasa/músculo
            │   │   └── NuevaMedicionViewModel.kt  Validación + guardar + mensaje motivador
            │   │
            │   ├── historial/
            │   │   ├── HistorialScreen.kt  LazyColumn con delete por fila
            │   │   └── HistorialViewModel.kt  Flow de todas las mediciones + IMC
            │   │
            │   ├── graficos/
            │   │   ├── GraficosScreen.kt  FilterChips métrica/rango + SimpleLineChart
            │   │   └── GraficosViewModel.kt  Filtrado por rango + valores por métrica
            │   │
            │   ├── metas/
            │   │   ├── MetasScreen.kt     Inputs objetivos + GoalProgressBar
            │   │   └── MetasViewModel.kt  Guarda en perfil, calcula progreso
            │   │
            │   └── ajustes/
            │       ├── AjustesScreen.kt   Perfil, slider recordatorio, exportar
            │       └── AjustesViewModel.kt  Exportar CSV/PDF, guardar perfil, notif.
            │
            └── notifications/
                ├── ReminderWorker.kt        HiltWorker: envía notificación local
                └── NotificationScheduler.kt  WorkManager: programa trabajo periódico
```

---

## Modelo de datos

```
┌──────────────────────────────┐    ┌─────────────────────────────────┐
│         perfil (Room)        │    │       mediciones (Room)          │
├──────────────────────────────┤    ├─────────────────────────────────┤
│ id: Int = 1 (único)          │    │ id: Long (PK autoincrement)      │
│ altura_cm: Float             │    │ fecha: Long (epoch ms)           │
│ fecha_nacimiento: Long       │    │ peso_kg: Float                   │
│ peso_objetivo_kg: Float?     │    │ grasa_pct: Float                 │
│ grasa_objetivo_pct: Float?   │    │ musculo_pct: Float               │
│ frecuencia_recordatorio_dias │    │                                  │
│   : Int (default 7)          │    │ imc → calculado al vuelo         │
└──────────────────────────────┘    └─────────────────────────────────┘
```

---

## Paleta de colores

| Token | Hex | Uso |
|---|---|---|
| `Oro` | `#C9A84C` | Primary, accent principal |
| `OroClaro` | `#E8C97A` | Hover/secondary |
| `FondoOscuro` | `#0A0A0A` | Background, TopAppBar |
| `SuperficieOscura` | `#1A1A1A` | Surface, BottomBar |
| `SuperficieMedia` | `#252525` | Cards, TextFields |
| `Verde` | `#4CAF7D` | Músculo, mejora |
| `Naranja` | `#CF844C` | Grasa corporal |
| `Azul` | `#4C9FCF` | IMC |
| `Rojo` | `#CF6679` | Error, empeora |

---

## Flujo principal

```
Usuario abre app
      │
      ▼
DashboardScreen
  (últimos valores + mini-chart + progreso metas)
      │
      ├─── FAB (+) ──► NuevaMedicionScreen
      │                  │ guarda Medicion en Room
      │                  │ compara con anterior
      │                  └─► MotivadorDialog (mejora/empeora/estanc.)
      │                        └─► vuelve al Dashboard
      │
      ├─── Historial ──► LazyColumn de mediciones (eliminable)
      │
      ├─── Gráficos ──► FilterChip métrica + rango + SimpleLineChart
      │
      ├─── Metas ──► Inputs objetivo + GoalProgressBar animada
      │
      └─── Ajustes ──► Perfil (altura/nacimiento)
                       Slider frecuencia recordatorio → WorkManager
                       Exportar CSV / PDF (FileProvider + Intent.ACTION_SEND)
```

---

## Decisiones técnicas clave

| Decisión | Razón |
|---|---|
| IMC calculado al vuelo (no guardado en DB) | Evita inconsistencias si se edita la altura del perfil |
| `StateFlow` + `combine()` en todos los ViewModels | Un solo punto de verdad; la UI nunca desincroniza |
| `PeriodicWorkRequestBuilder` (WorkManager) | Sobrevive reinicios del dispositivo; correcto para recordatorios periódicos |
| `SimpleLineChart` con Canvas nativo | Sin dependencia externa de gráficos; ligero y coherente con el tema oscuro |
| `FileProvider` para exportación | Requisito Android 7+ para compartir archivos entre apps |
| Hilt para DI | Reduce boilerplate de ViewModel factories y simplifica las pruebas |
| minSdk 34 | Según spec del proyecto (Android 14) |
