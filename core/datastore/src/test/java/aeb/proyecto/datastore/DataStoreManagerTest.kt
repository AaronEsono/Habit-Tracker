package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.LastSearched
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.io.File


class DataStoreManagerTest{

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() = runBlocking{
        dataStore = PreferenceDataStoreFactory.create(
            produceFile = { File.createTempFile("test_prefs", ".preferences_pb") }
        )

        dataStoreManager = DataStoreManager(dataStore)
    }

    @Test
    fun `set ThemeMode in Datastore preferences`() = runBlocking{
        //Given
        val themeMode = 1

        //When
        dataStoreManager.setModeTheme(themeMode)

        //Then
        val value = dataStoreManager.themeMode.first()
        assertEquals(value,1)
        assertNotNull(value)
    }

    @Test
    fun `set Email in Datastore preferences`() = runBlocking{
        //Given
        val email = "valueExample@gmail.com"

        //When
        dataStoreManager.setEmail(email)

        //Then
        val value = dataStoreManager.getEmailPassword().email
        assertEquals(value,"valueExample@gmail.com")
        assertNotNull(value)
    }

    @Test
    fun `set Password in Datastore preferences`() = runBlocking{
        //Given
        val password = "passwordExample"

        //When
        dataStoreManager.setPassword(password)

        //Then
        val value = dataStoreManager.getEmailPassword().password
        assertEquals(value,"passwordExample")
        assertNotNull(value)
    }

    @Test
    fun `set LastSearched in Datastore preferences`() = runBlocking {
        //Given
        val lastSearched = LastSearched(uid = "uidExample", date = "dateExample")

        //When
        dataStoreManager.setLastSearched(lastSearched.uid ?: "",lastSearched.date ?: "")

        //Then
        val value = dataStoreManager.getLastSearched()
        assertEquals(value.uid,"uidExample")
        assertEquals(value.date,"dateExample")
        assertEquals(value.searched,true)

        assertNotNull(value.uid)
        assertNotNull(value.date)
        assertNotNull(value.searched)
    }

    @Test
    fun `default value in themeMode`() = runBlocking{
        //When
        val value = dataStoreManager.themeMode.first()

        //Then
        assertEquals(value,0)

        assertNotNull(value)
    }

    @Test
    fun `default value in email and password`() = runBlocking{
        //When
        val value = dataStoreManager.getEmailPassword()

        //Then
        assertEquals(value.email,"")
        assertEquals(value.password,"")

        assertNotNull(value.email)
        assertNotNull(value.password)
    }

    @Test
    fun `default value in lastSearched`() = runBlocking{
        //When
        val value = dataStoreManager.getLastSearched()

        //Then
        assertEquals(value.uid,"")
        assertEquals(value.date,"")
        assertEquals(value.searched,false)

        assertNotNull(value.uid)
        assertNotNull(value.date)
        assertNotNull(value.searched)
    }

}