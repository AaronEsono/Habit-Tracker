package aeb.proyecto.habittracker.data.model.data

import java.time.LocalDateTime

data class FirestoreData(
    val habit:String = "",
    val date:String = LocalDateTime.now().toString()
)