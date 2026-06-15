package aeb.proyecto.save.components.common.spacer

import aeb.proyecto.ui.dimmens.Dimmens.spacing8
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * A standardized vertical spacer used to maintain consistent rhythm and
 * spacing throughout the Save module layouts.
 * @param vertical The vertical padding amount, defaulting to [spacing8] for
 * standard modular alignment.
 */
@Composable
fun CustomSpacerSave(vertical: Dp = spacing8){
    Spacer(modifier = Modifier.padding(vertical = vertical))
}