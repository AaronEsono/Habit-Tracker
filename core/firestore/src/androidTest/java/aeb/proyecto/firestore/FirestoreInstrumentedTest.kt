package aeb.proyecto.firestore

import aeb.proyecto.analytics.AnalyticsManagerInterface
import aeb.proyecto.firestore.model.FirestoreData
import app.cash.turbine.test
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirestoreInstrumentedTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firestoreManager: FirestoreManager

    private val analyticsManager: AnalyticsManagerInterface = mockk(relaxed = true)

    @Before
    fun setup() {
        // 1. Forzamos a que Firebase use una configuración limpia sin persistencia en disco local
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()

        firestore = Firebase.firestore
        firestore.firestoreSettings = settings

        try {
            // 2. ¡LA CLAVE! Le decimos al SDK de Firebase que apunte al emulador de tu PC
            // "127.0.0.1" o "localhost" en el puerto 8080
            firestore.useEmulator("127.0.0.1", 8080)
        } catch (e: IllegalStateException) {
            // Firebase no permite llamar a useEmulator más de una vez en el mismo proceso.
            // Ponemos este catch por si se ejecutan varios tests seguidos para que no de error.
        }

        // 3. Instanciamos el manager REAL con el Firestore REAL (pero conectado al emulador)
        firestoreManager = FirestoreManager(firestore, analyticsManager)
    }

    @Test
    fun saveAndGetDataIntegrationTest() = runTest {
        // --- GIVEN ---
        val userId = "user_integration_test_123"
        val dataToSave = FirestoreData(habit = "Ir al gimnasio", date = "2026-07-03")

        // --- WHEN (Guardamos los datos de verdad en el emulador) ---
        firestoreManager.saveDataUser(dataToSave, userId).test {
            TestCase.assertEquals(AuthResponseFirestore.Loading, awaitItem())

            val successResult = awaitItem() as AuthResponseFirestore.Success
            // Verificamos que devuelve el Success(null) que programaste
            TestCase.assertEquals(null, successResult.data)
            awaitComplete()
        }

        // --- THEN (Recuperamos los datos de verdad para ver si impactaron correctamente) ---
        firestoreManager.getDataUser(userId).test {
            TestCase.assertEquals(AuthResponseFirestore.Loading, awaitItem())

            val successResult = awaitItem() as AuthResponseFirestore.Success
            // ¡Magia! Comprobamos que lo que recuperamos es exactamente lo que guardamos en el paso anterior
            TestCase.assertEquals("Ir al gimnasio", successResult.data?.habit)
            TestCase.assertEquals("2026-07-03", successResult.data?.date)

            awaitComplete()
        }
    }

}