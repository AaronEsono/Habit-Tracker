package aeb.proyecto.alarmmanager

import aeb.proyecto.alarmmanager.service.AlarmService
import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.model.classes.TypeNotification
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlarmManager
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.Calendar

// IMPORTANTE --------------------------------------------------
//Pequeña fuga en los relojes, puede que no se seteen bien, mirar esto
// En un futuro, en el codigo, inyectar el clock, para no depender del localDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class NotificationUtilsTest {

    @get:Rule
    val mainDispatchersRule = MainDispatcherRule()

    private lateinit var context: Context
    private lateinit var shadowAlarmManager: ShadowAlarmManager
    private lateinit var notificationUtils: NotificationUtils

    @Before
    fun setup() {
        context = RuntimeEnvironment.getApplication()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        shadowAlarmManager = shadowOf(alarmManager)
        notificationUtils = NotificationUtils(context)
    }

    @Test
    fun `given recurring type notification in the future when setUpAlarm is called then schedules alarm at exact same day time`() {
        // --- GIVEN ---
        val futureTime = LocalTime.of(23, 59)
        val alarmItem = NotificationWithNameAndColor(
            id = 123L,
            name = "Ir al gimnasio",
            color = 0xFF0000,
            time = futureTime,
            typeNotification = TypeNotification.Recurring(interval = 2)
        )

        val expectedTimeInMillis = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
        }.timeInMillis

        // --- WHEN ---
        notificationUtils.setUpAlarm(alarmItem)

        // --- THEN ---
        val scheduledAlarm = shadowAlarmManager.peekNextScheduledAlarm()
        assertNotNull("Debería haber una alarma programada", scheduledAlarm)

        assertEquals(AlarmManager.RTC_WAKEUP, scheduledAlarm?.type)

        assertEquals(
            expectedTimeInMillis.toDouble(),
            scheduledAlarm?.triggerAtTime?.toDouble() ?: 0.0,
            1000.0
        )
    }

    @Test
    fun `given recurring notification in the past when setUpAlarm is called then schedules alarm shifting days forward by interval`() {
        // --- GIVEN ---
        val fixedCalendarNow = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 16)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        android.os.SystemClock.setCurrentTimeMillis(fixedCalendarNow.timeInMillis)

        val pastTime = LocalTime.of(14, 0)
        val intervalDays = 2

        val alarmItem = NotificationWithNameAndColor(
            id = 456L,
            name = "Anotar hábitos",
            color = 0x00FF00,
            time = pastTime,
            typeNotification = TypeNotification.Recurring(interval = intervalDays)
        )

        val expectedCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val oneDayMillis = 86400000L
        val expectedTimeInMillis = expectedCalendar.timeInMillis + (oneDayMillis * intervalDays)

        // --- WHEN ---
        notificationUtils.setUpAlarm(alarmItem)

        // --- THEN ---
        val scheduledAlarm = shadowAlarmManager.peekNextScheduledAlarm()
        assertNotNull("La alarma debería haberse programada en el futuro", scheduledAlarm)

        // Comprobamos que el cálculo de tu 'when' sumó los 2 días de intervalo correctamente
        assertEquals(
            expectedTimeInMillis.toDouble(),
            scheduledAlarm?.triggerAtTime?.toDouble() ?: 0.0,
            1000.0 // Margen de 1 segundo de tolerancia
        )
    }

    @Test
    fun `given daily type notification in the future when setUpAlarm is called then schedules alarm at exact same day time`() {
        // --- GIVEN ---
        val fixedCalendarNow = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2026)
            set(Calendar.MONTH, Calendar.JULY)
            set(Calendar.DAY_OF_MONTH, 7)
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        android.os.SystemClock.setCurrentTimeMillis(fixedCalendarNow.timeInMillis)

        val futureTime = LocalTime.of(23, 59)
        val alarmItem = NotificationWithNameAndColor(
            id = 123L,
            name = "Ir al gimnasio",
            color = 0xFF0000,
            time = futureTime,
            typeNotification = TypeNotification.Daily(days = listOf(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.TUESDAY))
        )

        // 2. CONSTRUIMOS EL TIEMPO ESPERADO EN EL MISMO DÍA CONGELADO:
        val expectedTimeInMillis = Calendar.getInstance().apply {
            timeInMillis = fixedCalendarNow.timeInMillis
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
        }.timeInMillis

        // --- WHEN ---
        notificationUtils.setUpAlarm(alarmItem)

        // --- THEN ---
        val scheduledAlarm = shadowAlarmManager.peekNextScheduledAlarm()
        assertNotNull("Debería haber una alarma programada", scheduledAlarm)

        assertEquals(AlarmManager.RTC_WAKEUP, scheduledAlarm?.type)

        assertEquals(
            expectedTimeInMillis.toDouble(),
            scheduledAlarm?.triggerAtTime?.toDouble() ?: 0.0,
            1000.0
        )
    }

    @Test
    fun `given daily type notification in the future when setUpAlarm is called then schedules alarm to the next day`() {
        // --- GIVEN ---
        val fixedCalendarNow = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2026)
            set(Calendar.MONTH, Calendar.JULY)
            set(Calendar.DAY_OF_MONTH, 8) // Miércoles 8 de Julio
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        android.os.SystemClock.setCurrentTimeMillis(fixedCalendarNow.timeInMillis)

        val futureTime = LocalTime.of(23, 59)
        val alarmItem = NotificationWithNameAndColor(
            id = 123L,
            name = "Ir al gimnasio",
            color = 0xFF0000,
            time = futureTime,
            typeNotification = TypeNotification.Daily(days = listOf(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY))
        )

        val daysToWait = 2L
        val instant = java.time.Instant.ofEpochMilli(fixedCalendarNow.timeInMillis)
        val zoneId = java.time.ZoneId.systemDefault()
        val localDateTimeNow = java.time.LocalDateTime.ofInstant(instant, zoneId)

        val expectedTimeInMillis = localDateTimeNow
            .plusDays(daysToWait)
            .with(futureTime)
            .atZone(zoneId)
            .toInstant()
            .toEpochMilli()

        // --- WHEN ---
        notificationUtils.setUpAlarm(alarmItem)

        // --- THEN ---
        val scheduledAlarm = shadowAlarmManager.peekNextScheduledAlarm()
        assertNotNull("Debería haber una alarma programada", scheduledAlarm)

        assertEquals(
            expectedTimeInMillis.toDouble(),
            scheduledAlarm?.triggerAtTime?.toDouble() ?: 0.0,
            1000.0
        )
    }

    @Test
    fun `given daily notification when setRepeatedAlarm is called then schedules next execution skipping days correctly`() {
        // --- GIVEN ---
        // Como hoy es , el día más cercano en el futuro de esta lista es THURSDAY (Jueves), que está a 2 días.
        val alarmItem = NotificationWithNameAndColor(
            id = 789L,
            name = "Estudiar Ingeniería",
            color = 0x9C27B0,
            time = LocalTime.of(18, 0), // A las 18:00
            typeNotification = TypeNotification.Daily(
                days = listOf(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
            )
        )

        val baseCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val oneDayMillis = 86400000L
        val daysToWait = 2L
        val expectedTimeInMillis = baseCalendar.timeInMillis + (oneDayMillis * daysToWait)

        // --- WHEN ---
        notificationUtils.setRepeatedAlarm(alarmItem)

        // --- THEN ---
        val scheduledAlarm = shadowAlarmManager.peekNextScheduledAlarm()
        assertNotNull("Debería haberse programado la alarma de repetición", scheduledAlarm)

        assertEquals(AlarmManager.RTC_WAKEUP, scheduledAlarm?.type)

        assertEquals(
            expectedTimeInMillis.toDouble(),
            scheduledAlarm?.triggerAtTime?.toDouble() ?: 0.0,
            1000.0 // Margen de tolerancia por si Calendar.getInstance() difiere por milisegundos
        )
    }

    @Test
    fun `given recurring notification when setRepeatedAlarm is called then schedules next execution skipping days correctly`() {
        // --- GIVEN ---
        // Como hoy es , el día más cercano en el futuro de esta lista es THURSDAY (Jueves), que está a 2 días.
        val alarmItem = NotificationWithNameAndColor(
            id = 789L,
            name = "Estudiar Ingeniería",
            color = 0x9C27B0,
            time = LocalTime.of(18, 0), // A las 18:00
            typeNotification = TypeNotification.Recurring(interval = 3)
        )

        val baseCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val oneDayMillis = 86400000L
        val daysToWait = 3
        val expectedTimeInMillis = baseCalendar.timeInMillis + (oneDayMillis * daysToWait)

        // --- WHEN ---
        notificationUtils.setRepeatedAlarm(alarmItem)

        // --- THEN ---
        val scheduledAlarm = shadowAlarmManager.peekNextScheduledAlarm()
        assertNotNull("Debería haberse programado la alarma de repetición", scheduledAlarm)

        assertEquals(AlarmManager.RTC_WAKEUP, scheduledAlarm?.type)

        assertEquals(
            expectedTimeInMillis.toDouble(),
            scheduledAlarm?.triggerAtTime?.toDouble() ?: 0.0,
            1000.0 // Margen de tolerancia por si Calendar.getInstance() difiere por milisegundos
        )
    }

    @Test
    fun `given an active alarm when cancelAlarm is called then removes it from AlarmManager`() {
        // --- GIVEN ---
        val alarmId = 999L

        val intent = Intent(context, AlarmService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 100000, // En el futuro
            pendingIntent
        )

        assertEquals(1, shadowAlarmManager.getScheduledAlarms().size)

        // --- WHEN ---
        notificationUtils.cancelAlarm(alarmId)

        // --- THEN ---
        val activeAlarms = shadowAlarmManager.getScheduledAlarms()
        assertTrue("La lista de alarmas debería estar vacía tras la cancelación", activeAlarms.isEmpty())
    }

    @Test
    fun `given today is Monday when next day is Wednesday then returns two days of offset`() {
        // --- GIVEN ---
        val today = DayOfWeek.MONDAY // Valor = 1
        val daysList = listOf(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY) // 3 y 5

        // --- WHEN ---
        val result = getNextDay(daysList, today)

        // --- THEN ---
        assertEquals(2, result)
    }

    @Test
    fun `given today is Friday and next day is Tuesday then returns four days crossing next week`() {
        // --- GIVEN ---
        val today = DayOfWeek.FRIDAY // Valor = 5
        val daysList = listOf(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY) // 2 y 6

        // --- WHEN ---
        val result = getNextDay(daysList, today)

        // --- THEN ---
        val singleTuesdayList = listOf(DayOfWeek.TUESDAY)
        val resultCrossWeek = getNextDay(singleTuesdayList, today)

        assertEquals(4, resultCrossWeek)
    }

    @Test
    fun `given an unsorted list of days when getNextDay is called then handles sorting internally`() {
        // --- GIVEN ---
        val today = DayOfWeek.WEDNESDAY // Valor = 3
        val unsortedList = listOf(DayOfWeek.SATURDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)

        // --- WHEN ---
        val result = getNextDay(unsortedList, today)

        // --- THEN ---
        assertEquals(1, result)
    }

}