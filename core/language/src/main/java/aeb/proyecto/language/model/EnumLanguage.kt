package aeb.proyecto.language.model

import aeb.proyecto.language.R
import androidx.annotation.DrawableRes

/**
 * Supported localized languages within the ecosystem, bundling presentation identifiers,
 * ISO locale code configurations, and visual layout assets.
 *
 * @property title An explicit [Int] layout resource reference token pointing to the localized language name string.
 * @property value The specific standard ISO-639-1 language code string token.
 * @property image An explicit [Int] drawable resource reference token pointing to the regional flag graphic representation.
 */
enum class EnumLanguage(val title:Int, val value:String,@DrawableRes val image:Int) {
    ESPANOL(R.string.language_es, "es",R.drawable.im_spain),
    ENGLISH(R.string.language_en, "en",R.drawable.im_uk)
}

/**
 * Matches a raw ISO-639-1 code token against the registered localized language entries matrix.
 *
 * @param language The raw string target locale code to search for.
 * @return The matching [EnumLanguage] topology record, or null if no registry bounds overlap.
 */
fun findLanguage(language:String):EnumLanguage?{
    return EnumLanguage.entries.find { it.value == language }
}

/**
 * Resolves the presentation string resource identifier token for a specified language code,
 * gracefully falling back to [ENGLISH] if the matching evaluation fails.
 *
 * @param language The raw string target locale code to resolve.
 * @return An explicit [Int] reference mapping to the resolved target language UI string resource.
 */
fun returnStringValue(language: String):Int{
    return EnumLanguage.entries.find { it.value == language }?.title ?: EnumLanguage.ENGLISH.title
}