# Module: Build-Logic 🛠️

A dedicated, standalone Gradle build configuration module that implements **Convention Plugins**.

In a multi-module Android architecture, build scripts can quickly become bloated with repetitive boilerplate code. This module centralizes and unifies the build logic, project dependencies, and compiler settings across the entire ecosystem, ensuring a single source of truth and enforcing architectural boundaries.

---

## 🚀 Key Responsibilities

* **Boilerplate Reduction:** Eliminates repetitive `build.gradle.kts` configurations for submodules.
* **Consistency:** Enforces identical SDK targets, Java compile options, and optimization flags across features.
* **Decoupling:** Keeps feature modules decoupled from the underlying dependency injection and build tool implementations.

---

## 📋 Available Convention Plugins

This module registers custom plugins that map directly to standard technology stacks used within the application. You can apply them using the IDs defined via the Version Catalog (`libs.versions.toml`):

| Plugin Class Name | Plugin ID / Alias | Description & Side Effects |
| :--- | :--- | :--- |
| **`AndroidLibraryPluginConvention`** | `habittracker.android.library.plugin.convention` | The foundation of all library modules. Sets `compileSdk 36`, `minSdk 26`, Java 11 compilation targets, and default release ProGuard rules. |
| **`HiltPluginConvention`** | `habittracker.hilt.plugin.convention` | Applies Dagger Hilt and Kotlin Kapt. Provisions core injection and Navigation Compose runtime dependencies. |
| **`JetpackComposePluginConvention`** | `habittracker.jetpack.compose.plugin.convention` | Enables Compose compiler flags, configures the Compose BoM, and provisions basic Material 3, UI, and Graphics libraries. |
| **`TestingPluginConvention`** | `habittracker.testing.plugin.convention` | Sets up the AndroidX Test Runner and registers local unit testing frameworks (JUnit4, MockK, Mockito, Coroutines Test). |
| **`ComposeUITestPluginConvention`** | `habittracker.compose.ui.test.plugin.convention` | Supplies the specialized Compose UI JUnit4 framework and test manifests for isolated composable testing. |
| **`FirebaseBasePluginConvention`** | `habittracker.firebase.base.plugin.convention` | Hooks up Google Services and Crashlytics plugins, alongside Firebase BoM (Auth, Firestore, Analytics) and Android Credentials Manager. |
| **`DatastorePluginConvention`** | `habittracker.datastore.plugin.convention` | Configures Jetpack Preferences DataStore for reactive, thread-safe local key-value storage. |

---

## 🛠️ Usage Example

Instead of manually maintaining long dependency blocks, a new feature module (e.g., `:feature:habits`) simply declares the pre-configured blocks it requires:

```kotlin
// feature/habits/build.gradle.kts
plugins {
    alias(libs.plugins.habittracker.android.library.plugin.convention)
    alias(libs.plugins.habittracker.jetpack.compose.plugin.convention)
    alias(libs.plugins.habittracker.hilt.plugin.convention)
    alias(libs.plugins.habittracker.testing.plugin.convention)
}

android {
    namespace = "com.yourdomain.habittracker.feature.habits"
}

dependencies {
    // Only module-specific business logic dependencies go here
    implementation(project(":core:database"))
}