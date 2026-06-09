# Habit UI Module

This module contains the presentation layer for the habit tracking system, designed with a responsive architecture that adapts seamlessly between vertical (mobile) and horizontal (tablet/landscape) orientations.

## Architecture Overview
The module follows a **State-Driven UI** pattern. It separates concerns by delegating business logic to ViewModels and rendering strategies based on device orientation.

### Key Components
* **Root Containers (`HabitScreen`):** Acts as the primary state orchestrator. It listens to orientation changes and routes the UI to either `VerticalHabitScreen` or `HorizontalHabitScreen`.
* **Content Orquestrators:** `VerticalHabitContentScreen` and `HorizontalHabitContentScreen` manage the lifecycle of habit lists, tab navigation, and modal bottom sheets.
* **Responsive View Layers:**
    * **Grid-based (`Horizontal`):** Utilizes `LazyVerticalGrid` to optimize horizontal screen real estate.
    * **List-based (`Vertical`):** Utilizes `LazyColumn` for native mobile scrolling experiences.
* **Strategy Pattern:** Employs specialized UI logic to render different card types (e.g., `UniqueMonthlyCard` vs. `SeparateMonthlyCard`) based on the habit configuration.

## Features
* **Orientation Awareness:** Seamless switching between list and grid layouts.
* **Staggered Entry Animations:** Each habit card uses `AnimatedVisibility` with custom delays to provide a polished, fluid user experience.
* **Unified Modal System:** Centralized management of configuration, editing, and date selection sheets, ensuring consistent behavior across all views.
* **State-Hoisting:** All event callbacks (clicks, long-presses, actions) are hoisted to the parent level, keeping UI components stateless and easily testable.

## Design Patterns
* **State-Driven UI:** UI components are purely reactive to `UiState` objects.
* **Strategy Pattern:** Dynamic selection of habit cards based on the `TypeHabit` and goal configuration.
* **Composition:** Modular design where specialized widgets (Headers, Calendars, Action Buttons) are reused across different screen variations.

## Project Structure

```text
/
├── components/          # UI components separated by orientation
│   ├── common/          # Reusable shared widgets
│   ├── horizontal/      # Horizontal layout implementations
│   └── vertical/        # Vertical layout implementations
├── constants/           # Project-wide constants
├── model/               # UI state models and data classes
├── navigation/          # Navigation logic and helpers
├── utils/               # Extension functions and utilities
├── HabitScreen.kt       # Root entry point (orientation handler)
└── HabitViewModel.kt    # Main ViewModel for the feature