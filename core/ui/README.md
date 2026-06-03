# 🎨 UI Architecture & Design System Module

This module houses the centralized Design System and presentation scaffolding for the application. Built entirely on **Jetpack Compose (Material 3)**, it isolates all graphical tokens, atom components, gesture interception mechanics, and architectural layout orchestrators to enforce visual consistency across features.

---

## 🛠 Core Technical Implementations

### 1. Stateful Input Sanitization Engine
To eliminate data corruption at the frontier layer, the text input architecture leverages the state-backed `TextFieldState` API instead of legacy `String` callbacks.
* **Reactive Interception:** Connects directly to `LaunchedEffect(textFieldState.text)` to perform synchronous character filtering via optimized `Regex` constraints on every buffer mutation.
* **Ergonomic Formatting:** Automatically transforms structural shorthand mutations in real-time (e.g., expanding leading decimals like `.` into `0.`, and pre-padding clock sequences like `7` into `07`).

### 2. High-Performance Gesture & Focus Pipelines
* **Exponential Decay Clicker (`repeatingClickable`):** A custom low-level `pointerInput` modifier tracking touch hardware pressure. It bypasses main-thread drawing allocations and uses a progressive fractional decay factor ($20\%$) to exponentially accelerate input incrementation the longer a node remains pressed.
* **Software Keyboard Focus Sync:** Integrates with `WindowInsets.isImeVisible` to actively monitor OS keyboard state dismissals. It cleanly wipes active focus coordinates out of text layout matrices when the user closes the IME, preventing blinking cursor leaks.
* **Refined Tactile Response:** Overrides the legacy cascading ripple configuration down the composition tree to silence hovering/focus noise, confining feedback to sharp, high-contrast, zero-opacity pointer strikes.

### 3. Dynamic Multi-Theme Orchestration
The module provides a centralized design engine supporting **8 distinct visual personalities** (ranging from pure black OLED layouts to sub-toned clear stone matrices) with zero runtime memory instantiation cost:
* **Persistent Enums:** Themes are tied to primitive compilation-time integers to insulate local storage contracts against future code refactorings.
* **Static Resolution:** The root custom theme composable uses highly optimized $O(1)$ conditional routing to swap global `ColorScheme` and `Typography` contexts transparently across all downstream sub-nodes.

### 4. Decoupled Top Bar Scaffold Infrastructure
Resolves the common architectural bottleneck of view-scaffolding inside deep navigation graphs:
* **Centralized Coordination:** A centralized `TopBarViewModel` holds declarative slots for title copy, navigation triggers, and context action rows.
* **Referential Equality Protection:** State variables leverage explicit `referentialEqualityPolicy()` bindings to halt aggressive compiler lambda invalidations, protecting rendering performance.
* **Safe Context Injection:** Screen-level wrappers (`ProvideAppBarTitle`, `ProvideAppBarActions`) isolate local `NavBackStackEntry` contexts to dynamically feed the shared scaffold without cross-screen state leakage.

---

## 📐 Design System Tokens

### Typography Matrix
The text framework standardizes an explicit **9-level typographic hierarchy** spanning `Body`, `Label`, and `Title` variations.
* All tokens default to semantic surface contrast colors (`onSurface`) to ensure instant compliance across light and dark theme matrices.
* Base reading blocks are configured with rigid $150\%$ proportional line-height metrics to maximize human reading tracking speeds on high-density displays.

### Primitive Variables
* **Dimensions:** Centralized layout paddings, target grid guidelines, and element scaling vectors are declared as static extensions to eliminate magic numbers from layout files.

```text
ui/
└── kotlin+java/
└── aeb.proyecto.ui/
├── bottomsheet/         # Custom sheets for contextual overlay components
├── calendar/            # High-density custom tracker calendar matrices
├── color/               # Multi-palette theme hex definitions
├── constants/           # Core static animation speeds and visual boundaries
├── controllerProvider/  # Global application controllers & scoping extensions
├── date/                # Localized format mappings for date variables
├── dialog/              # Structural alert, tracking, and confirmation dialog boxes
├── dimmens/             # Centralized spatial layout grids and elevation systems
├── month/               # Month-to-index semantic mappings and string wrappers
├── navigationicon/      # Core interface vectors for navigation asset hooks
├── orientation/         # Adaptive viewport multipliers and device screen-size bounds
├── regexTextField/      # Sanitization regex expressions and input logic filters
├── repeatingClick/      # Custom touch modifiers supporting exponential acceleration
├── ripple/              # Platform interaction ripple suppression profiles
├── text/                # Explicit 9-level typographic text wrapper catalog
├── textField/           # State-backed custom and secure outlined inputs
├── theme/               # Global dynamic MaterialTheme orchestrator
├── timer/               # Static base-zero anchors for progress/countdown views
├── topbar/              # High-performance state coordinator for scaffold headers
└── typography/          # Core text style matrices, spacing, and weight blueprints