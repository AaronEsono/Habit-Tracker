# :core:alarm-manager Module

The `:core:alarm-manager` module encapsulates all infrastructure and business logic required to schedule, update, and revoke high-precision, low-power-resilient system notifications. It operates independently of the application's visual lifecycle, ensuring habit tracking reminders are dispatched punctually whether the application is active, scaled to the background, or completely closed.

## 🛠️ Key Architectural Responsibilities

Managing background alerts in modern Android SDK variants requires strict adherence to operating system energy constraints. This module solves these challenges through a defensive, decoupled design:

### 1. High-Precision Sequential Chaining (`NotificationUtils`)
* **Anti-Drift Strategy:** Rather than utilizing platform-level repeating engines (`AlarmManager.setRepeating`)—which are aggressively batched and delayed by the OS to save battery—this module implements a **sequential chaining pattern**.
* **Hardware Wake Hooks:** The engine registers an initial exact alert via `setExactAndAllowWhileIdle` (`RTC_WAKEUP`). The moment that alert fires, the system evaluates the chronological timeline and programmatically chains the subsequent alarm window, bypassing standard Doze Mode restrictions.
* **Algorithmic Week Wrapping:** Integrates an implicit calendar overflow calculator (`getNextDay`) to handle circular weekday boundaries (wrapping smoothly from Sunday back to Monday) when calculating the future millisecond delta profiles.

### 2. Reactive Asynchronous Receivers (`AlarmService`)
* **Lifecycle-Safe Bridging:** Anchored via `@AndroidEntryPoint`, this `BroadcastReceiver` intercepts system hardware triggers in real time, securely relying on Dagger Hilt field injection.
* **Dynamic Styling & Deep Linking:** Reconstructs incoming notification payloads to tint platform alerts with the habit's precise branding color. It wraps communication channels inside a secured, immutable `PendingIntent` populated with explicit deep-linking schemas (`app://main`) to manage flawless task stack restoration.

### 3. Polymorphic Data Persistence & Serialization
* **Type-Safe Serialization:** Features custom **Gson** adapters (`LocalTimeAdapter` and `TypeNotificationAdapter`) to transform complex time and inheritance graphs into flat JSON streams.
* **String Discriminator Topology:** Implements a manual polymorphic strategy by injecting a `"tag"` key field discriminator (e.g., `DAILY` or `RECURRING`). This decouples data definitions cleanly, ensuring safe data reconstruction and robust recovery default fallbacks against corrupted schemas.

---

## 📂 Structural Overview

```text
:core:alarm-manager
├── com.package.alarm
│   ├── AlarmService.kt             # Asynchronous BroadcastReceiver & execution hook
│   │
│   ├── constants                   # Constants of the module
│   │
│   ├── GsonProvider.kt             # Centralized Singleton building the unified Gson engine
│   │
│   ├── converters                  # Advanced JSON mapper infrastructure
│   │   ├── LocalTimeAdapter.kt     # ISO-8601 clock format converter
│   │   └── TypeNotificationAdapter.kt # Polymorphic discriminator serialization adapter
│   │
│   └── NotificationUtils.kt        # Main orchestration engine (setUp, cancel, repeat)
