# ⏱️ Timer Module

This module is the core component of the application, designed to manage high-precision timers and stopwatches. It features a robust, state-driven architecture optimized for performance, scalability, and seamless adaptability across different screen orientations.

## 🏗️ Technical Architecture

The module follows the **MVVM (Model-View-ViewModel)** pattern using **Jetpack Compose**, ensuring a unidirectional data flow and clean separation of concerns.

### Key Features
* **Adaptive Design:** Dynamic layout switching between `Vertical` and `Horizontal` modes using `BoxWithConstraints` and orientation detection.
* **State-Driven UI:** Reactive UI built with `collectAsStateWithLifecycle()`, ensuring efficient resource consumption and state consistency.
* **Modular Componentization:** A clear hierarchy of Atoms (base UI), Molecules (Pickers, Inputs), and Organisms (Screens).
* **Fluid Transitions:** Implementation of `AnimatedContent` for smooth state transitions between configuration and active execution modes.

## 🚀 Scalability Highlights

The system utilizes a **Main Router** pattern within the `TimerScreen`. By leveraging `contentKey = { it::class }` in animations, adding new timer modes or screen variations is highly maintainable. The architecture treats the UI as a function of its state, making it highly testable and extensible.

## 🛠️ Technology Stack
* **Kotlin & Jetpack Compose**
* **Hilt** for Dependency Injection
* **Coroutines & Flows** for asynchronous event management
* **Lifecycle KTX** for lifecycle-aware data collection