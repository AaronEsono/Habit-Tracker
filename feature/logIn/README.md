# Login Module

## 🚀 Overview
The Authentication Module handles the user sign-in and account recovery flows. It is built upon a modular, reactive architecture designed to provide a secure and seamless user experience across different screen sizes.

## 🏗 Architecture & Design
This module utilizes a **Compositional UI pattern** with **Material Design 3**. It emphasizes reusability through atomic components and a polymorphic approach to bottom sheets.

* **State Management**: Follows an MVI/MVVM pattern using `StateFlow` to ensure UI reactivity.
* **Polymorphic UI**: Custom `BottomSheet` containers (`HorizontalLoginBottomSheet`, `VerticalLoginBottomSheet`) adapt their layout based on the specific authentication workflow (e.g., Login, Forgot Password, Unverified Email).
* **Security & UX**:
    * **Obfuscation**: Secure fields feature a "Reveal Last Typed" mode to reduce input errors.
    * **Interaction Control**: Full-screen loading overlays prevent race conditions and accidental double-taps during authentication.

## 🧩 Component Library
The module includes the following specialized components:

| Component | Purpose |
| :--- | :--- |
| `LoginTextField` | Standard non-sensitive data entry with auto-clear functionality. |
| `LoginSecureTextField` | Obfuscated input for passwords/PINs with visibility toggle. |
| `LoginButton` | Elevated primary action button with state-aware styling. |
| `LoginGoogleButton` | Social auth integration with brand-aligned visual identity. |
| `LoginLoading` | Full-screen overlay to block interaction during async tasks. |

## 🛠 Technical Highlights
* **Focus Management**: Automated `FocusManager` integration ensures a smooth "Next" -> "Done" keyboard experience.
* **Responsive Layouts**: Design tokens and padding constants ensure the UI scales correctly between compact and expanded device profiles.
* **Declarative Logic**: Business logic is separated from UI state, allowing for easy testing and maintenance of the authentication flows.

## 🤝 Integration
To integrate this module into a new flow, ensure the `ViewModel` provides the required state and use the `Horizontal` or `Vertical` sheet variations depending on the host screen context.

```text
/
├── components/          # UI components separated by orientation
│   ├── common/          # Reusable shared widgets
│   ├── horizontal/      # Horizontal layout implementations
│   └── vertical/        # Vertical layout implementations
├── model/               # UI state models and data classes
├── navigation/          # Navigation logic and helpers
├── utils/               # Extension functions and utilities
├── LoginScreen.kt       # Root entry point (orientation handler)
└── LoginViewModel.kt    # Main ViewModel for the feature