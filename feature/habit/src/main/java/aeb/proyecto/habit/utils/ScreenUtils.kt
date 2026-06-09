package aeb.proyecto.habit.utils

import aeb.proyecto.ui.dimmens.Dimmens.spacing10
import aeb.proyecto.ui.dimmens.Dimmens.spacing12
import aeb.proyecto.ui.dimmens.Dimmens.spacing4
import aeb.proyecto.ui.dimmens.Dimmens.spacing6
import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import aeb.proyecto.ui.dimmens.Dimmens.spacing80
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier

/**
 * Computes and injects localized vertical padding configurations for habit items inside standard portrait lists.
 * Dynamically forces an extra bottom buffer on the tail element to prevent visual overlap with the FloatingActionButton,
 * and normalizes top spacing exclusively for the structural head node of the collection.
 *
 * @param index The zero-based operational position of the current item inside the parent layout iteration log.
 * @param lastElement State indicator signaling if the active node represents the final item of the collection.
 */
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

/**
 * Computes and injects localized padding configurations for habit items running inside multi-column landscape grids.
 * Analyzes binary coordinate rows (checking indexes 0 and 1) to push uniform top baselines, and injects protective
 * bottom layout buffers on trailing nodes to maintain absolute separation from floating system overlay controls.
 *
 * @param index The zero-based operational position of the current item inside the parent grid layout layout iteration.
 * @param lastElement State indicator signaling if the active node sits at the absolute tail end of the collection.
 */
fun Modifier.cardHabitPaddingHorizontal(index:Int, lastElement:Boolean = false): Modifier {
    return if(lastElement){
        if(index == 0 || index == 1)
            padding(bottom = spacing80, top = spacing12)
        else
            padding(bottom = spacing80)
    }else{
        if (index == 0 || index == 1) padding(top = spacing12) else Modifier
    }
}