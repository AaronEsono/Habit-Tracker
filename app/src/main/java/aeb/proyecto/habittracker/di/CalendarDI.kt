package aeb.proyecto.habittracker.di

import aeb.proyecto.habittracker.data.database.DatabaseHabit
import aeb.proyecto.habittracker.data.model.calendar.CalendarDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalendarDI {

    @Provides
    @Singleton
    fun provideCalendar() = CalendarDataSource()
}