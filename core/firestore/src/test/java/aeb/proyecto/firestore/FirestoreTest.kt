package aeb.proyecto.firestore

import aeb.proyecto.analytics.AnalyticsManagerInterface
import aeb.proyecto.analytics.events.FirestoreEvents
import aeb.proyecto.firestore.R
import aeb.proyecto.firestore.model.FirestoreData
import app.cash.turbine.test
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.theories.suppliers.TestedOn
import org.mockito.kotlin.mock
import java.time.LocalDate
import kotlin.Exception

class FirestoreTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val firestore: FirebaseFirestore = mockk()
    private val collectionReference: CollectionReference = mockk()
    private val documentReference: DocumentReference = mockk()
    private val documentSnapshot: DocumentSnapshot = mockk()
    private val analyticsManager: AnalyticsManagerInterface = mockk(relaxed = true)
    private lateinit var firestoreManager: FirestoreManager

    @Before
    fun setUp(){
        every { firestore.collection("Habits") } returns collectionReference

        firestoreManager = FirestoreManager(firestore, analyticsManager)
    }

    @Test
    fun `given a valid userId when getDataUser is called then returns Loading and Success states`() = runTest {
        //Given
        val userId = "1234"
        val expectedData = FirestoreData(habit = "Habit1", date= LocalDate.now().toString())
        val task: Task<DocumentSnapshot> = mockk()

        //When
        every { collectionReference.document(userId) } returns documentReference
        every { documentReference.get() } returns task

        coEvery { task.isComplete } returns true
        coEvery { task.isCanceled } returns false
        coEvery { task.result } returns documentSnapshot
        coEvery { task.exception } returns null

        every { documentSnapshot.toObject(FirestoreData::class.java) } returns expectedData

        //Then
        firestoreManager.getDataUser(userId).test {

            assertEquals(AuthResponseFirestore.Loading, awaitItem())

            val successResult = awaitItem() as AuthResponseFirestore.Success
            assertEquals(expectedData, successResult.data)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "GET_DATA_USER" && event.extras["user_id"] == "1234"
            })
        }
    }

    @Test
    fun `given an invalid userId when getDataUser is called then returns Loading and Error states`() = runTest {
        //Given
        val invalidUserId = "userError"
        val expectedErrorStringRes = R.string.error_firestore_default
        val task: Task<DocumentSnapshot> = mockk()

        //When
        every { collectionReference.document(invalidUserId) } returns documentReference
        every { documentReference.get() } returns task

        coEvery { task.isComplete } returns true
        coEvery { task.isCanceled } returns false
        coEvery { task.result } throws Exception("Firestore connection error")
        coEvery { task.exception } returns Exception("Firestore connection error")

        //Then
        firestoreManager.getDataUser(invalidUserId).test {

            assertEquals(AuthResponseFirestore.Loading, awaitItem())

            val errorResult = awaitItem() as AuthResponseFirestore.Error
            assertEquals(expectedErrorStringRes, errorResult.message)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "ERROR" || event.extras.toString().contains("Firestore connection error")
            })
        }
    }

    @Test
    fun `given a valid userId and data when saveDataUser is called then returns Loading and Success states`() = runTest {
        //Given
        val userId = "1234"
        val fireStoreData = FirestoreData(habit = "Habit1", date= LocalDate.now().toString())
        val expectedData = null
        val task: Task<Void?> = mockk()

        //When
        every { collectionReference.document(userId) } returns documentReference
        every { documentReference.set(fireStoreData) } returns task

        coEvery { task.isComplete } returns true
        coEvery { task.isCanceled } returns false
        coEvery { task.result } returns null
        coEvery { task.exception } returns null

        //Then
        firestoreManager.saveDataUser(fireStoreData,userId).test {

            assertEquals(AuthResponseFirestore.Loading, awaitItem())

            val successResult = awaitItem() as AuthResponseFirestore.Success
            assertEquals(expectedData, successResult.data)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "SAVE_DATA_USER" && event.extras["user_id"] == "1234"
            })
        }
    }

    @Test
    fun `given a invalid userId and data when saveDataUser is called then returns Loading and Success states`() = runTest {
        //Given
        val invalidUserId = "1234"
        val invalidFireStoreData = FirestoreData(habit = "Habit1", date= LocalDate.now().toString())
        val expectedData = R.string.error_firestore_default
        val task: Task<Void?> = mockk()

        //When
        every { collectionReference.document(invalidUserId) } returns documentReference
        every { documentReference.set(invalidFireStoreData) } returns task

        coEvery { task.isComplete } returns true
        coEvery { task.isCanceled } returns false
        coEvery { task.result } throws Exception("Firestore connection error")
        coEvery { task.exception } returns Exception("Firestore connection error")

        //Then
        firestoreManager.saveDataUser(invalidFireStoreData,invalidUserId).test {

            assertEquals(AuthResponseFirestore.Loading, awaitItem())

            val errorResult = awaitItem() as AuthResponseFirestore.Error
            assertEquals(expectedData, errorResult.message)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "ERROR" || event.extras.toString().contains("Firestore connection error")
            })
        }
    }

    @Test
    fun `given a valid userId when deleteDataUser is called then returns Loading and Success states`() = runTest {
        //Given
        val idUser = "1234"
        val expectedData = null
        val task: Task<Void?> = mockk()

        //Then
        every { collectionReference.document(idUser) } returns documentReference
        every { documentReference.delete() } returns task

        coEvery { task.isComplete } returns true
        coEvery { task.isCanceled } returns false
        coEvery { task.result } returns null
        coEvery { task.exception } returns null

        //Then
        firestoreManager.deleteDataUser(idUser).test {

            assertEquals(AuthResponseFirestore.Loading, awaitItem())

            val successResult = awaitItem() as AuthResponseFirestore.Success
            assertEquals(expectedData, successResult.data)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "DELETE_DATA_USER" && event.extras["user_id"] == "1234"
            })
        }
    }

    @Test
    fun `given a invalid userId when deleteDataUser is called then returns Loading and Success states`() = runTest {
        //Given
        val invalidUserId = "1234"
        val expectedData = R.string.error_firestore_default
        val task: Task<Void?> = mockk()

        //Then
        every { collectionReference.document(invalidUserId) } returns documentReference
        every { documentReference.delete() } returns task

        coEvery { task.isComplete } returns true
        coEvery { task.isCanceled } returns false
        coEvery { task.result } throws Exception("Firestore connection error")
        coEvery { task.exception } returns Exception("Firestore connection error")

        //Then
        firestoreManager.deleteDataUser(invalidUserId).test {

            assertEquals(AuthResponseFirestore.Loading, awaitItem())

            val errorResult = awaitItem() as AuthResponseFirestore.Error
            assertEquals(expectedData, errorResult.message)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            analyticsManager.logEvent(match { event ->
                event.type.name == "ERROR" || event.extras.toString().contains("Firestore connection error")
            })
        }
    }
}