package aeb.proyecto.authentication.utils

import com.google.firebase.auth.FirebaseAuthException
import java.security.MessageDigest
import java.util.UUID

fun createNonce(): String {
    val rawNonce = UUID.randomUUID().toString()
    val bytes = rawNonce.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}

fun treatException(e:Exception):String{
    val error = if (e is FirebaseAuthException)
        e.errorCode
    else ""

    return error
}

//Sign up errors
const val ERROR_SEND_EMAIL = "ERROR_SEND_EMAIL"

// Sign in errors
const val ERROR_UNVERIFIED_EMAIL = "ERROR_UNVERIFIED_EMAIL"