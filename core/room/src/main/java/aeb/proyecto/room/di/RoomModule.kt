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

/**
 * Dependency Injection container module leveraging Hilt to orchestrate persistence engine configurations.
 *
 * This module provisions the centralized Room database singleton resource alongside its
 * dependent, thread-safe Data Access Objects (DAOs) to execute low-latency local storage workflows.
 */
@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    /**
     * Instantiates the primary monolithic relational database orchestrator for local state management.
     *
     * @param context The global [ApplicationContext] required to initialize sandbox disk storage access.
     * @return A thread-safe singleton instance of [DatabaseHabitTracker].
     */
    @Provides
    @Singleton
    fun provideDatabase (@ApplicationContext context: Context):DatabaseHabitTracker{
        return Room.databaseBuilder(
            context = context,
            DatabaseHabitTracker::class.java,
            "habit_database"
        ).build()
    }

    /**
     * Provisions the data pipeline interface mapping habits paired with their daily metrics records.
     *
     * @param databaseHabit The persistent database singleton instance.
     * @return The concrete [HabitWithDailyHabitDao] mapping implementation.
     */
    @Provides
    @Singleton
    fun provideHabitWithDailyHabitDao(databaseHabit: DatabaseHabitTracker) = databaseHabit.habitWithDailyHabitDao()

    /**
     * Provisions the data pipeline interface managing scheduled reminders and localization notification triggers.
     *
     * @param databaseHabit The persistent database singleton instance.
     * @return The concrete [HabitWithNotificationDao] mapping implementation.
     */
    @Provides
    @Singleton
    fun provideHabitWithNotificationDao(databaseHabit: DatabaseHabitTracker) = databaseHabit.habitWithNotificationDao()

    /**
     * Provisions the master transaction gateway covering complete relational habit schema operations.
     *
     * @param databaseHabit The persistent database singleton instance.
     * @return The concrete [EntireDaoHabit] mapping implementation.
     */
    @Provides
    @Singleton
    fun provideCompleteDaoHabit(databaseHabit: DatabaseHabitTracker) = databaseHabit.entireDaoHabit()

    /**
     * Provisions the time-tracking analytics interface logging duration data points for the stopwatch engine.
     *
     * @param databaseHabit The persistent database singleton instance.
     * @return The concrete [TimerEntryDao] mapping implementation.
     */
    @Provides
    @Singleton
    fun provideTimerEntryDao(databaseHabit: DatabaseHabitTracker) = databaseHabit.timerEntryDao()
}