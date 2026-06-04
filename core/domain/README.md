# Architecture Blueprint: Clean Domain Core

A high-performance, reactive, and framework-agnostic **Domain Layer** governing the core business rules of a modularized Android Habit-Tracker application. Built on **Clean Architecture** principles, this module serves as the pure Kotlin processing engine of the system, isolating critical enterprise rules from infrastructure components, UI toolkits, and external persistence frameworks.

---

## 🏗️ Architectural Core Principles

The architecture strictly enforces a **one-way dependency flow**, ensuring that the business logic remains uncoupled from databases (Room), network boundaries (Cloud Firestore), preference storage (DataStore), and background triggers (`AlarmManager`).

* **Pure Kotlin Engine:** Zero dependencies on Android framework context components, ensuring effortless unit testing configurations.
* **Reactive Data Highways:** Built entirely on top of continuous asynchronous `Kotlin Coroutines Flow` pipelines to guarantee real-time UI synchronizations.
* **Defensive Boundary Architecture:** Complete encapsulations of data transactions inside `runCatching` safe shields to prevent edge-case runtime crashes.
* **Deterministic Financial Precision:** Rejection of floating-point inaccuracies over logging metrics by executing mathematical calculations via `BigDecimal` transformations.

---

## 🧠 Core Features & Use Case Matrix

The module orchestrates the application's lifecycles across distinct feature boundaries. Each boundary houses granular, single-responsibility Use Cases:

### 1. Habit Management (`addHabit` & `habits`)
Manages structural mutations, daily tracking nodes, and contextual time-frame queries.
* **Intelligent Upsert Pipelines:** Evaluates the persistence layers to automatically choose between executing safe mathematical delta progressions or generating fresh logging tokens.
* **Hardware Alarm Synchronization:** Intercepts habit creation rules to calculate future reminder intervals and registers precise trigger hooks directly inside the OS system kernel.

### 2. Standalone & Linked Timer Engine (`timer` & `main`)
Drives the application’s core countdown and execution mechanics.
* **State Flattening Combination:** Fuses fragmented preference tracks (IDs, date structures, intervals, rest periods, sets) from separate storage vectors into a single, cohesive `TimerData` stream.
* **Reactive Interceptors:** Employs advanced coroutine transformations (`flatMapLatest`) to instantly evict or mount completion overlay dialog states without memory leaks or phantom threads.

### 3. Analytics & Time-Series Projections (`statistics`)
Hydrates historical charts and performance dashboards.
* **Variable Granularity Extraction:** Exposes granular query windows optimized for fixed-interval renderers (e.g., Vico Charts) by delegating range calculations to indexed SQL matrices, preventing local memory bloat.
* **Context-Preserving Filters:** Streams active viewport selections reactively while natively supporting nullability states to evaluate consolidated global statistics.

### 4. Enterprise Identity & Session Guard (`login` & `settings`)
Enforces authorization rules and security checkpoints.
* **Polymorphic Status Evaluations:** Processes incoming identity streams through clean type checking (`is AuthResponseAuthentication.Success`) to feed real-time structural menu mutations without exposing cryptographic footprints downstream.

### 5. Automated Cloud Ledger & Restoration (`save`)
Controls global backup protocols and data synchronization loops.
* **Circular Hydration Flow:** Coordinates mass data injection across three distinct layers:
    1. Pulls consolidated NoSQL documents down from remote clouds.
    2. Overwrites local database sheets and extracts embedded reminder schemes.
    3. Re-binds precise system alarms back into the device hardware automatically.

---

## 🛠️ Reactive Strategy Breakdown

The domain model leverages reactive operators to bridge the gap between persistent preference structures and real-time presentation requirements. Below is the functional data lifecycle of the core timer layout compilation:


By consolidating fragmented records inside the domain boundary, downstream ViewModels consume an immutable, predictable layout state token, achieving a pure unidirectional data flow (UDF).

---

```text
[Datastore Preference Fields]
├── idHabitLinkedTimer --------┐
├── dateHabitLinkedTimer ------┼─> combine() ──> [Safe DB Lookup via Room]
├── typeTimerSelected ---------┤                       │
├── wheelHour / restHour ------┤                       ▼
└── numberSetsTimerSelected ---┘               [TimerData State Snapshot]
│
▼
Collected by UI Engine
```

## 🚀 Testing Readiness

Due to the absolute isolation of this module, the entire business directory is **100% unit-testable** without requiring Robolectric, Android instrumentation runner environments, or live database instances. Every infrastructure interface dependency is injected via `javax.inject.Inject` (Hilt/Dagger compatible), enabling immediate substitution with highly optimized fakes or mock frameworks during testing suites execution.

```text
aeb.proyecto.domain.usecase
│
├── 📂 addHabit
│   ├── DataStoreAddHabitUseCase.kt
│   ├── RoomRepositoryAddHabitUseCase.kt
│   └── SetNotificationAddHabitUseCase.kt
│
├── 📂 habit
│   ├── GetDailyHabitUseCase.kt
│   ├── GetHabitUseCase.kt
│   ├── GetTypesOfHabitUseCase.kt
│   ├── HabitDatastoreUseCase.kt
│   └── ManageHabitsUseCase.kt
│
├── 📂 login
│   ├── LoginAuthenticationUseCase.kt
│   └── SaveLoginCredentialUseCase.kt
│
├── 📂 main
│   ├── ManageDatastoreUseCase.kt
│   └── ManageDialogTimerUseCase.kt
│
├── 📂 save
│   ├── SaveAuthenticationUseCase.kt
│   ├── SaveFirestoreUseCase.kt
│   ├── SaveHabitsRepositoryUseCase.kt
│   └── SaveNotificationUseCase.kt
│
├── 📂 settings
│   ├── DataSettingsUseCase.kt
│   ├── SetLanguageUseCase.kt
│   └── SettingsAuthenticationUseCase.kt
│
├── 📂 statistics
│   ├── GetHabitSelectedUseCase.kt
│   └── GetHabitsStatisticsUseCase.kt
│
└── 📂 timer
├── GetHabitUseCase.kt
├── GetTimerDataUseCase.kt
├── TimeEntriesUseCase.kt
└── TimerDataStoreUseCase.kt