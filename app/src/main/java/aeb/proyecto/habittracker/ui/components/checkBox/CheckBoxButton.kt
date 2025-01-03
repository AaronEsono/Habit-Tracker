package aeb.proyecto.habittracker.ui.components.checkBox

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.text.BodyLargeText
import aeb.proyecto.habittracker.ui.components.text.BodyMediumText
import aeb.proyecto.habittracker.utils.ColorsTheme
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun CheckBoxButton(
    checkedState : Boolean,
    onStateChange: (Boolean) -> Unit
){
    Row(
        Modifier.fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = null,
            modifier = Modifier.clickable {
                onStateChange(!checkedState)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = ColorsTheme.themeText,
                checkmarkColor = ColorsTheme.primaryColorApp,
            )
        )
        BodyMediumText(
            text = stringResource(R.string.import_habit_screen_remember),
            modifier = Modifier.padding(start = spacing8).clickable {
                onStateChange(!checkedState)
            }
        )
    }
}