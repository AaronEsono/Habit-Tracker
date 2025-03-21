package aeb.proyecto.authentication.errors

import aeb.proyecto.authentication.R
import android.util.Log
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthWebException

fun treatError(e: Exception): Int {
    return when (e) {
        is FirebaseAuthInvalidUserException -> R.string.error_auth_no_user_found
        is FirebaseAuthUserCollisionException -> R.string.error_auth_email_exists
        is FirebaseAuthWeakPasswordException -> R.string.error_auth_weak_password
        is FirebaseAuthInvalidCredentialsException -> R.string.error_auth_invalid_password
        is FirebaseAuthEmailException -> R.string.error_auth_send_email
        is FirebaseAuthActionCodeException -> R.string.error_auth_invalid_oob_code
        is FirebaseAuthRecentLoginRequiredException -> R.string.error_auth_credential_too_old_login_again
        is FirebaseAuthMultiFactorException -> R.string.error_auth_mfa_required
        is FirebaseAuthWebException -> R.string.error_auth_web_storage_unsupported
        is FirebaseAuthException -> when (e.errorCode) {
            "ERROR_EMAIL_ALREADY_IN_USE" -> R.string.error_auth_email_exists
            "ERROR_USER_NOT_FOUND" -> R.string.error_auth_no_user_found
            "ERROR_WRONG_PASSWORD" -> R.string.error_auth_invalid_password
            "ERROR_WEAK_PASSWORD" -> R.string.error_auth_weak_password
            "ERROR_INVALID_EMAIL" -> R.string.error_auth_invalid_email
            "ERROR_TOO_MANY_REQUESTS" -> R.string.error_auth_too_many_attempts_try_later
            "ERROR_USER_DISABLED" -> R.string.error_auth_user_disabled
            "ERROR_REQUIRES_RECENT_LOGIN" -> R.string.error_auth_credential_too_old_login_again
            "ERROR_NETWORK_REQUEST_FAILED" -> R.string.error_auth_network_request_failed
            "ERROR_OPERATION_NOT_ALLOWED" -> R.string.error_auth_operation_not_allowed
            "ERROR_UNVERIFIED_EMAIL" -> R.string.error_auth_unverified_email
            else -> R.string.error_auth_default
        }
        else -> R.string.error_auth_default
    }
}

