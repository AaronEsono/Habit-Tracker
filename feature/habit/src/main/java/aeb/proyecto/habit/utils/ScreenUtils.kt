package aeb.proyecto.habit.utils

import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing80
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier

fun Modifier.cardHabitPadding(index:Int, lastElement:Boolean = false): Modifier {
    return if(lastElement){
        if(index == 0)
            padding(bottom = spacing80, top = spacing12)
        else
            padding(bottom = spacing80)
    }else{
        if (index == 0) padding(top = spacing12, bottom = spacing10) else padding(bottom = spacing10)
    }
}