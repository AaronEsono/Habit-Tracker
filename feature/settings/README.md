# Settings Module

This module manages all application configurations, including user preferences, dynamic dialog management, system permissions, and support links.

## Key Features

* **Architecture:** Implemented using MVVM, following Clean Architecture principles and Unidirectional Data Flow (UDF).
* **Reactivity:** Uses `StateFlow` and `collectAsStateWithLifecycle` for seamless UI integration.
* **Adaptive Design:** Differentiated layouts for Portrait and Landscape orientations.
* **Type-Safe Navigation:** Modern implementation using `Navigation Compose` with serializable routes.
* **Dynamic Dialogs:** Polymorphic dialog system based on a unique configuration model (`DataDialog`), making it easy to add new settings.
* **System Utilities:** Management of permissions (Overlay), opening external links, and pre-configured support email creation.

## Module Structure

* **`presentation/settings`**: Contains the `ViewModel` logic, the main screen (`SettingsScreen`), and orientation-specific layouts.
* **`presentation/settings/components`**: Reusable components (dialog buttons, dividers, loading indicators).
* **`presentation/settings/model`**: State models and `Sealed Classes` for communication between UI and ViewModel.
* **`presentation/settings/utils`**: Helper functions for OS interactions (Intents, permissions).
* **`presentation/settings/navigation`**: Navigation route configuration.

## Tech Stack

* **Jetpack Compose:** Declarative UI.
* **Hilt:** Dependency Injection.
* **Coroutines & Flow:** Reactive programming.
* **Material 3:** Design system and components.

## How to add new settings

1. Add a new option to the `DataDialog` enum (in `model`).
2. Define the element type in `DialogElements`.
3. Implement the saving logic within `treatResultDialog` in the `SettingsViewModel`.
4. Create a specific button component if necessary or reuse the existing ones.

```text
aeb.proyecto.save
│
├── 📂 components
│   ├── 📂 common
│   │
│   ├── 📂 horizontal
│   │   ├── HorizontalDialogSettings.kt
│   │   └── HorizontalSettingsScreen.kt
│   │
│   └── 📂 vertical
│       ├── VerticalDialogSettings.kt
│       └── VerticalSettingsScreen.kt
│
├── 📂 model
│   ├── DataDialog.kt
│   ├── DataResult.kt
│   └── SettingsDialogState.kt
│
├── 📂 navigation
│   └── SettingsNavigation.kt
│
├── SettingsScreen.kt
└── SettingsViewModel.kt