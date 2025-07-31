package aeb.proyecto.room.di

import aeb.proyecto.room.converters.TypeHabitConverter
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
    fun provideHabitWithDailyHabitDao(databaseHabit: DatabaseHabitTracker) = databaseHabit.habitWithDailyHabitDao()

    @Provides
    @Singleton
    fun provideHabitWithNotificationDao(databaseHabit: DatabaseHabitTracker) = databaseHabit.habitWithNotificationDao()

    @Provides
    @Singleton
    fun provideCompleteDaoHabit(databaseHabit: DatabaseHabitTracker) = databaseHabit.entireDaoHabit()

    @Provides
    @Singleton
    fun provideTimerEntryDao(databaseHabit: DatabaseHabitTracker) = databaseHabit.timerEntryDao()
}