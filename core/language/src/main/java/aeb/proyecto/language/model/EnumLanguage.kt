package aeb.proyecto.language.model

import aeb.proyecto.language.R
import androidx.annotation.DrawableRes

enum class EnumLanguage(val title:Int, val value:String,@DrawableRes val image:Int) {
    ESPANOL(R.string.language_es, "es",R.drawable.im_spain),
    ENGLISH(R.string.language_en, "en",R.drawable.im_uk)
}