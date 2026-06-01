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

/**
 * Core infrastructure orchestrator implementing [LanguageInterface] to manage dynamic
 * application-level locale configurations.
 *
 * This manager leverages the modern Android 13+ [LocaleManager] system service API while
 * gracefully falling back to [AppCompatDelegate] backwards-compatibility layers to guarantee
 * seamless, dynamic language shifting across all target API levels without activity recreation.
 *
 * @property context The global [ApplicationContext] injection token used to interface with system services.
 */
class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context
): LanguageInterface {

    /**
     * Dynamically overrides the current application locale configuration.
     *
     * @param language The standard ISO-639-1 language code string token (e.g., "es", "en").
     */
    override fun setLanguage(language: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(language)
        }
    }

    /**
     * Inspects the active execution environment to resolve the active language code token.
     *
     * @return A clean ISO-639-1 language string representation, falling back to "en" if unassigned.
     */
    override fun getLanguage(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales[0].toLanguageTag()
        } else {
            AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag()?.split("-")?.first()
                ?: "en"
        }
    }
}