package aeb.proyecto.authentication.utils

import com.google.firebase.auth.FirebaseAuthException
import java.security.MessageDigest
import java.util.UUID


fun treatException(e:Exception):String{
    val error = if (e is FirebaseAuthException)
        e.errorCode
    else ""

    return error
}