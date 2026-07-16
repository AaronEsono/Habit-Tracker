package aeb.proyecto.save

import aeb.proyecto.domain.usecase.save.SaveAuthenticationUseCase
import aeb.proyecto.domain.usecase.save.SaveFirestoreUseCase
import aeb.proyecto.domain.usecase.save.SaveHabitsRepositoryUseCase
import aeb.proyecto.domain.usecase.save.SaveNotificationUseCase
import aeb.proyecto.firestore.AuthResponseFirestore
import aeb.proyecto.firestore.model.FirestoreData
import aeb.proyecto.save.model.DataBottomSheet
import aeb.proyecto.save.model.DataSaveScreen
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class SaveViewModelTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val mockSaveHabitsRepositoryUseCase = mockk<SaveHabitsRepositoryUseCase>(relaxed = true)
    private val mockSaveNotificationUseCase = mockk<SaveNotificationUseCase>(relaxed = true)
    private val mockSaveFirestoreUseCase = mockk<SaveFirestoreUseCase>(relaxed = true)
    private val mockSaveAuthenticationUseCase = mockk<SaveAuthenticationUseCase>(relaxed = true)

    private lateinit var viewModel: SaveViewModel

    @Before
    fun setUp() {
        viewModel = SaveViewModel(
            saveHabitsRepositoryUseCase = mockSaveHabitsRepositoryUseCase,
            saveNotificationUseCase = mockSaveNotificationUseCase,
            saveFirestoreUseCase = mockSaveFirestoreUseCase,
            saveAuthenticationUseCase = mockSaveAuthenticationUseCase
        )
    }

    @Test
    fun `given viewModel, when initialized, then verify default states`() {
        assertFalse(viewModel.bottomSheetState.value.showBottomSheet)

        assertEquals(SaveUIState.Loading, viewModel.saveUIState.value)

        assertEquals(DataSaveScreen(), viewModel.dataSaveScreen.value)
    }

    @Test
    fun `given closed bottom sheet, when setBottomSheetState is called, then show bottom sheet with correct data`() {
        // --- GIVEN ---
        val fakeDataBottomSheet = DataBottomSheet.SAVE_HABIT
        // --- WHEN ---
        viewModel.setBottomSheetState(fakeDataBottomSheet)

        // --- THEN ---
        val currentState = viewModel.bottomSheetState.value
        assertTrue(currentState.showBottomSheet)
        assertEquals(fakeDataBottomSheet, currentState.dataBottomSheet)
    }

    @Test
    fun `given open bottom sheet, when closeBottomSheet is called, then hide bottom sheet`() {
        // --- GIVEN ---
        val fakeDataBottomSheet = DataBottomSheet.SAVE_HABIT
        viewModel.setBottomSheetState(fakeDataBottomSheet)
        assertTrue(viewModel.bottomSheetState.value.showBottomSheet)

        // --- WHEN ---
        viewModel.closeBottomSheet()

        // --- THEN ---
        assertFalse(viewModel.bottomSheetState.value.showBottomSheet)
        assertEquals(fakeDataBottomSheet, viewModel.bottomSheetState.value.dataBottomSheet)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given authentication and firestore success, when getDataUser is called, then update state to Success and save data`() = runTest {
        // --- GIVEN ---
        val userId = "user123"
        val userName = "John Doe"
        val targetDateString = "2026-07-16T13:45:00"
        val expectedDate = LocalDateTime.parse(targetDateString)

        coEvery { mockSaveAuthenticationUseCase.getCurrentId() } returns userId
        coEvery { mockSaveAuthenticationUseCase.getName() } returns userName

        val mockUserFirestoreData = FirestoreData(date = targetDateString)
        val successResponse = AuthResponseFirestore.Success(mockUserFirestoreData)

        coEvery { mockSaveFirestoreUseCase.getDataUser(userId) } returns flowOf(successResponse)

        // --- WHEN ---
        viewModel.getDataUser()
        runCurrent()

        // --- THEN ---
        assertEquals(SaveUIState.Success, viewModel.saveUIState.value)

        val currentScreenData = viewModel.dataSaveScreen.value
        assertEquals(expectedDate, currentScreenData.localDateTime)
        assertEquals(userName, currentScreenData.name)

        viewModel.getDataUser()
        runCurrent()

        coVerify(exactly = 1) { mockSaveFirestoreUseCase.getDataUser(userId) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given firestore returns error, when getDataUser is called, then update state to Error`() = runTest {
        // --- GIVEN ---
        val userId = "user123"
        coEvery { mockSaveAuthenticationUseCase.getCurrentId() } returns userId

        val errorResponse = AuthResponseFirestore.Error(1)
        coEvery { mockSaveFirestoreUseCase.getDataUser(userId) } returns flowOf(errorResponse)

        // --- WHEN ---
        viewModel.getDataUser()
        runCurrent()

        // --- THEN ---
        assertEquals(SaveUIState.Error, viewModel.saveUIState.value)
    }

    @Test
    fun `given user is authenticated, when deleteDataUser completes successfully, then update UI state to Success and reset localDateTime`() = runTest {
        // --- GIVEN ---
        val userId = "user123"
        coEvery { mockSaveAuthenticationUseCase.getCurrentId() } returns userId

        val successResponse = AuthResponseFirestore.Success(FirestoreData())
        coEvery {
            mockSaveFirestoreUseCase.deleteDataUser(userId)
        } returns flowOf(successResponse)

        viewModel.setBottomSheetState(DataBottomSheet.DELETE_HABIT)
        assertTrue(viewModel.bottomSheetState.value.showBottomSheet)

        // --- WHEN ---
        viewModel.requestAcceptBottomSheet()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(SaveUIState.Success, viewModel.saveUIState.value)

        assertEquals(null, viewModel.dataSaveScreen.value.localDateTime)

        val finalBottomSheetState = viewModel.bottomSheetState.value
        assertTrue(finalBottomSheetState.showBottomSheet)
        assertEquals(DataBottomSheet.DELETED_DATA, finalBottomSheetState.dataBottomSheet)

        coVerify(exactly = 1) { mockSaveAuthenticationUseCase.getCurrentId() }
        coVerify(exactly = 1) { mockSaveFirestoreUseCase.deleteDataUser(userId) }
    }

    @Test
    fun `given deleteDataUser is called, when Firestore returns error, then update UI state to Error`() = runTest {
        // --- GIVEN ---
        val userId = "user_error"
        coEvery { mockSaveAuthenticationUseCase.getCurrentId() } returns userId

        val errorResponse = AuthResponseFirestore.Error(1)
        coEvery {
            mockSaveFirestoreUseCase.deleteDataUser(userId)
        } returns flowOf(errorResponse)

        viewModel.setBottomSheetState(DataBottomSheet.DELETE_HABIT)

        // --- WHEN ---
        viewModel.requestAcceptBottomSheet()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(SaveUIState.Error, viewModel.saveUIState.value)
    }

    @Test
    fun `given deleteDataUser is called, when authentication throws exception, then catch exception and update state to Error`() = runTest {
        // --- GIVEN ---
        coEvery { mockSaveAuthenticationUseCase.getCurrentId() } throws RuntimeException("Auth failed on delete")

        viewModel.setBottomSheetState(DataBottomSheet.DELETE_HABIT)

        // --- WHEN ---
        viewModel.requestAcceptBottomSheet()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(SaveUIState.Error, viewModel.saveUIState.value)
    }

    @Test
    fun `when logOut is called, then close bottom sheet, update state to LogOut and call use case`() = runTest {
        // --- GIVEN ---
        coEvery { mockSaveAuthenticationUseCase.logOut() } returns Unit

        viewModel.setBottomSheetState(DataBottomSheet.LOG_OUT)
        assertTrue(viewModel.bottomSheetState.value.showBottomSheet)

        // --- WHEN ---
        viewModel.requestAcceptBottomSheet()
        advanceUntilIdle()

        // --- THEN ---
        assertEquals(SaveUIState.LogOut, viewModel.saveUIState.value)

        assertFalse(viewModel.bottomSheetState.value.showBottomSheet)

        coVerify(exactly = 1) { mockSaveAuthenticationUseCase.logOut() }
    }

    @Test
    fun `given an error resource ID, when treatError is executed, then configure bottom sheet with correct message and show it`() {
        // --- GIVEN ---
        val expectedErrorMessageId = R.string.save_error_generic

        // --- WHEN ---
        coEvery { mockSaveAuthenticationUseCase.getCurrentId() } throws RuntimeException("Forced error")

        viewModel.setBottomSheetState(DataBottomSheet.DELETE_HABIT)
        viewModel.requestAcceptBottomSheet()

        // --- THEN ---
        val finalBottomSheetState = viewModel.bottomSheetState.value

        assertTrue(finalBottomSheetState.showBottomSheet)
        assertEquals(DataBottomSheet.ERROR, finalBottomSheetState.dataBottomSheet)

        assertEquals(expectedErrorMessageId, finalBottomSheetState.dataBottomSheet.label)
    }
}