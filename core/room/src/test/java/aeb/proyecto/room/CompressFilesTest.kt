package aeb.proyecto.room

import aeb.proyecto.room.entities.Habit
import aeb.proyecto.room.entities.relations.EntireHabit
import aeb.proyecto.room.utils.compressJson
import aeb.proyecto.room.utils.decompressJson
import aeb.proyecto.room.utils.decompressJsonFirestore
import aeb.proyecto.room.utils.jsonCompressed
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class CompressFilesTest {

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    @Test
    fun `given raw json string when compress json is called then returns valid base64 string`() {
        // --- GIVEN ---
        val originalJson = """{"id":1,"name":"Ir al gimnasio","color":-16711936}"""

        // --- WHEN ---
        val compressed = compressJson(originalJson)

        // --- THEN ---
        assertNotNull(compressed)
        assert(compressed.isNotEmpty())
    }

    @Test
    fun `given compressed base64 string when decompress json is called then returns original raw json`() {
        // --- GIVEN ---
        val originalJson = """{"id":2,"name":"Estudiar Kotlin"}"""
        val compressed = compressJson(originalJson)

        // --- WHEN ---
        val decompressed = decompressJson(compressed)

        // --- THEN ---
        assertEquals(originalJson, decompressed)
    }



    @Test
    fun `given list of entire habits when json compressed is called then serializes and compresses successfully`() {
        // --- GIVEN ---
        // Instancia un fake con los datos mínimos que use tu convertToDTO()
        val fakeHabits = listOf(
            EntireHabit(
                habit = Habit(id = 1L, name = "Harina de otro costal"),
                dailyHabits = mutableListOf()
            )
        )

        // --- WHEN ---
        val compressedPayload = jsonCompressed(fakeHabits)

        // --- THEN ---
        assertNotNull(compressedPayload)

        // Opcional: Validamos el viaje de vuelta completo para asegurar que no se pierde info
        val recoveredHabits = decompressJsonFirestore(compressedPayload)
        assertEquals(fakeHabits.size, recoveredHabits.size)
        assertEquals(fakeHabits.first().habit.name, recoveredHabits.first().habit.name)
    }

    @Test
    fun `given empty list of habits when json compressed is called then returns compressed empty structural array`() {
        // --- GIVEN ---
        val emptyHabitsList = emptyList<EntireHabit>()

        // --- WHEN ---
        val compressedPayload = jsonCompressed(emptyHabitsList)

        // --- THEN ---
        assertNotNull(compressedPayload)

        // Verificamos el viaje de vuelta: debe reconstruir una lista vacía de forma segura sin romper el mapeador
        val recoveredList = decompressJsonFirestore(compressedPayload)
        assertTrue(recoveredList.isEmpty())
    }

    @Test
    fun `given complex characters or long text when compress and decompress are called then preserves encoding integrity`() {
        // --- GIVEN ---
        // Probamos con caracteres especiales (emojis, acentos) que podrían corromperse si falla el Charsets.UTF_8
        val complexJson = """
        {
          "id": 42,
          "name": "🏋️‍♂️ Mañana de gimnasio: ¡Bíceps y forearms!",
          "notes": "Relación, combinación y compresión con Room & Firestore."
        }
        """.trimIndent()

        // --- WHEN ---
        val compressed = compressJson(complexJson)
        val decompressed = decompressJson(compressed)

        // --- THEN ---
        assertEquals(complexJson, decompressed)
    }
}