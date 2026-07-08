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

        val zoneId = java.time.ZoneId.systemDefault()
        val fixedInstant = java.time.ZonedDateTime.of(
            2026, 7, 8, 10, 0, 0, 0, zoneId
        ).toInstant()

        val fixedClock = java.time.Clock.fixed(fixedInstant, zoneId)

        notificationUtils = NotificationUtils(context, fixedClock)
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
        val zoneId = java.time.ZoneId.systemDefault()

        // 🗓️ Fijamos el "Ahora" del test usando java.time: Hoy a las 16:00
        // Usamos el año/mes/día actual, pero forzamos que sean las 16:00
        val nowToday = java.time.LocalDate.now()
        val fixedInstant = java.time.LocalDateTime.of(nowToday, java.time.LocalTime.of(16, 0))
            .atZone(zoneId)
            .toInstant()

        // Configuramos el reloj estático para este test y actualizamos la instancia
        val fixedClock = java.time.Clock.fixed(fixedInstant, zoneId)
        notificationUtils = NotificationUtils(context, fixedClock)

        val pastTime = java.time.LocalTime.of(14, 0) // La alarma era a las 14:00 (ya pasó respecto a las 16:00)
        val intervalDays = 2

        val alarmItem = NotificationWithNameAndColor(
            id = 456L,
            name = "Anotar hábitos",
            color = 0x00FF00,
            time = pastTime,
            typeNotification = TypeNotification.Recurring(interval = intervalDays)
        )

        // Calculamos el tiempo esperado usando la misma API moderna:
        // Al haber pasado la hora hoy (14:00 < 16:00), tu código le sumará los días de intervalo
        val expectedTimeInMillis = java.time.LocalDateTime.ofInstant(fixedInstant, zoneId)
            .with(pastTime)             // Ponemos las 14:00
            .plusDays(intervalDays.toLong()) // Le sumamos los 2 días de intervalo
            .atZone(zoneId)
            .toInstant()
            .toEpochMilli()

        // --- WHEN ---
        notificationUtils.setUpAlarm(alarmItem)

        // --- THEN ---
        val scheduledAlarm = shadowAlarmManager.peekNextScheduledAlarm()
        assertNotNull("La alarma debería haberse programada en el futuro", scheduledAlarm)

        assertEquals(
            expectedTimeInMillis.toDouble(),
            scheduledAlarm?.triggerAtTime?.toDouble() ?: 0.0,
            1000.0
        )
    }

    @Test
    fun `given daily type notification in the future when setUpAlarm is called then schedules alarm at exact same day time`() {
        // --- GIVEN ---
        val zoneId = java.time.ZoneId.systemDefault()

        val fixedInstant = java.time.ZonedDateTime.of(
            2026, 7, 7, 10, 0, 0, 0, zoneId
        ).toInstant()

        // Configuramos nuestro reloj inyectado para congelar el tiempo del test
        val fixedClock = java.time.Clock.fixed(fixedInstant, zoneId)
        notificationUtils = NotificationUtils(context, fixedClock)

        val futureTime = java.time.LocalTime.of(23, 59)
        val alarmItem = NotificationWithNameAndColor(
            id = 123L,
            name = "Ir al gimnasio",
            color = 0xFF0000,
            time = futureTime,
            typeNotification = TypeNotification.Daily(days = listOf(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.TUESDAY))
        )

        val expectedTimeInMillis = java.time.LocalDateTime.ofInstant(fixedInstant, zoneId)
            .with(futureTime) // Cambiamos la hora a las 23:59
            .atZone(zoneId)
            .toInstant()
            .toEpochMilli()

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
        val zoneId = java.time.ZoneId.systemDefault()

        val fixedInstant = java.time.ZonedDateTime.of(
            2026, 7, 7, 10, 0, 0, 0, zoneId
        ).toInstant()

        val fixedClock = java.time.Clock.fixed(fixedInstant, zoneId)
        notificationUtils = NotificationUtils(context, fixedClock)

        val alarmItem = NotificationWithNameAndColor(
            id = 789L,
            name = "Estudiar Ingeniería",
            color = 0x9C27B0,
            time = java.time.LocalTime.of(18, 0),
            typeNotification = TypeNotification.Daily(
                days = listOf(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
            )
        )

        val expectedTimeInMillis = java.time.LocalDateTime.ofInstant(fixedInstant, zoneId)
            .plusDays(2) // Desplazamos los 2 días del salto (Martes a Jueves)
            .with(alarmItem.time) // Ponemos las 18:00
            .atZone(zoneId)
            .toInstant()
            .toEpochMilli()

        // --- WHEN ---
        notificationUtils.setRepeatedAlarm(alarmItem)

        // --- THEN ---
        val scheduledAlarm = shadowAlarmManager.peekNextScheduledAlarm()
        assertNotNull("Debería haberse programado la alarma de repetición", scheduledAlarm)

        assertEquals(AlarmManager.RTC_WAKEUP, scheduledAlarm?.type)

        assertEquals(
            expectedTimeInMillis.toDouble(),
            scheduledAlarm?.triggerAtTime?.toDouble() ?: 0.0,
            1000.0
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