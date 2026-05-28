package aeb.proyecto.datastore

import aeb.proyecto.datastore.model.LastSearched
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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

//    private lateinit var dataStore: DataStore<Preferences>
//    private lateinit var dataStoreManager: DataStoreManager
//
//    @Before
//    fun setUp() = runBlocking{
//        dataStore = PreferenceDataStoreFactory.create(
//            produceFile = { File.createTempFile("test_prefs", ".preferences_pb") }
//        )
//
//        dataStoreManager = DataStoreManager(dataStore)
//    }
//
//    @Test
//    fun `set ThemeMode in Datastore preferences`() = runBlocking{
//        //Given
//        val themeMode = 1
//
//        //When
//        dataStoreManager.setModeTheme(themeMode)
//
//        //Then
//        val value = dataStoreManager.themeMode.first()
//        assertEquals(1,value)
//        assertNotNull(value)
//    }
//
//    @Test
//    fun `set Email in Datastore preferences`() = runBlocking{
//        //Given
//        val email = "valueExample@gmail.com"
//
//        //When
//        dataStoreManager.setEmail(email)
//
//        //Then
//        val value = dataStoreManager.getEmailPassword().email
//        assertEquals("valueExample@gmail.com",value)
//        assertNotNull(value)
//    }
//
//    @Test
//    fun `set Password in Datastore preferences`() = runBlocking{
//        //Given
//        val password = "passwordExample"
//
//        //When
//        dataStoreManager.setPassword(password)
//
//        //Then
//        val value = dataStoreManager.getEmailPassword().password
//        assertEquals("passwordExample",value)
//        assertNotNull(value)
//    }
//
//    @Test
//    fun `set LastSearched in Datastore preferences`() = runBlocking {
//        //Given
//        val lastSearched = LastSearched(uid = "uidExample", date = "dateExample")
//
//        //When
//        dataStoreManager.setLastSearched(lastSearched.uid ?: "",lastSearched.date ?: "")
//
//        //Then
//        val value = dataStoreManager.getLastSearched()
//        assertEquals("uidExample",value.uid)
//        assertEquals("dateExample",value.date)
//        assertEquals(true,value.searched)
//
//        assertNotNull(value.uid)
//        assertNotNull(value.date)
//        assertNotNull(value.searched)
//    }
//
//    @Test
//    fun `default value in themeMode`() = runBlocking{
//        //When
//        val value = dataStoreManager.themeMode.first()
//
//        //Then
//        assertEquals(0,value)
//
//        assertNotNull(value)
//    }
//
//    @Test
//    fun `default value in email and password`() = runBlocking{
//        //When
//        val value = dataStoreManager.getEmailPassword()
//
//        //Then
//        assertEquals("",value.email)
//        assertEquals("",value.password)
//
//        assertNotNull(value.email)
//        assertNotNull(value.password)
//    }
//
//    @Test
//    fun `default value in lastSearched`() = runBlocking{
//        //When
//        val value = dataStoreManager.getLastSearched()
//
//        //Then
//        assertEquals("",value.uid)
//        assertEquals("",value.date)
//        assertEquals(false,value.searched)
//
//        assertNotNull(value.uid)
//        assertNotNull(value.date)
//        assertNotNull(value.searched)
//    }
//
//    @Test
//    fun `clear user data only deletes email and password in datastore preferences`() = runBlocking{
//        //Given
//        val email = "valueExample@gmail.com"
//        val password = "passwordExample"
//        val themeMode = 1
//        val lastSearched = LastSearched(uid = "uidExample", date = "dateExample")
//
//        //When
//        dataStoreManager.setEmail(email)
//        dataStoreManager.setPassword(password)
//        dataStoreManager.setModeTheme(themeMode)
//        dataStoreManager.setLastSearched(lastSearched.uid ?: "",lastSearched.date ?: "")
//
//        dataStoreManager.clearDataUser()
//
//        val emailValue = dataStoreManager.getEmailPassword().email
//        val passwordValue = dataStoreManager.getEmailPassword().password
//        val themeModeValue = dataStoreManager.themeMode.first()
//        val lastSearchedValue = dataStoreManager.getLastSearched()
//
//        //Then
//        assertEquals("",emailValue)
//        assertEquals("",passwordValue)
//
//        assertEquals(themeModeValue,1)
//        assertEquals("uidExample",lastSearchedValue.uid)
//        assertEquals("dateExample",lastSearchedValue.date)
//        assertEquals(true,lastSearchedValue.searched)
//    }
//
//    @Test
//    fun `set another email after set a previous email`() = runBlocking {
//        //Given
//        val email1 = "previousEmail@gmail.com"
//        val email2 = "newEmail@gmail.com"
//
//        //When
//        dataStoreManager.setEmail(email1)
//        dataStoreManager.setEmail(email2)
//
//        //Then
//        val value = dataStoreManager.getEmailPassword().email
//
//        assertEquals("newEmail@gmail.com",value)
//        assertNotEquals("previousEmail@gmail.com",value)
//    }
//
//    @Test
//    fun `set another themeMode after set a previous themeMode`() = runBlocking {
//        //Given
//        val themeMode1 = 1
//        val themeMode2 = 2
//
//        //When
//        dataStoreManager.setModeTheme(themeMode1)
//        dataStoreManager.setModeTheme(themeMode2)
//
//        //Then
//        val value = dataStoreManager.themeMode.first()
//
//        assertEquals(2,value)
//        assertNotEquals(1,value)
//    }
//
//    @Test
//    fun `concurrent writes and reads in datastore preferences`() = runBlocking {
//        //Given
//        val email1 = "previousEmail@gmail.com"
//        val email2 = "newEmail@gmail.com"
//        val themeMode1 = 1
//        val themeMode2 = 2
//
//        //When
//        coroutineScope {
//            val job1 = async {
//                repeat(100){
//                    dataStoreManager.setEmail(email1)
//                    dataStoreManager.setModeTheme(themeMode1)
//                }
//            }
//            val job2 = async {
//                repeat(100){
//                    dataStoreManager.setEmail(email2)
//                    dataStoreManager.setModeTheme(themeMode2)
//                }
//            }
//
//            job1.await()
//            job2.await()
//        }
//
//        //Then
//        val email = dataStoreManager.getEmailPassword().email
//        val themeMode = dataStoreManager.themeMode.first()
//
//        assertEquals(true,email == "previousEmail@gmail.com" || email == "newEmail@gmail.com")
//        assertEquals(true,themeMode == 1 || themeMode == 2)
//    }
//
//    @Test
//    fun `store a large email in datastore preferences`() = runBlocking {
//        //Given
//        val longEmail = "a".repeat(10000) + "@example.com"
//
//        //When
//        dataStoreManager.setEmail(longEmail)
//
//        //Then
//        val value = dataStoreManager.getEmailPassword().email
//        assertEquals(longEmail,value)
//    }
//
//    @Test
//    fun `store a large themeMode in datastore preferences`() = runBlocking {
//        //Given
//        val negativeThemeMode = -9999
//        val largeThemeMode = Int.MAX_VALUE
//
//        //When
//        dataStoreManager.setModeTheme(negativeThemeMode)
//        val firstValue = dataStoreManager.themeMode.first()
//
//        dataStoreManager.setModeTheme(largeThemeMode)
//        val secondValue = dataStoreManager.themeMode.first()
//
//        //Then
//        assertEquals(negativeThemeMode,firstValue)
//        assertEquals(largeThemeMode,secondValue)
//    }

}