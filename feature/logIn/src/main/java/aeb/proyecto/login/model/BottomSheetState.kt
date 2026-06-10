package aeb.proyecto.login.model

import androidx.compose.foundation.text.input.TextFieldState

/**
 * Represents the UI state for modal bottom sheets within the authentication flow.
 *
 * This class tracks whether a bottom sheet is visible, the type of content it
 * should display, and holds temporary state for specific fields like email inputs.
 *
 * @property showBottomSheet Visibility toggle for the bottom sheet.
 * @property dataBottomSheet The current configuration type for the bottom sheet (e.g., Error, Account Created).
 * @property emailSentForgotPassword State holder for the email input field specifically used in recovery flows.
 */
data class BottomSheetState(
    var showBottomSheet:Boolean = false,
    var dataBottomSheet: DataLoginBottomSheet = DataLoginBottomSheet.ERROR,
    var emailSentForgotPassword: TextFieldState = TextFieldState(),
)