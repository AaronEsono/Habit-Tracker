package aeb.proyecto.firestore.errors

import aeb.proyecto.firestore.R
import com.google.firebase.firestore.FirebaseFirestoreException

fun treatError(e: Exception): Int {
    return when (e) {
        is FirebaseFirestoreException -> when (e.code) {
            FirebaseFirestoreException.Code.ABORTED -> R.string.error_firestore_aborted
            FirebaseFirestoreException.Code.ALREADY_EXISTS -> R.string.error_firestore_already_exists
            FirebaseFirestoreException.Code.CANCELLED -> R.string.error_firestore_cancelled
            FirebaseFirestoreException.Code.DATA_LOSS -> R.string.error_firestore_data_loss
            FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> R.string.error_firestore_deadline_exceeded
            FirebaseFirestoreException.Code.FAILED_PRECONDITION -> R.string.error_firestore_failed_precondition
            FirebaseFirestoreException.Code.INTERNAL -> R.string.error_firestore_internal
            FirebaseFirestoreException.Code.INVALID_ARGUMENT -> R.string.error_firestore_invalid_argument
            FirebaseFirestoreException.Code.NOT_FOUND -> R.string.error_firestore_not_found
            FirebaseFirestoreException.Code.OK -> R.string.error_firestore_ok
            FirebaseFirestoreException.Code.OUT_OF_RANGE -> R.string.error_firestore_out_of_range
            FirebaseFirestoreException.Code.PERMISSION_DENIED -> R.string.error_firestore_permission_denied
            FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> R.string.error_firestore_resource_exhausted
            FirebaseFirestoreException.Code.UNAUTHENTICATED -> R.string.error_firestore_unauthenticated
            FirebaseFirestoreException.Code.UNAVAILABLE -> R.string.error_firestore_unavailable
            FirebaseFirestoreException.Code.UNIMPLEMENTED -> R.string.error_firestore_unimplemented
            FirebaseFirestoreException.Code.UNKNOWN -> R.string.error_firestore_unknown
            else -> R.string.error_firestore_default
        }
        else -> R.string.error_firestore_default
    }
}
