package aeb.proyecto.language

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context
): LanguageInterface {

    override fun setLanguage(language: String) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(language)
        }else{
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
        }
    }

    override fun getLanguage(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales[0].toLanguageTag()
        } else {
            AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()?.split("-")?.first()
                ?: "en"
        }
    }
}