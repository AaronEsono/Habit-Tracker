package aeb.proyecto.habittracker.ui.components.checkBox

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.BodyMediumText
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CheckBoxButton(
    modifierRow: Modifier = Modifier,
    modifierCheckbox: Modifier = Modifier,
    modifierText: Modifier = Modifier,
    checkedColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    checkmarkColor: Color = MaterialTheme.colorScheme.primary,
    height: Dp = 56.dp,
    checkedState : Boolean,
    onStateChange: (Boolean) -> Unit
){
    Row(
        modifierRow.fillMaxWidth()
            .height(height),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = null,
            modifier = modifierCheckbox.clickable {
                onStateChange(!checkedState)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = checkedColor,
                checkmarkColor = checkmarkColor,
            )
        )
        BodyMediumText(
            text = stringResource(R.string.import_habit_screen_remember),

            modifier = modifierText.padding(start = spacing8).clickable {
                onStateChange(!checkedState)
            }
        )
    }
}