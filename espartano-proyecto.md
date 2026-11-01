# ESPARTANO — Documento de referencia del proyecto

App Android nativa y personal para registrar peso, % de grasa corporal y % de masa muscular, con seguimiento de altura y edad, gráficos de progreso, mensajes motivadores y metas.

*Última actualización: 28/08/2026*

---

## 1. Objetivo general

Tener una aplicación Android sencilla, 100% privada y sin conexión a internet, que permita registrar de forma rápida las mediciones corporales periódicas (peso, % grasa, % músculo), visualizar la evolución en el tiempo mediante gráficos, y recibir motivación basada en el propio progreso.

## 2. Objetivos específicos

1. Permitir registrar peso, % de grasa corporal y % de masa muscular en cada medición, con fecha automática.
2. Guardar datos de perfil una sola vez (altura, fecha de nacimiento) y calcular automáticamente edad e IMC.
3. Visualizar la evolución histórica de peso, % grasa, % músculo e IMC mediante gráficos.
4. Mostrar un mensaje motivador (tono "entrenador exigente") cada vez que se registra una nueva medición, comparándola con la anterior.
5. Permitir definir un peso objetivo y un % de grasa objetivo, y visualizar el progreso hacia esa meta.
6. Recordar al usuario registrar sus mediciones mediante notificaciones con frecuencia configurable.
7. Permitir exportar el histórico completo de datos en CSV y en PDF.
8. Garantizar que todos los datos se almacenan localmente en el dispositivo, sin backend, sin login ni sincronización en la nube.

## 3. Alcance funcional (v1 — MVP ampliado)

| # | Funcionalidad | Detalle |
|---|---|---|
| 1 | Registro de medición | Peso (kg), % grasa, % músculo + fecha automática |
| 2 | Perfil del usuario | Altura (cm), fecha de nacimiento (edad auto-calculada) |
| 3 | Cálculo automático | IMC = peso / altura² |
| 4 | Gráficos de progreso | Peso, % grasa, % músculo, IMC — evolución temporal |
| 5 | Mensajes motivadores | Tono "entrenador exigente", según mejora/empeora/estancamiento |
| 6 | Metas | Peso objetivo y % grasa objetivo, con indicador de progreso |
| 7 | Recordatorios | Notificaciones locales, frecuencia configurable por el usuario |
| 8 | Exportación | CSV y PDF del histórico completo |
| 9 | Diseño | Modo oscuro, unidades métricas, sin login ni PIN |

**Fuera de alcance en v1:** iOS, backend/nube, multiusuario, integración con básculas inteligentes (Bluetooth), autenticación/bloqueo de app.

## 4. Modelo de datos (borrador)

**Tabla `perfil`** (un único registro)
- `altura_cm`: Float
- `fecha_nacimiento`: Date
- `peso_objetivo_kg`: Float (nullable)
- `grasa_objetivo_pct`: Float (nullable)
- `frecuencia_recordatorio`: enum/int (días)

**Tabla `mediciones`**
- `id`: Long (PK, autoincremental)
- `fecha`: Date
- `peso_kg`: Float
- `grasa_pct`: Float
- `musculo_pct`: Float
- `imc_calculado`: Float (derivado, se puede calcular al vuelo en vez de guardar)

## 5. Arquitectura técnica

- **Lenguaje/UI:** Kotlin + Jetpack Compose
- **Persistencia:** Room (SQLite local), sin red
- **Notificaciones:** WorkManager + AlarmManager para recordatorios periódicos
- **Gráficos:** librería Compose nativa de gráficos (ej. Vico)
- **Exportación:** `PdfDocument` (Android nativo) para PDF; escritura de texto plano para CSV
- **Arquitectura de app:** MVVM (ViewModel + StateFlow/Compose State), repositorio sobre Room
- **minSdk:** 34 (Android 14, build UP1A.231005.007)
- **Distribución:** APK firmado, instalación manual (sin Play Store)
- **Sin autenticación:** acceso directo, sin PIN ni biometría

## 6. Pantallas previstas

1. **Dashboard / Inicio** — últimos valores registrados, mini-gráficos, progreso hacia metas
2. **Nueva medición** — formulario rápido (peso, % grasa, % músculo) → guarda y muestra mensaje motivador
3. **Historial** — lista de mediciones pasadas, editable/eliminable
4. **Gráficos** — vista detallada por métrica (peso / grasa / músculo / IMC), selector de rango de fechas
5. **Metas** — definir/editar peso objetivo y % grasa objetivo
6. **Ajustes** — perfil (altura, fecha nacimiento), frecuencia de recordatorios, exportar datos (CSV/PDF), tema

## 7. Mensajes motivadores — lógica

Al guardar una nueva medición se compara con la anterior:
- **Mejora** (↓ % grasa o ↑ % músculo, según el objetivo): mensaje motivador de refuerzo, tono exigente pero positivo.
- **Empeora** (↑ % grasa o ↓ % músculo): mensaje directo que empuja a corregir, sin ser desmotivador.
- **Estancamiento** (sin cambios significativos): mensaje que incentiva constancia.

Se necesita un set de ~5-8 frases por categoría para que no se repita siempre el mismo mensaje.

## 8. Plan de desarrollo (cómo se hará)

**Fase 0 — Preparación**
- Confirmar stack (Kotlin + Compose + Room) y crear proyecto base en Android Studio.
- Configurar minSdk 34, estructura de paquetes (data / domain / ui), tema oscuro base.

**Fase 1 — Datos y lógica core**
- Modelo de datos con Room (perfil + mediciones).
- Repositorio y ViewModels.
- Cálculo automático de edad e IMC.

**Fase 2 — Pantallas principales**
- Formulario de nueva medición.
- Historial de mediciones.
- Dashboard con últimos valores.

**Fase 3 — Gráficos**
- Integración de librería de gráficos.
- Vistas de evolución por métrica con selector de rango.

**Fase 4 — Motivación y metas**
- Lógica de comparación entre mediciones.
- Set de mensajes motivadores (tono entrenador exigente).
- Pantalla de metas con indicador de progreso.

**Fase 5 — Recordatorios y exportación**
- Notificaciones locales configurables (WorkManager).
- Exportación a CSV y PDF.

**Fase 6 — Pulido y entrega**
- Revisión de UI/UX en modo oscuro.
- Icono y nombre "ESPARTANO".
- Generación de APK firmado para instalación manual.
- Pruebas en dispositivo real.

## 9. Pendientes / decisiones abiertas

- Redactar el set completo de frases motivadoras (tono "entrenador exigente").
- Diseñar el icono/identidad visual de ESPARTANO.
- Definir el rango de fechas por defecto en los gráficos (últimos 30 días / 3 meses / todo).
- Decidir si el IMC se guarda en base de datos o se calcula siempre al vuelo (recomendado: al vuelo, para evitar inconsistencias si se edita el perfil).

## 10. Fuera de alcance por ahora (posibles futuras mejoras)

- Integración con básculas inteligentes vía Bluetooth.
- Copia de seguridad en la nube / multi-dispositivo.
- Versión iOS.
- Widget de pantalla de inicio con el último dato registrado.

---
*Este documento resume todas las decisiones tomadas hasta ahora sobre el proyecto ESPARTANO y sirve como referencia para retomar el desarrollo en cualquier momento.*
