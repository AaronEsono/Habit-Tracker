package aeb.proyecto.login.model

import aeb.proyecto.login.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material.icons.filled.Outbox
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines the configuration for various modal bottom sheets in the authentication flow.
 * * Each enum entry encapsulates the resource IDs for the title and subtitle,
 * alongside the icon to be displayed, ensuring a consistent UI/UX pattern
 * across different notification types.
 *
 * @property title The string resource ID for the bottom sheet header.
 * @property iconTitle The [ImageVector] to be displayed as a visual indicator.
 * @property subtitle The string resource ID for the bottom sheet body/description.
 */
enum class DataLoginBottomSheet(
    @StringRes var title: Int,
    var iconTitle: ImageVector,
    @StringRes var subtitle: Int,
) {

    /** Error notification state. */
    ERROR(R.string.login_error_title, Icons.Filled.Error, R.string.login_error_default),

    /** State when an email verification is required. */
    UNVERIFIED_EMAIL(
        R.string.login_unverified_title,
        Icons.Filled.MarkEmailUnread,
        R.string.login_unverified_subtitle
    ),

    /** Success state upon successful account creation. */
    ACCOUNT_CREATED(
        R.string.login_account_created_title,
        Icons.Filled.CheckCircle,
        R.string.login_account_created_subtitle
    ),

    /** Success state when an email has been dispatched. */
    EMAIL_SENT(
        R.string.login_email_sent_title,
        Icons.Filled.MarkEmailRead,
        R.string.login_email_sent_subtitle
    ),

    /** State for the password recovery request flow. */
    FORGOT_PASSWORD(
        R.string.login_forgot_password_title,
        Icons.Filled.LockReset,
        R.string.login_forgot_password_subtitle
    ),

    /** Success state after a password reset email has been sent. */
    EMAIL_SENT_FORGOT_PASSWORD(
        R.string.login_forgot_password_sent_title,
        Icons.Filled.Outbox,
        R.string.login_forgot_password_sent_subtitle
    )
}