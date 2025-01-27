package aeb.proyecto.room.repository

import aeb.proyecto.room.dao.HabitDao
import aeb.proyecto.room.database.DatabaseHabitTracker
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class HabitRepoTest {

    private lateinit var database: DatabaseHabitTracker
    private lateinit var habitDao: HabitDao
    private lateinit var habitRepo: HabitRepo

    @Before
    fun setUpDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            DatabaseHabitTracker::class.java
        ).allowMainThreadQueries()
            .build()

        habitDao = database.habitDao()
        habitRepo = HabitRepo(habitDao)
    }

    @Test
    fun prueba() = runBlocking{
        assertEquals(1,1)
    }

    @After
    fun closeDatabase() {
        database.close()
    }
}