package aeb.proyecto.domain.usecase.settings

import aeb.proyecto.language.LanguageInterface
import javax.inject.Inject

/**
 * Domain Use Case designed to orchestrate runtime language and localization adjustments
 * across the application environment.
 *
 * Acts as an isolated, single-responsibility boundary contract abstracting configuration-level
 * locale mutations behind a clean interface.
 *
 * @property languageInterface The boundary contract handling platform-specific localization updates.
 */
class SetLanguageUseCase @Inject constructor(
    private val languageInterface: LanguageInterface,
) {

    /**
     * Commits a structural locale mutation to dynamically switch the application's active language interface.
     *
     * @param language The ISO language code string (e.g., "en", "es") targeting the new system layout.
     */
    suspend fun setLanguage(language:String) = languageInterface.setLanguage(language)
}