# Add & Edit Habit Module

A high-performance, adaptive UI module built with Jetpack Compose for creating, configuring, and modifying user habits. Designed under strict architectural principles, this module natively scales across device orientations, provides robust hardware permission handling, and features an enterprise-grade state-driven workflow.

---

## Technical Stack & Jetpack Compose Ecosystem

*   **100% Jetpack Compose**: Form-factor agnostic rendering utilizing cutting-edge declarative layout paradigms.
*   **Hilt / Dependency Injection**: Scalable and scoped business logic instances via `@HiltViewModel` integration.
*   **Architecture Components (UfI/MVI approach)**: Predictable single-source-of-truth state emissions leveraging `StateFlow` and safe data consumption with `collectAsStateWithLifecycle()`.
*   **Material 3 Customization**: Adherence to a minimalist, custom monochromatic color palette system with dynamic light/dark contrast safety buffers.

---

## Architecture & Core Features

### 1. Dynamic Form Polymorphism
The module hosts reactive text input managers (`TextFieldState`) and dynamically transforms its composition tree based on selected parameters:
*   **Scalar Vector Counter**: Single-field interface optimized for numeric tracking increments (e.g., discrete counters).
*   **Chronometer Input Split**: Advanced dual-focus text nodes with an inline colon separator (`:`), dynamically handling sequential hardware focus via `ImeAction.Next` to capture time metrics (Hours/Minutes or Minutes/Seconds).

### 2. State-Driven Modal Context Router
Avoids decentralized boolean flag hell. Every modal dialog interface (including the strategy type picker, system `DatePickerDialogHabit`, scalar metric matrices, or chronometer alert wheels) is managed through an atomic state-driven `when` routing dispatcher controlled directly by the business layer.

### 3. Native Landscape Optimization & Matrix Chunking
Engineered specifically to prevent stretched or sub-optimal horizontal form layouts:
*   **Mirror Synchronization**: The landscape viewport splits primary metadata inputs (Name/Description) and quantitative units evenly into twin vertical column traces using proportional layout weights.
*   **Re-ordered Grid Collections**: The local push notifications register structures multi-column grids by chunking operational data tracks into uniform pairs (`chunked(2)`). It dynamically injects structural spacing buffers to guarantee perfectly aligned cell baselines when handling odd count collections.

### 4. Resilient Hardware Permission Barriers
Built-in defensive UI wrappers intercept execution paths when systemic hardware access (such as `POST_NOTIFICATIONS`) is restricted. Instead of disrupting workflows, the view seamlessly shifts to an elegant, tinted warning slate with localized, context-aware redirection bridges mapping directly into OS system settings.

### 5. Smart Container Unification
The root controller (`AddHabitScreen`) decouples visual layout constraints from the business engine. It handles lifecycle initialization asynchronously via `LaunchedEffect`, provisions structural toolbar metadata updates globally using contextual `CompositionLocal` providers (`ProvideAppBarTitle`), and bridges actions smoothly using efficient linear method references.

## Architectural File Structure

The module strictly implements a specialized **Atomic Design** workflow coupled with structural orientation encapsulation. Core presentation layers are completely decentralized from business engines, maintaining clear architectural boundaries:

```text
addhabit/
├── components/
│   ├── common/
│   │   ├── bottomSheet/       # Contextual modal persistent tray sheets
│   │   ├── button/            # Atomic interactive click triggers & micro-cards
│   │   ├── card/              # Standard leading graphic layout rows
│   │   ├── dialog/            # State-driven multi-purpose overlays
│   │   ├── divider/           # Clean structural boundary delimiters
│   │   ├── grid/              # Matrix catalogue selection views
│   │   ├── loading/           # Defensive asynchronous loading slates
│   │   ├── notifications/     # Adaptive notification cell items
│   │   ├── textField/         # Sanitized smart input fields
│   │   └── typeHabit/         # Complex temporal strategy sub-forms
│   ├── horizontal/            # Dedicated multi-column landscape layouts
│   └── vertical/              # Dedicated fluid portrait layouts
├── constants/                 # Immutable structural token trackers
├── converter/                 # Functional mapping & conversion pipelines
├── model/                     # Pure data layer semantic contracts
├── navigation/                # Module router graph coordinators
├── utils/                     # Local context helper extensions
├── AddHabitScreen.kt          # Smart Container orchestration root
└── AddHabitViewModel.kt       # Core transactional business engine