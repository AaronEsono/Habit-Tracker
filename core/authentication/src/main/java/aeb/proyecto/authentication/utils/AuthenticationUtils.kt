package aeb.proyecto.authentication.utils

import com.google.firebase.auth.FirebaseAuthException
import java.security.MessageDigest
import java.util.UUID

/**
 * A cryptographic utility that generates a secure, single-use token (Nonce) to safeguard
 * identity authentication pipelines against replay attacks.
 *
 * The function executes a three-stage deterministic security pipeline:
 * 1. **Entropy Generation:** It provisions an unpredictable, unique baseline seed using a universally
 * unique identifier ([UUID.randomUUID]).
 * 2. **Cryptographic Hashing:** It passes the seed's byte stream through a native **SHA-256** cryptographic
 * hashing function via [MessageDigest] to generate a secure, fixed-length digest signature.
 * 3. **Hexadecimal Formatting:** It reduces and folds the compiled byte array into a normalized, lower-case
 * hexadecimal string representation using structural formatting constraints:
 * $$\text{HEX} = \sum_{b \in \text{digest}} \text{format}("\%02\text{x}", b)$$
 *
 * @return A 64-character hexadecimal string representing the compiled unique cryptographic nonce token.
 */
fun createNonce(): String {
    val rawNonce = UUID.randomUUID().toString()
    val bytes = rawNonce.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}