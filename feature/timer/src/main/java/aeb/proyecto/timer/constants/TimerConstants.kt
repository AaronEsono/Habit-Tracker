package aeb.proyecto.timer.constants

val hours = (0..99).map { it.toString().padStart(2, '0') }
val minutes = (0..59).map { it.toString().padStart(2, '0') }
val seconds = (0..59).map { it.toString().padStart(2, '0') }

enum class TypeList{
    Hours, Minutes, Seconds
}