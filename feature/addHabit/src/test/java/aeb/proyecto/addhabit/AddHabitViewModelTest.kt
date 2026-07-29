package aeb.proyecto.addhabit

import aeb.proyecto.addhabit.constants.GridOption
import aeb.proyecto.addhabit.constants.GridOptionResult
import aeb.proyecto.addhabit.constants.PICK_NOTIFICATION
import aeb.proyecto.addhabit.constants.TypeHabit
import aeb.proyecto.addhabit.constants.TypeNotificationResult
import aeb.proyecto.addhabit.model.AddHabitNotification
import aeb.proyecto.addhabit.model.BottomSheetState
import aeb.proyecto.addhabit.model.DataAddHabitScreen
import aeb.proyecto.addhabit.model.DataBottomSheet
import aeb.proyecto.domain.usecase.addHabit.DataStoreAddHabitUseCase
import aeb.proyecto.domain.usecase.addHabit.RoomRepositoryAddHabitUseCase
import aeb.proyecto.domain.usecase.addHabit.SetNotificationAddHabitUseCase
import aeb.proyecto.room.entities.HabitNotification
import aeb.proyecto.room.entities.relations.HabitWithNotification
import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.model.classes.TypeNotification
import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.room.model.classes.UnitType
import aeb.proyecto.ui.constants.getContrastColor
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.ui.graphics.Color
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class AddHabitViewModelTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private val roomRepositoryAddHabitUseCase = mockk<RoomRepositoryAddHabitUseCase>(relaxed = true)
    private val setNotificationAddHabitUseCase = mockk<SetNotificationAddHabitUseCase>(relaxed = true)
    private val dataStoreAddHabitUseCase = mockk<DataStoreAddHabitUseCase>(relaxed = true)

    private lateinit var viewModel: AddHabitViewModel

     @Before
     fun setUp(){
         viewModel = AddHabitViewModel(roomRepositoryAddHabitUseCase,setNotificationAddHabitUseCase,dataStoreAddHabitUseCase)
     }

    @Test
    fun `given viewModel initialized then initial states are correct`() {
        // --- GIVEN & WHEN ---
        val initialData = viewModel.dataAddHabit.value
        val initialUIState = viewModel.addHabitUIState.value

        // --- THEN ---
        assertEquals(DataAddHabitScreen().habitScreen.id, initialData.habitScreen.id)
        assertEquals(AddHabitUIState.Success, initialUIState)
    }

    @Test
    fun `given colorResult when onClickGridOption invoked then updates color contrast and collapses picker`() {
        // --- GIVEN ---
        val selectedColor = Color.Red
        val result = GridOptionResult.colorResult(color = selectedColor)

        // --- WHEN ---
        viewModel.onClickGridOption(result)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(selectedColor, currentState.habitScreen.color)
        assertEquals(getContrastColor(selectedColor), currentState.contrastColor)
        assertFalse(currentState.isColorSelected)
    }

    @Test
    fun `given iconResult when onClickGridOption invoked then updates icon in state`() {
        // --- GIVEN ---
        val selectedIcon = Icons.Default.FitnessCenter
        val result = GridOptionResult.iconResult(icon = selectedIcon)

        // --- WHEN ---
        viewModel.onClickGridOption(result)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(selectedIcon, currentState.habitScreen.icon)
    }

    @Test
    fun `given gridOption COLORS when onClickCard invoked then executes colorGridState`() {
        // --- GIVEN ---
        val option = GridOption.COLORS

        // --- WHEN ---
        viewModel.onClickCard(option)

        // --- THEN ---
        assertTrue(viewModel.dataAddHabit.value.isColorSelected)
    }

    @Test
    fun `given gridOption ICONS when onClickCard invoked then executes iconGridState`() {
        // --- GIVEN ---
        val option = GridOption.ICONS

        // --- WHEN ---
        viewModel.onClickCard(option)

        // --- THEN ---
        assertTrue(viewModel.dataAddHabit.value.isIconSelected)
    }

    @Test
    fun `given iconResult when onClickGridOption invoked then updates icon and collapses icon picker`() {
        // --- GIVEN ---
        val selectedIcon = Icons.Default.FitnessCenter
        val result = GridOptionResult.iconResult(icon = selectedIcon)

        // --- WHEN ---
        viewModel.onClickGridOption(result)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(selectedIcon, currentState.habitScreen.icon)
        assertFalse(currentState.isIconSelected)
    }

    @Test
    fun `given colorGridState toggled when onClickCard COLORS invoked then toggles isColorSelected and forces isIconSelected to false`() {
        // --- GIVEN ---
        val initialColorSelected = viewModel.dataAddHabit.value.isColorSelected

        // --- WHEN ---
        viewModel.onClickCard(GridOption.COLORS)

        // --- THEN ---
        val stateAfterFirstClick = viewModel.dataAddHabit.value
        assertEquals(!initialColorSelected, stateAfterFirstClick.isColorSelected)
        assertFalse(stateAfterFirstClick.isIconSelected)

        // --- WHEN
        viewModel.onClickCard(GridOption.COLORS)

        // --- THEN ---
        val stateAfterSecondClick = viewModel.dataAddHabit.value
        assertEquals(initialColorSelected, stateAfterSecondClick.isColorSelected)
        assertFalse(stateAfterSecondClick.isIconSelected)
    }

    @Test
    fun `given iconGridState toggled when onClickCard ICONS invoked then toggles isIconSelected and forces isColorSelected to false`() {
        // --- GIVEN ---
        val initialIconSelected = viewModel.dataAddHabit.value.isIconSelected

        // --- WHEN ---
        viewModel.onClickCard(GridOption.ICONS)

        // --- THEN ---
        val stateAfterFirstClick = viewModel.dataAddHabit.value
        assertEquals(!initialIconSelected, stateAfterFirstClick.isIconSelected)
        assertFalse(stateAfterFirstClick.isColorSelected)

        // --- WHEN (Toggle de vuelta) ---
        viewModel.onClickCard(GridOption.ICONS)

        // --- THEN ---
        val stateAfterSecondClick = viewModel.dataAddHabit.value
        assertEquals(initialIconSelected, stateAfterSecondClick.isIconSelected)
        assertFalse(stateAfterSecondClick.isColorSelected)
    }

    @Test
    fun `given dialogType when setDialog invoked then updates showDialog to true and sets typeDialog`() {
        // --- GIVEN ---
        val expectedTypeDialog = 2

        // --- WHEN ---
        viewModel.setDialog(expectedTypeDialog)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertTrue(currentState.showDialog)
        assertEquals(expectedTypeDialog, currentState.typeDialog)
    }

    @Test
    fun `given active dialog when closeDialog invoked then sets showDialog to false`() {
        // --- GIVEN ---
        viewModel.setDialog(1)
        assertTrue(viewModel.dataAddHabit.value.showDialog)

        // --- WHEN ---
        viewModel.closeDialog()

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertFalse(currentState.showDialog)
    }

    @Test
    fun `given typeHabit when onClickTypeHabit invoked then updates typeHabit in habitScreen`() {
        // --- GIVEN ---
        val expectedTypeHabit = TypeHabit.DAILY

        // --- WHEN ---
        viewModel.onClickTypeHabit(expectedTypeHabit)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(expectedTypeHabit, currentState.habitScreen.typeHabit)
    }

    @Test
    fun `given numberDays when onClickWeekly invoked then updates numberOfDaysWeek in habitScreen`() {
        // --- GIVEN ---
        val expectedDays = 4

        // --- WHEN ---
        viewModel.onClickWeekly(expectedDays)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(expectedDays, currentState.habitScreen.numberOfDaysWeek)
    }

    @Test
    fun `given numberDays when monthNumberSelected invoked then updates numberOfDaysMonth in habitScreen`() {
        // --- GIVEN ---
        val expectedDays = 15

        // --- WHEN ---
        viewModel.monthNumberSelected(expectedDays)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(expectedDays, currentState.habitScreen.numberOfDaysMonth)
    }

    @Test
    fun `given localDate when onClickDate invoked then updates dateRecurringStartDate in habitScreen`() {
        // --- GIVEN ---
        val expectedDate = LocalDate.of(2026, 8, 1)

        // --- WHEN ---
        viewModel.onClickDate(expectedDate)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(expectedDate, currentState.habitScreen.dateRecurringStartDate)
    }

    @Test
    fun `given unitType TIME and decimal text when onPickUnit invoked then truncates decimals and sets unit`() {
        // --- GIVEN ---
        val initialDecimalText = "12.5"
        viewModel.dataAddHabit.value.habitScreen.numberTimesTextField = TextFieldState(initialText = initialDecimalText)

        val timeUnit = UnitHabit.TIMES

        // --- WHEN ---
        viewModel.onPickUnit(timeUnit)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(timeUnit, currentState.habitScreen.unit)
        assertEquals("12.5", currentState.habitScreen.numberTimesTextField.text.toString())
    }

    @Test
    fun `given unitType NOT_TIME when onPickUnit invoked then updates unit without modifying text`() {
        // --- GIVEN ---
        val initialDecimalText = "10.75"
        viewModel.dataAddHabit.value.habitScreen.numberTimesTextField = TextFieldState(initialText = initialDecimalText)

        val countUnit = UnitHabit.TIMES

        // --- WHEN ---
        viewModel.onPickUnit(countUnit)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(countUnit, currentState.habitScreen.unit)
        assertEquals("10.75", currentState.habitScreen.numberTimesTextField.text.toString())
    }

    @Test
    fun `given typeNotification when onClickTypeNotification invoked then configures notificationSelected shows dialog and sets PICK_NOTIFICATION`() {
        // --- GIVEN ---
        val expectedType = TypeNotification.Daily()

        // --- WHEN ---
        viewModel.onClickTypeNotification(expectedType)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(expectedType, currentState.notificationSelected.type)
        assertNotNull(currentState.notificationSelected.time)
        assertTrue(currentState.showDialog)
        assertEquals(PICK_NOTIFICATION, currentState.typeDialog)
    }

    @Test
    fun `given idSelected is sentinel minus one when onTimeSelected invoked then appends new notification to list`() {
        // --- GIVEN ---
        val expectedType = TypeNotification.Daily()
        viewModel.onClickTypeNotification(expectedType)

        val initialNotificationsCount = viewModel.dataAddHabit.value.habitScreen.notifications.size
        val selectedTime = LocalTime.of(14, 30)

        // --- WHEN ---
        viewModel.onTimeSelected(selectedTime)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        val notifications = currentState.habitScreen.notifications

        assertEquals(initialNotificationsCount + 1, notifications.size)

        val lastNotification = notifications.last()
        assertEquals(selectedTime, lastNotification.time)
        assertEquals(expectedType, lastNotification.type)
    }

    @Test
    fun `given notification id when onClickDeleteNotification invoked then sets notificationSelected and shows delete bottomSheet`() {
        // --- GIVEN ---
        val targetId = "notification_id_123"

        // --- WHEN ---
        viewModel.onClickDeleteNotification(targetId)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(targetId, currentState.notificationSelected.id)
        assertTrue(currentState.bottomSheetState.isVisible)
        assertEquals(DataBottomSheet.DELETE_NOTIFICATION, currentState.bottomSheetState.dataBottomSheet)
    }

    @Test
    fun `given DELETE_NOTIFICATION bottomSheet when onAcceptBottomSheet invoked then deletes target notification`() {
        // --- GIVEN ---
        viewModel.onClickTypeNotification(TypeNotification.Daily())
        viewModel.onTimeSelected(LocalTime.of(10, 0))

        val createdNotification = viewModel.dataAddHabit.value.habitScreen.notifications.last()
        val targetId = createdNotification.id

        viewModel.onClickDeleteNotification(targetId)

        // --- WHEN ---
        viewModel.onAcceptBottomSheet()

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        val notificationExists = currentState.habitScreen.notifications.any { it.id == targetId }

        assertFalse(notificationExists)
    }

    @Test
    fun `given daily notification when toggling a new day then adds day to list`() {
        // --- GIVEN ---
        val targetId = "daily_notif_1"
        val initialTime = LocalTime.of(8, 0)
        val initialType = TypeNotification.Daily(days = mutableListOf(DayOfWeek.MONDAY))

        viewModel.onClickTypeNotification(initialType)
        viewModel.onTimeSelected(initialTime)

        val createdNotif = viewModel.dataAddHabit.value.habitScreen.notifications.last()
        val createdId = createdNotif.id

        val resultPayload = TypeNotificationResult.Daily(id = createdId, day = DayOfWeek.TUESDAY)

        // --- WHEN ---
        viewModel.onClickTypeNotificationResult(resultPayload)

        // --- THEN ---
        val updatedNotif = viewModel.dataAddHabit.value.habitScreen.notifications.find { it.id == createdId }
        val updatedDays = (updatedNotif?.type as? TypeNotification.Daily)?.days

        assertNotNull(updatedDays)
        assertEquals(2, updatedDays?.size)
        assertTrue(updatedDays?.contains(DayOfWeek.MONDAY) == true)
        assertTrue(updatedDays?.contains(DayOfWeek.TUESDAY) == true)
    }

    @Test
    fun `given daily notification with multiple days when toggling existing day then removes day from list`() {
        // --- GIVEN ---
        val initialType = TypeNotification.Daily(days = mutableListOf(DayOfWeek.MONDAY, DayOfWeek.FRIDAY))

        viewModel.onClickTypeNotification(initialType)
        viewModel.onTimeSelected(LocalTime.of(9, 0))

        val createdNotif = viewModel.dataAddHabit.value.habitScreen.notifications.last()
        val createdId = createdNotif.id

        val resultPayload = TypeNotificationResult.Daily(id = createdId, day = DayOfWeek.FRIDAY)

        // --- WHEN ---
        viewModel.onClickTypeNotificationResult(resultPayload)

        // --- THEN ---
        val updatedNotif = viewModel.dataAddHabit.value.habitScreen.notifications.find { it.id == createdId }
        val updatedDays = (updatedNotif?.type as? TypeNotification.Daily)?.days

        assertNotNull(updatedDays)
        assertEquals(1, updatedDays?.size)
        assertFalse(updatedDays?.contains(DayOfWeek.FRIDAY) == true)
        assertTrue(updatedDays?.contains(DayOfWeek.MONDAY) == true)
    }

    @Test
    fun `given daily notification with only one day when toggling same day then guard prevents empty days list`() {
        // --- GIVEN ---
        val initialType = TypeNotification.Daily(days = mutableListOf(DayOfWeek.WEDNESDAY))

        viewModel.onClickTypeNotification(initialType)
        viewModel.onTimeSelected(LocalTime.of(10, 0))

        val createdNotif = viewModel.dataAddHabit.value.habitScreen.notifications.last()
        val createdId = createdNotif.id

        // Intentamos quitar el único día presente
        val resultPayload = TypeNotificationResult.Daily(id = createdId, day = DayOfWeek.WEDNESDAY)

        // --- WHEN ---
        viewModel.onClickTypeNotificationResult(resultPayload)

        // --- THEN ---
        val updatedNotif = viewModel.dataAddHabit.value.habitScreen.notifications.find { it.id == createdId }
        val updatedDays = (updatedNotif?.type as? TypeNotification.Daily)?.days

        assertNotNull(updatedDays)
        assertEquals(1, updatedDays?.size)
        assertTrue(updatedDays?.contains(DayOfWeek.WEDNESDAY) == true)
    }

    @Test
    fun `given recurring notification when action is true then increments interval days`() {
        // --- GIVEN ---
        val initialType = TypeNotification.Recurring(interval = 2)

        viewModel.onClickTypeNotification(initialType)
        viewModel.onTimeSelected(LocalTime.of(7, 30))

        val createdNotif = viewModel.dataAddHabit.value.habitScreen.notifications.last()
        val createdId = createdNotif.id

        val resultPayload = TypeNotificationResult.Recurring(id = createdId, action = true) // true -> Incrementar

        // --- WHEN ---
        viewModel.onClickTypeNotificationResult(resultPayload)

        // --- THEN ---
        val updatedNotif = viewModel.dataAddHabit.value.habitScreen.notifications.find { it.id == createdId }
        val updatedInterval = (updatedNotif?.type as? TypeNotification.Recurring)?.interval

        assertEquals(3, updatedInterval)
    }

    @Test
    fun `given recurring notification interval greater than 1 when action is false then decrements interval days`() {
        // --- GIVEN ---
        val initialType = TypeNotification.Recurring(interval = 4)

        viewModel.onClickTypeNotification(initialType)
        viewModel.onTimeSelected(LocalTime.of(20, 0))

        val createdNotif = viewModel.dataAddHabit.value.habitScreen.notifications.last()
        val createdId = createdNotif.id

        val resultPayload = TypeNotificationResult.Recurring(id = createdId, action = false)

        // --- WHEN ---
        viewModel.onClickTypeNotificationResult(resultPayload)

        // --- THEN ---
        val updatedNotif = viewModel.dataAddHabit.value.habitScreen.notifications.find { it.id == createdId }
        val updatedInterval = (updatedNotif?.type as? TypeNotification.Recurring)?.interval

        assertEquals(3, updatedInterval)
    }

    @Test
    fun `given recurring notification interval is 1 when action is false then guard prevents decrement below 1`() {
        // --- GIVEN ---
        val initialType = TypeNotification.Recurring(interval = 1)

        viewModel.onClickTypeNotification(initialType)
        viewModel.onTimeSelected(LocalTime.of(12, 0))

        val createdNotif = viewModel.dataAddHabit.value.habitScreen.notifications.last()
        val createdId = createdNotif.id

        val resultPayload = TypeNotificationResult.Recurring(id = createdId, action = false)

        // --- WHEN ---
        viewModel.onClickTypeNotificationResult(resultPayload)

        // --- THEN ---
        val updatedNotif = viewModel.dataAddHabit.value.habitScreen.notifications.find { it.id == createdId }
        val updatedInterval = (updatedNotif?.type as? TypeNotification.Recurring)?.interval

        assertEquals(1, updatedInterval)
    }

    @Test
    fun `given id and time when onEditNotification invoked then updates notificationSelected and shows PICK_NOTIFICATION_dialog`() {
        // --- GIVEN ---
        val targetId = "notification_456"
        val expectedTime = LocalTime.of(16, 45)

        // --- WHEN ---
        viewModel.onEditNotification(targetId, expectedTime)

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(targetId, currentState.notificationSelected.id)
        assertEquals(expectedTime, currentState.notificationSelected.time)
        assertTrue(currentState.showDialog)
        assertEquals(PICK_NOTIFICATION, currentState.typeDialog)
    }

    @Test
    fun `given invalid title name when saveData invoked then shows ERROR_NAME_UNIT bottomSheet`() {
        // --- GIVEN ---

        // --- WHEN ---
        viewModel.saveData()

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertTrue(currentState.bottomSheetState.isVisible)
        assertEquals(DataBottomSheet.ERROR_NAME_UNIT, currentState.bottomSheetState.dataBottomSheet)
    }

    @Test
    fun `given id minus one when getData invoked then fetches dayStartWeek and does not query room`() = runTest {
        // --- GIVEN ---
        val expectedDayOfWeek = DayOfWeek.MONDAY
        coEvery { dataStoreAddHabitUseCase.getDayOfWeek() } returns expectedDayOfWeek

        // --- WHEN ---
        viewModel.getData(id = -1L).join()

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(expectedDayOfWeek, currentState.dayStartWeek)

        coVerify(exactly = 0) { roomRepositoryAddHabitUseCase.getHabitById(any()) }
        assertEquals(AddHabitUIState.Success, viewModel.addHabitUIState.value)
    }

    @Test
    fun `given valid habit id when getData invoked for first time then loads habit and emits Success`() = runTest {
        // --- GIVEN ---
        val targetHabitId = 42L
        val expectedDayOfWeek = DayOfWeek.SUNDAY
        val mockHabit = mockk<HabitWithNotification>(relaxed = true)

        coEvery { dataStoreAddHabitUseCase.getDayOfWeek() } returns expectedDayOfWeek
        coEvery { roomRepositoryAddHabitUseCase.getHabitById(targetHabitId) } returns mockHabit

        // --- WHEN ---
        viewModel.getData(id = targetHabitId).join()

        // --- THEN ---
        val currentState = viewModel.dataAddHabit.value
        assertEquals(expectedDayOfWeek, currentState.dayStartWeek)
        assertEquals(AddHabitUIState.Success, viewModel.addHabitUIState.value)

        coVerify(exactly = 1) { roomRepositoryAddHabitUseCase.getHabitById(targetHabitId) }
    }

    @Test
    fun `given data already searched when getData invoked again then guard prevents redundant room queries`() = runTest {
        // --- GIVEN ---
        val targetHabitId = 10L
        val mockHabit = mockk<HabitWithNotification>(relaxed = true)

        coEvery { dataStoreAddHabitUseCase.getDayOfWeek() } returns DayOfWeek.MONDAY
        coEvery { roomRepositoryAddHabitUseCase.getHabitById(targetHabitId) } returns mockHabit

        viewModel.getData(id = targetHabitId).join()

        // --- WHEN
        viewModel.getData(id = targetHabitId).join()

        // --- THEN ---
        coVerify(exactly = 1) { roomRepositoryAddHabitUseCase.getHabitById(targetHabitId) }
    }

    @Test
    fun `given weeklyGoal is false when onCheckedWeeklyChange invoked then toggles weeklyGoal to true`() {
        // --- GIVEN ---
        val initialWeeklyGoal = viewModel.dataAddHabit.value.habitScreen.weeklyGoal

        // --- WHEN ---
        viewModel.onCheckedWeeklyChange()

        // --- THEN ---
        val updatedWeeklyGoal = viewModel.dataAddHabit.value.habitScreen.weeklyGoal
        assertEquals(!initialWeeklyGoal, updatedWeeklyGoal)
    }

    @Test
    fun `given weeklyGoal is true when onCheckedWeeklyChange invoked twice then reverts to initial value`() {
        // --- GIVEN ---
        val initialWeeklyGoal = viewModel.dataAddHabit.value.habitScreen.weeklyGoal

        // --- WHEN ---
        viewModel.onCheckedWeeklyChange() 
        viewModel.onCheckedWeeklyChange() 

        // --- THEN ---
        val finalWeeklyGoal = viewModel.dataAddHabit.value.habitScreen.weeklyGoal
        assertEquals(initialWeeklyGoal, finalWeeklyGoal)
    }

    @Test
    fun `given monthlyGoal is false when onCheckedMonthlyChange invoked then toggles monthlyGoal to true`() {
        // --- GIVEN ---
        val initialMonthlyGoal = viewModel.dataAddHabit.value.habitScreen.monthlyGoal

        // --- WHEN ---
        viewModel.onCheckedMonthlyChange()

        // --- THEN ---
        val updatedMonthlyGoal = viewModel.dataAddHabit.value.habitScreen.monthlyGoal
        assertEquals(!initialMonthlyGoal, updatedMonthlyGoal)
    }

    @Test
    fun `given monthlyGoal is true when onCheckedMonthlyChange invoked twice then reverts to initial value`() {
        // --- GIVEN ---
        val initialMonthlyGoal = viewModel.dataAddHabit.value.habitScreen.monthlyGoal

        // --- WHEN ---
        viewModel.onCheckedMonthlyChange()
        viewModel.onCheckedMonthlyChange() 

        // --- THEN ---
        val finalMonthlyGoal = viewModel.dataAddHabit.value.habitScreen.monthlyGoal
        assertEquals(initialMonthlyGoal, finalMonthlyGoal)
    }
}