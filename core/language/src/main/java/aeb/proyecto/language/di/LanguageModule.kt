package aeb.proyecto.language.di

import aeb.proyecto.language.LanguageInterface
import aeb.proyecto.language.LanguageManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LanguageModule {

    /**
     * Binds the internal infrastructure implementation of the language manager
     * to its corresponding domain abstraction interface.
     *
     * This setup isolates runtime locale resolution configurations, ensuring that
     * dependent features interact purely with the interface boundary.
     *
     * @param languageManager The core concrete application locale orchestrator.
     * @return A thread-safe exposed reference of [LanguageInterface].
     */
    @Binds
    internal abstract fun bindLanguageModule(
        languageManager: LanguageManager
    ): LanguageInterface
}