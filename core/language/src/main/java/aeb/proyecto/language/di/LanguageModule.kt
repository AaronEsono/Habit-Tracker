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

    @Binds
    internal abstract fun bindLanguageModule(
        languageManager: LanguageManager
    ): LanguageInterface
}