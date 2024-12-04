package aeb.proyecto.habittracker.ui.screens.addHabit

import aeb.proyecto.habittracker.R
import aeb.proyecto.habittracker.ui.components.textField.CustomOutlinedTextField
import aeb.proyecto.habittracker.utils.Dimmens.spacing16
import aeb.proyecto.habittracker.utils.Dimmens.spacing4
import aeb.proyecto.habittracker.utils.Dimmens.spacing8
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import kotlin.reflect.KProperty

@Composable
fun AddHabitScreen(){
    val nameHabit = rememberTextFieldState("")
    val descriptionHabit = rememberTextFieldState("")

    Column (modifier = Modifier.fillMaxSize().padding(spacing16)){

        CustomOutlinedTextField(
            rememberTextFieldState = nameHabit,
            label = R.string.add_habit_screen_name,
            isNeeded = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(vertical = spacing8))

        CustomOutlinedTextField(
            rememberTextFieldState = descriptionHabit,
            label = R.string.add_habit_screen_description,
            modifier = Modifier.fillMaxWidth()
        )

        Icons.Filled::class.members
            .filterIsInstance<KProperty<*>>()
            .mapNotNull { it.getter.call() as? ImageVector }

    }
}