# Module: Language & Region Localization

This module encapsulates all application-level localization architecture, orchestrating both dynamic runtime language configurations and regional calendar adjustments based on the user's environmental context.

---

## 🏗️ Architecture & Core Concepts

The module decouples linguistic interface preferences from geographical metrics, resolving the common architectural pitfall of treating language and region as a single entity. It operates on three main pillars:

### 1. Per-App Language Preferences
Leveraging Android 13's native platforms, this module bypasses legacy context-wrapping hacks to update localized layouts in real time.
* **Modern Execution (Android 13+):** Utilizes the system-level `LocaleManager` service to dynamically stream language switches directly to the OS pipeline without enforcing hard activity recreations.
* **Backward Compatibility (Android 12 & below):** Intercepts legacy runtime layers seamlessly via `AppCompatDelegate`, maintaining identical state behaviors across all active API distributions.

### 2. Network-Driven Region Isolation
To automatically align calendar structures (such as pinpointing whether a habit tracking tracking week initiates on a Monday, Saturday, or Sunday), the module establishes a multi-tier localization evaluation:
* **Cellular Tower Triangulation (`TelephonyManager`):** Queries live network SIM operator ISO indicators to capture the user's instantaneous physical country boundaries. This ensures that an expatriate user reading the application in Spanish while residing in Peru or the US correctly receives a localized Sunday-start calendar grid.
* **Hardware OS Fallback:** If cellular telemetry drops (e.g., Airplane Mode, tablets, or unprovisioned emulators), the core parsing logic gracefully falls back onto the internal hardware configuration locale matrix.

### 3. Structural Mapping Models (`EnumLanguage` & Matrix Lookups)
Linguistic variants are cataloged natively within strict Type-Safe structures using Kotlin's high-efficiency `entries` enumeration fields. Concurrently, regional ISO-3166 codes feed into a lightweight lookup matrix that maps geographic entities straight to standard `java.time.DayOfWeek` initializers.

---

## 🛡️ Resilience & Design Safeguards

* **Index-OutOfBounds Prevention:** Includes empty-state validation guards when accessing system configuration locale structures, ensuring the application defaults to standard international baselines (`en` / `DayOfWeek.MONDAY`) if the host operating system returns an uninitialized locale tree.
* **Decoupled Contract Boundaries:** The presentation layer and upper data modules remain completely insulated from specific framework dependencies, interacting exclusively through the clean abstraction boundaries defined by `LanguageInterface`.

## 📁 Package Architecture

The root package `aeb.proyecto.language` is structured in a decoupled and modular layout:

```text
aeb.proyecto.language
│
├── 📂 di                        # Dependency Injection Modules (Hilt)
│   └── LanguageModule.kt        # Binds the abstract LanguageInterface contract to the manager implementation
│
├── 📂 model                     # Layout Domain Models
│   └── EnumLanguage.kt          # Type-safe linguistic registry bundling ISO codes and graphic flags
│
├── 📂 provider                  # Geographical Configuration Parsers
│   └── RegionFirstDayProvider.kt # Evaluates host network context to locate calendar boundaries
│
├── LanguageInterface.kt        # Formal contract boundary defining available locale query and shift operations
└── LanguageManager.kt          # Infrastructure engine managing system and compatibility locale frameworks
