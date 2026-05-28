# 🔐 Module: Authentication

This module architecture coordinates cloud session lifecycle synchronization and user profile management within the application ecosystem. It implements modern Android identity structures via the **Google Credential Manager API**, completely decoupled from underlying platform services using abstraction patterns.

## 🏗️ Architectural Topology

The package structure follows a strict Clean Architecture layout tailored for modularization and dependency inversion:

* **`di/`**: Houses Hilt modules (`FirebaseAuthModule`, `FirebaseModule`) isolating third-party SDK lifecycles and enforcing single-source-of-truth access points.
* **`errors/`**: Centralizes exception processing via `TreatErrors.kt`. Converts cryptic platform or network transport exceptions into deterministic, localized resource identifiers (`@StringRes`) preventing raw error leaks to UI layers.
* **`utils/`**: Implements static metadata anchors (`AuthenticationConstants.kt`) and stateless functional helpers (`AuthenticationUtils.kt`).
* **`AuthenticationInterface`**: The declarative architectural boundary. Exposes cold reactive streams (`Flow`) for decoupled presentation layer subscription.
* **`AuthenticationManager`**: The concrete infrastructure engine. Coordinates Firebase Auth events, JWT verification pipelines, and Google One Tap handshakes.

---

## 🛠️ Technology Stack & Core Design Patterns

### 1. Google Credential Manager & Identity Federation
The authentication pipeline replaces legacy Google Sign-In infrastructures with the modern `androidx.credentials` architecture.
* **Context Unwrapping**: To mitigate `GetCredentialCancellationException` anomalies within Jetpack Compose recomposition topologies, the module incorporates context-unwrapping mechanics to guarantee execution context maps explicitly to a physical `ComponentActivity`.
* **Cryptographic Verification**: Enforces atomic verification between local OAuth 2.0 Web Client identifiers and Firebase Identity structures before dispatching authentication session tokens.

### 2. Reactive Asynchronous Flow Architecture
All transaction boundaries are engineered as cold, sequential, execution-safe Kotlin **`Flow`** streams rather than traditional callback channels:
* **Zero Resource Leaks**: Replaces stateful `callbackFlow` mechanisms with sequential `flow { ... }` scopes, assuring automatic channel closure upon emitting terminal states (`Success` or `Error`).
* **Non-Blocking Suspension**: Heavy synchronization steps leverage the `kotlinx.coroutines.tasks.await()` bridge to seamlessly marshal Firebase asynchronous tasks linearly.

---

## 🚦 Transaction States (`AuthResponseAuthentication`)

Presentation components consume a sealed runtime topology representing the operational pipeline status:

* `Loading`: Informs the presentation layer to render non-interactive processing visuals.
* `Success`: Signals terminal transaction completion.
* `Error(val message: Int)`: Wraps a safe, production-ready `@StringRes` identifier ready for immediate layout rendering.

---

## 🔌 Core API Contract Expositions

### Cloud Session Lifecycle Mutators

```kotlin
// Re-initiates the email verification handshake for a verified credential profile
suspend fun resendEmail(email: String, password: String): Flow<AuthResponseAuthentication>

// Dispatches a secure cryptographic password recovery token link to an external SMTP mailbox
suspend fun forgotPassword(email: String): Flow<AuthResponseAuthentication>

// Terminates active local token states and signs out safely from Cloud nodes
fun logOut()

## 📁 Package & Directory Topology

Below is the structural organization of the authentication component, adhering to a strict Clean Architecture layout tailored for multi-module systems:

```text
aeb.proyecto.authentication/
│
├── 📂 di/                           # Dependency Injection Boundary (Hilt)
│   ├── 📄 FirebaseAuthModule.kt     # Scopes and provisions individual Firebase Auth instances
│   └── 📄 FirebaseModule.kt         # Exposes underlying structural Firebase gateway primitives
│
├── 📂 errors/                       # Centralized Exception Processing Perimeter
│   └── 📄 TreatErrors.kt            # Maps raw network/OAuth anomalies into deterministic UI @StringRes
│
├── 📂 utils/                        # Stateless Helpers & Constant Anchors
│   ├── 📄 AuthenticationConstants.kt # Global static configuration keys and string tags
│   └── 📄 AuthenticationUtils.kt    # Small functional primitives and non-stateful operations
│
├── 📄 AuthenticationInterface.kt    # Architectural Contract (Declarative Domain Boundary)
└── 📄 AuthenticationManager.kt      # Production Engine (Concrete Implementation & Task Marshalling)