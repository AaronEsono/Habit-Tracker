# Save/Sync Module

## Overview
This module facilitates the synchronization of user data between local storage (Room) and the cloud (Firestore). It provides a robust interface for backing up, restoring, deleting data, and managing user sessions.

## Core Features
* **Data Lifecycle Management**: Enables full synchronization cycles, including backup, recovery, and secure data deletion.
* **Orientation-Aware UI**: Adapts layout dynamically between `Portrait` (stacked) and `Landscape` (side-by-side) modes for optimal user experience.
* **Transactional Security**: Uses `ViewModel` to orchestrate operations across local databases, notification schedulers, and network layers, ensuring data integrity.
* **Reactive State Flow**: Implements `StateFlow` and `collectAsStateWithLifecycle` to maintain real-time synchronization status and UI consistency.

## Technical Implementation

### Architecture
The module follows a **Model-View-ViewModel (MVVM)** pattern:
* **ViewModel**: Acts as the central orchestrator for all business logic. It handles the mapping between Firestore response states and the UI state.
* **UI Layer**: Composed of `HorizontalSaveScreen` and `VerticalSaveScreen`, both consuming the same shared `SaveViewModel` logic.
* **State Management**: Uses a custom `SaveUIState` sealed class to handle different screen phases (Loading, Success, Error, LogOut).

### Key Components
* **Bottom Sheets**: Dynamically configured via an `enum` (`DataBottomSheet`), enabling single-component reuse for various actions (Save, Restore, Delete, Logout, Error).
* **Sync Card (`CardSave`)**: Displays the last synchronization timestamp using `LocalDateTime`, ensuring users have clear visibility into their data status.
* **Loading Overlay**: A full-screen blocking component (`SaveScreenLoading`) that prevents concurrent operations during network requests.

### Navigation
The module uses **Type-Safe Navigation** (`@Serializable object Save`). Navigation events are decoupled, delegating transitions to the navigation graph.

## Operations
| Operation | Functionality |
| :--- | :--- |
| **Save** | Fetches all local habits and persists them to Firestore. |
| **Restore** | Downloads habits from Firestore and synchronizes local Room database + notification scheduler. |
| **Delete** | Removes all user data from Firestore and clears local sync markers. |
| **LogOut** | Clears user session and triggers navigation to the Authentication flow. |

## Dependencies
* **Jetpack Compose**: For the declarative UI.
* **Hilt**: For Dependency Injection of UseCases and Repositories.
* **Kotlin Coroutines**: Used for asynchronous data operations (`Dispatchers.IO`).
* **Material Design 3**: Providing consistent components and surface elevations.

```text
aeb.proyecto.save
│
├── 📂 components
│   ├── 📂 common
│   │   ├── BottomSheetFilledButton.kt
│   │   ├── BottomSheetOutLinedButton.kt
│   │   ├── CardSave.kt
│   │   ├── CustomSpacerSave.kt
│   │   └── SaveScreenLoading.kt
│   │
│   ├── 📂 horizontal
│   │   ├── HorizontalSaveBottomSheet.kt
│   │   └── HorizontalSaveScreen.kt
│   │
│   └── 📂 vertical
│       ├── VerticalSaveBottomSheet.kt
│       └── VerticalSaveScreen.kt
│
├── 📂 model
│   ├── BottomSheetState.kt
│   ├── DataBottomSheet.kt
│   └── DataSaveScreen.kt
│
├── 📂 navigation
│   └── SaveNavigation.kt
│
├── SaveScreen.kt
└── SaveViewModel.kt