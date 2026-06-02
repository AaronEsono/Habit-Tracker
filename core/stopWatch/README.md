# Module: Stopwatch service

## Overview
This module provides a robust, high-precision time-tracking infrastructure designed to operate seamlessly in the background and through an interactive system overlay. Built to adhere to modern Android execution constraints, the architecture guarantees chronological accuracy down to the millisecond while maintaining a strict, minimalist, and high-contrast monochromatic visual footprint.

The core engine is capable of driving three distinct instrumentation modalities:
* **Progressive Stopwatch:** Linear forward-counting chronological tracking.
* **Countdown Timer:** Regressive time-box monitoring with absolute terminal boundaries.
* **Interval Matrix:** Dual-state structural loops (Work/Rest segments) with dynamic repetition counters.

---

## Architectural Layout & Core Components

The module relies on an atomic, state-driven architecture to decouple low-level Android operating system components from the declarative UI rendering layer.

```text
+-------------------------------------------------------------+
|                  System-Level OS Hardware                   |
|      (SystemClock, PowerManager, Audio, Vibrator, WM)       |
+------------------------------+------------------------------+
|
v
+-------------------------------------------------------------+
|                      StopWatchService                       |
|  (Foreground Lifecycle, Component Cleanup, WakeLock Guard)   |
+------------------------------+------------------------------+
|
Updates State         |         Exposes Streams
Asynchronously        v         via StateFlow
+-------------------------------------------------------------+
|                    StopWatchStateManager                    |
|              (Single Source of Truth / SSOT)                |
+------------------------------+------------------------------+
|
v
+-------------------------------------------------------------+
|                     Jetpack Compose UI                      |
|         (System Overlay Canvas & Notification Shell)        |
+-------------------------------------------------------------+
```

### 1. The Single Source of Truth (`StopWatchStateManager`)
An externalized, reactive state manager that governs the entire runtime lifecycle. By emitting immutable state variables through asynchronous streams (`StateFlow`), it ensures that the background service notifications, the system overlay layout, and the main application UI stay perfectly synchronized without race conditions.

### 2. The Execution Core (`StopWatchService`)
A prioritized, long-running background component that serves as the hardware-orchestration layer. It intercepts Android system broadcast signals, handles hardware audio/haptic routing, and maps lifecycle events to prevent the operating system from reclaiming resources during active tracking sessions.

### 3. The Visual Presentation Layer (`Compose Overlay`)
A detached `ComposeView` injected directly into the platform's window hierarchy via low-level system services. It translates user gesture deltas into real-time coordinate transformations while implementing memory-efficient rendering loops optimized for constant redrawing.

---

## Technical Implementations & Hardware Guarantees

### Chronological Precision Guardrails
Traditional software loops like `java.util.Timer` or Coroutine `delay()` windows suffer from accumulation drifting caused by CPU scheduling delays. To bypass this limitation, this module anchors all mathematical calculations onto raw hardware uptime:

* **`SystemClock.elapsedRealtime()`:** Used as an absolute reference anchor. This clock ticks monotonically and includes deep sleep states, ensuring that tracking metrics remain precise even if the host processor enters low-power optimization postures.
* **Delta Accumulation Pool:** Upon entering a paused state, runtime fractions are calculated as:
  $$\Delta t = t_{\text{current}} - t_{\text{start}}$$
  This delta slice is accumulated directly into a cumulative pool variable (`timeElapsedBeforePause`), safeguarding historical time investment down to the millisecond across infinite suspension cycles.

### Power & Resource Defense
To operate reliably as a background mechanism under aggressive battery-saving protocols introduced in modern Android APIs, the module implements layered defensive hooks:

* **Non-Reference Counted `PARTIAL_WAKE_LOCK`:** Explicitly claims a CPU execution token from the `PowerManager`. Disabling reference counting guarantees that a single explicit detachment directive completely releases the hardware lock, neutralizing background power leaks.
* **Foreground Elevation Requirements:** Automatically requests high-priority elevation within the required platform timeout window, binding a persistent notification shell to neutralize sudden process termination by the operating system’s Out-Of-Memory (OOM) killer.

### Low-Level Window Orchestration
The floating desktop overlay interfaces directly with the Android `WindowManager` utilizing specialized configuration parameters:
* **`TYPE_APPLICATION_OVERLAY`:** Complies with modern security protocols to prevent tapjacking vectors while rendering layouts on top of third-party software surfaces.
* **`FLAG_NOT_FOCUSABLE`:** Isolates input parameters so that touch events are only intercepted within the physical perimeter of the overlay canvas. Global touch input and keyboard focus outside the overlay pass unobstructed to the background applications.

---

## State Machine Execution Matrix

The internal engine cycles through a deterministic set of behavioral states, mapping distinct UI components, audio/haptic alerts, and database synchronization pipelines based on the active tracking profile.

| Operational State | Notification Controls | Haptic/Audio Feedback | Persistence Action |
| :--- | :--- | :--- | :--- |
| **`Idle`** | None (Service Uninitialized) | Disabled | None |
| **`InProgress`** | Pause, Cancel | Disabled | Real-time `DataStore` ticking cache |
| **`Stopped`** | Resume, Cancel | Disabled | Immediate metric delta caching |
| **`Finished`** | Finish (Saves log) | High-Priority Alarm + Haptic Pulse | Complete asynchronous flush to Room DB (`TimeEntry`) |

---

## Monochromatic UI & Design Semantics

Adhering to a strict minimalist aesthetic, the graphic layer uses high-contrast, structural, and custom-tailored elements to maximize daylight legibility without visual bloat:

* **Dynamic Identity Matching:** The linear progress bar (`PercentageBar`) computes completion fractions dynamically using a mathematical `coerceIn(0f, 1f)` layout shield. If the active session is explicitly linked to an entity within the local database, the track dynamically extracts the token's hexadecimal value to color-match the progress indicator.
* **Symmetrical Suspension Aesthetics:** Important universal states use static shared color identifiers (such as a normalized `pausedBarColor` gray) across both light and dark configurations to represent a system-wide "freeze" state uniformly.
* **Fluid Framerate Interpolation:** Because time ticks occur at discrete intervals to optimize processing resources, visual transitions utilize custom linear easing animations (`animateFloatAsState`) to interpolate rendering frame steps smoothly at maximum device framerates (60Hz / 120Hz), bypassing visual stutter entirely.

```text
aeb.proyecto.stopwatch/
│
├── constants/
│   └── Constants.kt
│
├── di/
│   ├── ContextModule.kt
│   ├── NotificationModule.kt
│   ├── StopWatchHelperModule.kt
│   └── VibratorModule.kt
│
├── manager/
│   └── StopWatchStateManager.kt
│
├── model/
│   ├── NotificationInfo.kt
│   └── OverlayColors.kt          <-- Movido aquí (es un modelo/data class de configuración de UI)
│
├── notification/
│   └── NotificationBuilderHelper.kt
│
├── overlay/
│   ├── OverlayContent.kt         <-- El composable raíz expuesto
│   ├── OverlayLayoutParams.kt    <-- Refactorizado de OverlayParams.kt
│   └── components/               <-- Unificamos los componentes gráficos aquí
│       ├── OverlayButton.kt
│       └── PercentageBar.kt      <-- Corrección tipográfica (sin la "j")
│
├── service/
│   └── StopWatchService.kt
│
└── utils/                        <-- Centralizado. Una única carpeta de helpers para todo el módulo
├── PercentageUtils.kt        <-- Operaciones matemáticas del temporizador
├── TitleUtils.kt             <-- Generador de strings dinámicos (notificación + overlay)
└── Utils.kt                  <-- Extensiones genéricas del tiempo