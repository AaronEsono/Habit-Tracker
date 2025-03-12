package aeb.proyecto.room.model.habitCompressed

data class HabitCompressed(
    var name:String = "",
    var description:String? = "",
    var color:Int = 0,
    var icon:String = "",
    var times:Int = 0,
    var unit:Int = 0,
)