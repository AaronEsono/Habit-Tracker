package aeb.proyecto.firestore.model

import java.time.LocalDateTime

data class FirestoreData(
    val habit:String = "",
    val date:String = LocalDateTime.now().toString()
)