# :core:analytics Module

The `:core:analytics` module establishes a centralized, type-safe, and vendor-agnostic infrastructure for propagating runtime telemetry and product metrics across the application ecosystem. By isolating telemetry mechanics from the core presentation layers, the architecture guarantees that feature components remain entirely decoupled from third-party analytics SDK dependencies.

## 🛠️ Architectural Topologies & Design Patterns

### 1. Vendor Abstraction & Loose Coupling
* **Interface-Driven Contract:** Client components (e.g., ViewModels or Repositories inside `:features`) consume the unified `AnalyticsManagerInterface` via dependency injection. They remain entirely agnostic of whether data is routed to Google Firebase, Amplitude, Mixpanel, or a local diagnostic system.
* **Encapsulated Visibility:** Leveraging Dagger Hilt's `@Binds` routing alongside the Kotlin `internal` visibility modifier ensures that concrete manager implementations remain locked within the module boundaries, exposing only pure abstract contracts to the rest of the application.

### 2. Strongly-Typed Factory Domain Events
* **Compile-Time Safety:** Eliminates volative, hardcoded raw strings by enforcing structured event registration registries.
* **Domain Segregation:** Events are separated logically into specialized factories:
    * `AuthAnalyticsEvents`: Monitors registration funnels, federated authentication (Google SSO conversion tracks), and credential recovery pipelines.
    * `FirestoreEvents`: Tracks remote NoSQL document snapshots (reads, network mutation writes, and deletions) to monitor operational thresholds against infrastructure cloud quotas.

### 3. Defensive Production Processing
* **API Constrained Truncation:** To safeguard against silent vendor dropouts or application runtime crashes induced by strict platform limitations, the `AnalyticsManager` implements a defensive sanitization layer. Parameter keys are aggressively restricted to their first 40 characters ($key.take(40)$), and stringified metadata values are bound to a tight ceiling of 100 characters ($value.take(100)$).
* **Conditional Gatekeeping:** Utilizes an integrated audit control flag (`TypeLog.register`) to short-circuit event telemetry processing instantly based on operational settings or user privacy compliance frameworks.

---

## 📂 Structural Overview

```text
:core:analytics
├── com.package.analytics
│   ├── AnalyticsManagerInterface.kt # Unified abstract consumer contract
│   ├── AnalyticsManager.kt          # Class thats permits prepare events
│   │
│   ├── di                           # Dependency Injection Graph
│   │   ├── AnalyticsModule.kt       # Static provider factory (Foreign SDK provision)
│   │   └── AnalyticsRepositoryModule.kt # Abstract binding definitions (Internal encapsulation)
│   │
│   ├── events                       # Strongly-typed factory registries
│   │   ├── AuthAnalyticsEvents.kt   # Authentication funnel tracking logs
│   │   └── FirestoreEvents.kt       # Cloud database query & mutation tracking logs
│   │
│   ├── model                        # Immutable Data Carriers
│   │    ├── AnalyticsEvent.kt        # Structural semantic container metadata
│   │    └── TypeLog.kt               # Event taxonomy and auditing rule tokens
│   │ 
│   └── AnalyticsUtils.kt            # Utility functions for tracking 
