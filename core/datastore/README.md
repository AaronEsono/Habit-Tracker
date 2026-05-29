# Module: Datastore Persistence Layer

This module centralizes and manages local Key-Value data persistence using **Jetpack DataStore Preferences**. It is architected following **Clean Architecture** principles, ensuring non-blocking asynchronous data streams driven entirely by `Kotlin Coroutines` and `Flow`.

---

## 📁 Package Architecture

The root package `aeb.proyecto.datastore` is structured to separate concerns and maintain modularity:

* **📂 di (Dependency Injection):** Contains Hilt modules. `DatastoreModule.kt` provides the singleton instance of the physical core preferences file, while `DatastoreRepositoryModule.kt` binds the abstract interface to its target implementation.
* **📂 model (Data Classes):** Encapsulates immutable, bundled structures like `AppSettings` (UI/Theme variables), `LastSearched` (search filter histories), and `UserSession` (atomic authentication state) to prevent multi-threaded race conditions during mass updates.
* **📂 repository:** Hosts `DatastoreRepository.kt`, the structural mediator between infrastructure data mappings and core domain business requirements.
* **⚙️ Datastore.kt (DataStoreManager):** The core framework operator. Defines private `preferencesKey` parameters and shields downstream collectors using `.distinctUntilChanged()` to suppress redundant state emissions.
* **📜 DatastoreInterface.kt:** The formal domain contract boundary exposed to ViewModels and services, abstracting the internal serialization mechanisms.

---

## 🏗️ Architectural Flow

To ensure high testability and decoupling, the persistence layout establishes three distinct boundaries:

1.  **Infrastructure Layer (`Datastore.kt`):** Directly mutates the underlying preferences file. It handles atomic primitive writes independently (critical for preventing UI tearing on fast-scrolling UI elements like *Wheel Pickers*).
2.  **Domain Contract (`DatastoreInterface.kt`):** Defines what actions are available to the business layer without leaking how the parameters are locally recorded or encrypted.
3.  **Repository Bridge (`DatastoreRepository.kt`):** Evaluates and processes complex operations, such as utilizing the `combine` operator to merge separate hour, minute, and second preference flows into formatted `HH:mm:ss` display strings for the UI.

---

## ⚙️ Hilt Dependency Injection

Lifecycle management is orchestrated natively via Dependency Injection:
* `DatastoreModule` handles the file-system initialization, injecting the core DataStore instance as a `@Singleton`.
* `DatastoreRepositoryModule` exposes the implementation instance bound to the `DatastoreInterface` scope, decoupling execution layers seamlessly.

## 📁 Package Architecture

The root package `aeb.proyecto.datastore` is structured in a decoupled and modular layout:

```text
aeb.proyecto.datastore
│
├── 📂 di                        # Dependency Injection Modules (Hilt)
│   ├── DatastoreModule.kt       # Provides the singleton instance of the physical DataStore file
│   └── DatastoreRepositoryModule.kt # Binds the repository interface to its implementation
│
├── 📂 model                     # Immutable data structures (Grouped Data Classes)
│   ├── AppSettings.kt           # Global system configurations (Themes, UI, etc.)
│   ├── LastSearched.kt          # Search states and historical tracking filters
│   └── UserSession.kt           # Atomic session data (Email and Credentials)
│
├── 📂 repository                # Data abstraction layer
│   └── DatastoreRepository.kt   # Repository implementation acting as a domain bridge
│
├── Datastore.kt                 # DataStoreManager: Core infrastructure and primitive mappings
└── DatastoreInterface.kt        # Formal contract boundary exposed to the rest of the application