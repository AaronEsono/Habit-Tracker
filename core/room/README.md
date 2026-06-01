# Habit-Tracker 🚀

A modern, native, high-performance habit-tracking engine for the Android ecosystem. This application is engineered under the principles of **Clean Architecture**, extreme reactivity, and robust atomic local persistence, built to scale and synchronize seamlessly with cloud infrastructures.

---

## 🛠️ Tech Stack & Infrastructure

The core of **Habit-Tracker** is built upon the most powerful tools and libraries in modern Android development:

* **Language:** 100% Kotlin featuring Coroutines and distributed asynchronous data streams (`Flow`, `SharedFlow`, `StateFlow`).
* **User Interface:** Jetpack Compose (Declarative, modular, and state-driven UI).
* **Dependency Injection:** Hilt / Dagger (Clean dependency graphs with tightly scoped lifecycles).
* **Local Database:** Room Database (ORM abstraction over SQLite with native transactional support).
* **Serialization:** Gson (Customized via polymorphic type adapters).
* **Synchronization:** Designed with a hybrid compression engine (GZIP + Base64) optimized for massive single-transaction writes into **Cloud Firestore**.

---

## 📐 Persistence Architecture Decisions

The local storage layer goes beyond simple table mapping; it implements advanced design patterns to guarantee background performance and strict data consistency:

### ⚡ Atomic Transactions (ACID Safety)
Every complex relational mutation (such as batch notification injections or master cloud synchronization overwrites) is executed inside SQLite `@Transaction` wrappers. This guarantees an *all-or-nothing* behavior, safeguarding the database against partial states or corruption if a failure occurs mid-loop.

### 🧩 Hybrid Serialization & Polymorphism
To overcome **Type Erasure** issues when persisting `sealed class` hierarchies into relational database structures, we implemented two surgical approaches:
1.  **Manual Prefix Discriminators:** Utilized in alerts, enabling fast query indexation without penalizing runtime read performance.
2.  **Custom Type Adapters (Gson):** Bespoke interceptors for complex polymorphic logical trees (such as habit cadences), keeping the resulting JSON string flat, readable, and highly optimized.

### 🛡️ Advanced Production: Minification Rules (R8/ProGuard)
Since dynamic asset mapping relies on **Java Reflection (`Class.forName`)** to eliminate endless boilerplate lookup tables, the system embeds preventive configuration rules inside `proguard-rules.pro`. This strictly prohibits R8 from stripping or renaming Material Icons vector signatures during production release builds.

---

## 🌟 Key Data Engine Features

* **Reactive Range Queries:** Time-bound stream filtering (`LocalDate`) optimized via scalar conversion into Unix epoch milliseconds.
* **Advanced Stream Combiners (`combine`):** Drastic reduction of UI-layer overhead by merging and shaping multiple independent reactive flows into a single unified pipeline within the repository layer.
* **"Wipe & Re-insert" Strategy:** Efficient synchronization of `1:N` relational sub-graphs without leaving orphaned records or triggering primary key collisions.
* **Defensive Initialization:** Anti-crash null safety achieved by dynamically injecting baseline progress objects (`BigDecimal(0)`) into daily tracking cells that haven't been initialized by the user yet.

## 📁 Project Architecture & Folder Structure

The persistence and data infrastructure layers are strictly decoupled into single-responsibility packages. Below is the structural architectural breakdown of the components:

```text
├── 📂 converters       # Room TypeConverters transforming complex data types into SQLite scalars
├── 📂 dao              # Data Access Objects orchestrating atomic and reactive queries
├── 📂 database         # Central Room Database configuration and migration hub
├── 📂 di               # Dependency Injection modules (Hilt/Dagger) provisioning database instances
├── 📂 dto              # Data Transfer Objects specialized for Firestore cloud synchronization network payloads
├── 📂 entities         # Database schema definitions and relational aggregate entities
│   └── 📂 relations    # Intermediate Room junction models handling 1:N database tables bridges
├── 📂 model            # Rich Domain presentation models, polymorphic sealed classes, and UI projections
│   └── 📂 classes      # Structural cadence blueprints (Habit Types, Notification Cadences)
├── 📂 repository       # Domain-facing repositories mapping data models and combining reactive streams
└── 📂 utils            # Low-level compression utility tools and mathematical formatting helpers