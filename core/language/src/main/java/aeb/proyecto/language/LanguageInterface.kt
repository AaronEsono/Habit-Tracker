package aeb.proyecto.language

/**
 * Formal domain contract establishing the architectural boundaries for application locale management.
 *
 * This boundary abstracts the configuration mechanisms required to update and query
 * the operational language settings across runtime contexts and persistence layers.
 */
interface LanguageInterface {

    /**
     * Reconfigures the current application execution locale context and synchronizes
     * the update with the persistent state framework.
     *
     * @param language The raw standard ISO-639-1 language code string token to be applied.
     */
    fun setLanguage(language: String)

    /**
     * Retrieves the active localized language configuration token currently applied to the ecosystem.
     *
     * @return A standard ISO-639-1 string representation representing the active system or persistent locale.
     */
    fun getLanguage(): String

}