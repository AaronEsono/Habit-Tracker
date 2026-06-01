# Module: Firestore Remote Cloud Storage

This module abstracts and manages all cloud-based database operations using **Firebase Firestore**. It provides the application with a scalable, NoSQL document-oriented infrastructure to backup, sync, and delete tracking information securely in the cloud.

---

## 🏗️ Architecture & Core Concepts

The module is engineered around **Clean Architecture principles**, enforcing a strict separation between remote infrastructure frameworks and upstream business layers:

### 1. Reactive Execution Streams (`Flow`)
Unlike traditional rigid callback architectures, every operation exposed by this module streams its operational execution timeline natively through cold `Flow` pipelines. This enables the UI layer (Jetpack Compose) to effortlessly bind state wrappers to UI components, driving automated, state-driven interface updates (e.g., showing a progress spinner during network roundtrips).

### 2. Sequential Async Transformation (`.await()`)
To guarantee maximum read/write stability and eliminate callback-nesting traps, this module drops obsolete asynchronous task listeners in favor of the `kotlinx-coroutines-play-services` framework extension.
By utilizing the `.await()` operator, typical Firebase multi-threaded callbacks are transformed directly into linear, non-blocking suspension points. This ensures that traditional Kotlin `try-catch` blocks perform flawlessly—reliably intercepting actual network dropouts, transaction timeouts, or permission constraints the exact millisecond they occur.

### 3. Unified State Machine (`AuthResponseFirestore`)
Data flow sequences do not emit raw models or technical crash logs directly to the core application. Instead, all transactions are wrapped into a specialized finite state topology:
* **`Loading`:** Broadcasts background network activity to toggle UI skeleton frameworks or block accidental duplicate touch gestures.
* **`Success`:** Encapsulates the safely deserialized `FirestoreData` reflection model (or `null` for write/delete confirmations).
* **`Error`:** Delivers an abstracted Android resource identifier (`R.string.*`) mapped directly from the incoming exception.

---

## 🛡️ Telemetry & Resilience Features

* **Integrated Exception Mapping:** Features an independent exception evaluation matrix (`treatError`) that catches specialized `FirebaseFirestoreException.Code` structures, insulating the presentation layer from raw, unreadable backend system logs.
* **Automated Telemetry Funnels:** The core database operations orchestrator directly connects to the application's analytic framework interface. It seamlessly triggers localized event tracking logs on every fetch, push, or deletion sequence—alongside structural error telemetry hooks—to monitor global system reliability without leaking operational data.
* **Serialization Compliance:** The core cloud data transfer objects (DTOs) enforce strict default argument configurations to guarantee implicit, no-argument constructors, satisfying the Firebase reflection engine requirements out of the box.

---

## 📁 Package Architecture

The root package `aeb.proyecto.firestore` is structured in a decoupled and modular layout:

```text
aeb.proyecto.firestore
│
├── 📂 di                        # Dependency Injection Modules (Hilt)
│   ├── FirestoreModule.kt       # Provides the singleton instance of the remote FirebaseFirestore gateway
│   └── FirestoreRepositoryModule.kt # Binds the abstract FirestoreInterface contract to the manager implementation
│
├── 📂 errors                    # Centralized Failure Handlers
│   └── TreatErrors.kt           # Matrix mapping Firestore execution codes to application string resources
│
├── 📂 model                     # Serialization Schemas
│   └── FirestoreData.kt         # Data DTO compliant with reflection mechanics (No-argument constructors)
│
├── FirestoreInterface.kt        # Formal contract boundary defining available cloud actions (Sealed Responses)
└── FirestoreManager.kt          # Infrastructure engine executing sequential suspension pipelines