package aeb.proyecto.habittracker.data.model.state

import aeb.proyecto.habittracker.R

data class ImportState(
    val isInLogin: Boolean = true,
    val title: Int = R.string.import_habit_screen_login,
    val subtitle: Int = R.string.import_habit_screen_subtitle_login,
    val textSignIn: Int = R.string.import_habit_screen_no_account,
    val textSignUp: Int = R.string.import_habit_screen_sign_up
)