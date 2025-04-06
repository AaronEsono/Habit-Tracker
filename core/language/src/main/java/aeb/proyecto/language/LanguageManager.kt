package aeb.proyecto.language

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context
): LanguageInterface {

    override fun setLanguage(language: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(language)
        }else{
            // Establece el idioma local
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources = context.resources

            // Modifica la configuración de recursos
            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            // Reinicia la actividad para aplicar el cambio
            val intent = Intent(Intent.ACTION_VIEW, "app://main".toUri()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            // Inicia la nueva actividad y cierra la actual para aplicar el idioma
            context.startActivity(intent)
            (context as Activity).finish()
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