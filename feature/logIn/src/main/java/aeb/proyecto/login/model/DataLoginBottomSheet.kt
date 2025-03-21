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

enum class DataLoginBottomSheet(
    @StringRes var title: Int,
    var iconTitle: ImageVector,
    @StringRes var subtitle: Int,
) {
    ERROR(R.string.login_error_title, Icons.Filled.Error, R.string.login_error_default),
    UNVERIFIED_EMAIL(
        R.string.login_unverified_title,
        Icons.Filled.MarkEmailUnread,
        R.string.login_unverified_subtitle
    ),
    ACCOUNT_CREATED(
        R.string.login_account_created_title,
        Icons.Filled.CheckCircle,
        R.string.login_account_created_subtitle
    ),
    EMAIL_SENT(
        R.string.login_email_sent_title,
        Icons.Filled.MarkEmailRead,
        R.string.login_email_sent_subtitle
    ),
    FORGOT_PASSWORD(
        R.string.login_forgot_password_title,
        Icons.Filled.LockReset,
        R.string.login_forgot_password_subtitle
    ),
    EMAIL_SENT_FORGOT_PASSWORD(
        R.string.login_forgot_password_sent_title,
        Icons.Filled.Outbox,
        R.string.login_forgot_password_sent_subtitle
    )
}