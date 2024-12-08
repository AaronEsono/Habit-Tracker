package aeb.proyecto.habittracker.di

import aeb.proyecto.habittracker.data.database.DatabaseHabit
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDI {

    @Provides
    @Singleton
    fun provideDatabase (@ApplicationContext context: Context):DatabaseHabit{
        return Room.databaseBuilder(
            context = context,
            DatabaseHabit::class.java,
            "habit_database"
        ).build()
    }

    @Provides
    fun provideHabitDao(databaseHabit: DatabaseHabit) = databaseHabit.habitDao()
}