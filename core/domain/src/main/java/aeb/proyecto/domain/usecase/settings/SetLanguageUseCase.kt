package aeb.proyecto.domain.usecase.settings

import aeb.proyecto.language.LanguageInterface
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val languageInterface: LanguageInterface,
) {
    suspend fun setLanguage(language:String) = languageInterface.setLanguage(language)
}