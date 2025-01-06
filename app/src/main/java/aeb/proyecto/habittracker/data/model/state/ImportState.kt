package aeb.proyecto.habittracker.data.model.state

import aeb.proyecto.habittracker.R

data class ImportState(
    val isInLogin: Boolean = true,
    val title: Int = R.string.import_habit_screen_login,
    val subtitle: Int = R.string.import_habit_screen_subtitle_login,
    val textSignIn: Int = R.string.import_habit_screen_no_account,
    val textSignUp: Int = R.string.import_habit_screen_sign_up,
    val showGeneralDx:Boolean = false,
    val subtitleDx:Int = R.string.import_habit_create_account,
    val titleButton:Int = R.string.buttons_accept,
    val titleDx:Int  = R.string.general_dx_create_account,
)