package aeb.proyecto.alarmmanager

import aeb.proyecto.alarmmanager.gsonProvider.GsonProvider
import aeb.proyecto.alarmmanager.service.AlarmService
import aeb.proyecto.room.model.NotificationWithNameAndColor
import aeb.proyecto.room.model.classes.TypeNotification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowNotificationManager
import java.time.LocalTime

@RunWith(RobolectricTestRunner::class)
class AlarmServiceTest {

    @get:Rule
    val mainDispatchersRule = MainDispatcherRule()

    private lateinit var context: Context
    private lateinit var shadowNotificationManager: ShadowNotificationManager

    // Mockeamos la utilidad que reprograma la alarma repetida
    private val notificationUtils: NotificationUtils = mockk(relaxed = true)

    @Before
    fun setup() {
        context = spyk(RuntimeEnvironment.getApplication())

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        shadowNotificationManager = shadowOf(notificationManager)

        every { context.getString(R.string.notification_subtitle, any()) } returns "Recordatorio de hábito"

        mockkStatic(ContextCompat::class)
        every { ContextCompat.checkSelfPermission(any(), "android.permission.POST_NOTIFICATIONS") } returns PackageManager.PERMISSION_GRANTED
    }

    @After
    fun teardown() {
        unmockkStatic(ContextCompat::class)
    }

    @Test
    fun `given valid intent payload when onReceive is called then triggers notification and reschedules alarm`() {
        // --- GIVEN ---
        val mockNotificationData = NotificationWithNameAndColor(
            id = 99L,
            name = "Beber Agua",
            color = 0xFF42A5F5.toInt(),
            time = LocalTime.now(),
            typeNotification = TypeNotification.Daily()
        )
        val rawJson = GsonProvider.gson.toJson(mockNotificationData)

        val incomingIntent = Intent().apply {
            putExtra("REMINDER", rawJson) // Asegúrate de que "REMINDER" coincide con tu constante REMINDER
        }

        val alarmService = AlarmService()
        alarmService.notificationUtils = notificationUtils

        // --- WHEN ---
        alarmService.onReceive(context, incomingIntent)

        // --- THEN ---
        val notification = shadowNotificationManager.getNotification(99)
        assertNotNull("La notificación debería haberse publicado", notification)

        val shadowNotification = shadowOf(notification)
        assertEquals("Beber Agua", shadowNotification.contentTitle)

        verify(exactly = 1) {
            notificationUtils.setRepeatedAlarm(match { data ->
                data.id == 99L && data.name == "Beber Agua"
            })
        }
    }

}