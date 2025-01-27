package aeb.proyecto.room.di

import aeb.proyecto.room.database.DatabaseHabitTracker
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
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase (@ApplicationContext context: Context):DatabaseHabitTracker{
        return Room.databaseBuilder(
            context = context,
            DatabaseHabitTracker::class.java,
            "habit_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHabitDao(databaseHabit: DatabaseHabitTracker) = databaseHabit.habitDao()

    @Provides
    @Singleton
    fun provideDailyHabitDao(databaseHabit: DatabaseHabitTracker) = databaseHabit.dailyHabitDao()

    @Provides
    @Singleton
    fun provideHabitWithNotificationDao(databaseHabit: DatabaseHabitTracker) = databaseHabit.habitWithNotificationDao()

    @Provides
    @Singleton
    fun provideCompleteDaoHabit(databaseHabit: DatabaseHabitTracker) = databaseHabit.entireDaoHabit()
}