
# Statistics Module

The **Statistics Module** is a core component of the application, designed to provide users with comprehensive insights into their habit-tracking data. It features a reactive, modular, and adaptive UI capable of handling complex data visualizations in both portrait and landscape orientations.

## Key Features

* **Responsive Dashboard:** Adaptive layouts that switch between portrait and landscape modes, ensuring an optimal experience on both smartphones and tablets.
* **Advanced Data Visualization:** Custom-built components using `Canvas` and `Vico` (Cartesian charts) to visualize habit trends, hourly activity, and completion rates.
* **Reactive Architecture:** Built on the MVI/MVVM pattern using `StateFlow` and `collectAsStateWithLifecycle` to ensure data consistency and efficient lifecycle management.
* **Dynamic Theming:** Fully integrated with Material Design 3, providing a consistent look and feel across all device orientations.
* **Localization-Ready:** Built-in support for internationalization of labels, months, and descriptions.

## Tech Stack

* **Jetpack Compose:** Declarative UI framework.
* **Kotlin Coroutines & Flow:** For asynchronous data streams and reactive state updates.
* **Hilt:** For dependency injection.
* **Vico:** Custom chart library for drawing Cartesian line charts.
* **Material 3:** Modern design system for UI consistency.

## Architecture

The module follows a clean architectural approach:
1.  **State Layer:** Uses a `ViewModel` to process business logic and expose UI states via `StateFlow`.
2.  **Screen Layer:** Adaptive entry points (`StatisticsScreen`) that react to orientation changes.
3.  **Component Layer:** Highly reusable, modular UI components (`HorizontalPieChart`, `ChartCanvas`, `GoalBox`, etc.) that derive their sizing and typography dynamically from the container bounds.

## Getting Started

To integrate this module into your project, ensure you have the necessary dependencies for Material 3, Hilt, and Vico. The entry point is the `StatisticsScreen` composable, which requires a `StatisticsViewModel` to manage the data flow.

## Documentation Highlights

The components in this module are built with **Jetpack Compose's Canvas API** for maximum performance and **BoxWithConstraints** for fluid, resolution-independent layouts. Every component is documented for maintainability and ease of testing.

---
*Developed as part of the Habit Tracking Application.*

```text
aeb.proyecto.save
│
├── 📂 components
│   ├── 📂 common
│   │
│   ├── 📂 horizontal
│   │   └── HorizontalStatisticsScreen.kt
│   │
│   └── 📂 vertical
│       └── VerticalStatisticsScreen.kt
│
├── 📂 model
│   ├── BoxUIState.kt
│   ├── Constants.kt
│   ├── GraphicsState.kt
│   ├── StatisticsState.kt
│   └── GoalsDoneState.kt
│
├── 📂 navigation
│   └── StatisticsNavigation.kt
│
├── StatisticsScreen.kt
└── StatisticsViewModel.kt