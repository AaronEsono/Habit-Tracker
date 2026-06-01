package aeb.proyecto.firestore.model

import java.time.LocalDateTime

/**
 * Represents the cloud serialization schema architecture for data pushed to
 * and pulled from remote Firestore documents.
 *
 * This data class features default argument initializers to guarantee the generation
 * of an implicit no-argument constructor, which is strictly required by the
 * Firebase Firestore SDK serialization engine to instantiate objects via reflection.
 *
 * @property habit The raw string text or identifier payload representing the target habit behavior.
 * @property date The ISO-8601 string snapshot indicating when this specific data record was consolidated.
 */
data class FirestoreData(
    val habit:String = "",
    val date:String = LocalDateTime.now().toString()
)