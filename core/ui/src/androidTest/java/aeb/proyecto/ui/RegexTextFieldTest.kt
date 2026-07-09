package aeb.proyecto.ui

import aeb.proyecto.room.model.classes.UnitHabit
import aeb.proyecto.ui.regexTextField.IsOnlyDecimal
import aeb.proyecto.ui.regexTextField.IsOnlyDigit
import aeb.proyecto.ui.regexTextField.IsOnlyZeroTo59
import aeb.proyecto.ui.regexTextField.IsOnlyZeroTo99
import aeb.proyecto.ui.regexTextField.OneTo99
import aeb.proyecto.ui.textField.CustomTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.test.junit4.createComposeRule
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class RegexTextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenOneTo99_whenUserTypesCorrectly_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("45")

        // --- WHEN ---
        composeTestRule.setContent {
            OneTo99(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("45", textFieldState.text.toString())
    }

    @Test
    fun givenOneTo99_whenIsBiggerThan99_ThenReturnsAValueUnder99(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("456")

        // --- WHEN ---
        composeTestRule.setContent {
            OneTo99(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("45", textFieldState.text.toString())
    }

    @Test
    fun givenOneTo99_whenIsBiggerThan99AndChangedAgain_ThenReturnsAValueUnder99(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("456")

        // --- WHEN ---
        composeTestRule.setContent {
            OneTo99(textFieldState = textFieldState)
        }

        textFieldState.edit {
            append("1")
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("45", textFieldState.text.toString())
    }

    @Test
    fun givenOneTo99_whenUserIsUnderThan1_ThenReturnsAValueUnder99(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("-56")

        // --- WHEN ---
        composeTestRule.setContent {
            OneTo99(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyZeroTo99_whenUserTypesCorrectly_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("45")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyZeroTo99(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("45", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyZeroTo99_whenIsBiggerThan99_ThenReturnsAValueUnder99(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("456")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyZeroTo99(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("45", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyZeroTo99_whenIs0_ThenReturnsTheValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("0")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyZeroTo99(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("0", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyZeroTo99_whenIsBiggerThan99AndChangedAgain_ThenReturnsAValueUnder99(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("456")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyZeroTo99(textFieldState = textFieldState)
        }

        textFieldState.edit {
            append("1")
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("45", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyZeroTo99_whenIsUnderThan1_ThenReturnsAValueUnder99(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("-56")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyZeroTo99(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyZeroTo59_whenUserTypesCorrectly_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("45")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyZeroTo59(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("45", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyZeroTo59_whenIsBiggerThan99_ThenReturnsAValueUnder99(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("456")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyZeroTo59(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("45", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyZeroTo59_whenIs0_ThenReturnsTheValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("0")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyZeroTo59(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("0", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyZeroTo59_whenbetween6And9_ThenReturnsTheValueWithAZero(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("6")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyZeroTo59(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("06", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyZeroTo59_whenIsBiggerThan99AndChangedAgain_ThenReturnsAValueUnder99(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("456")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyZeroTo59(textFieldState = textFieldState)
        }

        textFieldState.edit {
            append("1")
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("45", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyZeroTo59_whenIsUnderThan1_ThenReturnsAValueUnder99(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("-56")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyZeroTo59(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyDecimal_whenIsANumber_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("56")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyDecimal(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("56", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyDecimal_whenIsAPoint_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState(".3")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyDecimal(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("0.", textFieldState.text.toString())
    }


    @Test
    fun givenIsOnlyDecimal_whenIsADecimal_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("56.456")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyDecimal(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("56.456", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyDecimal_whenIsADecimalIsTooLong_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("56.456343434")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyDecimal(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("56.456", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyDigit_whenIsANumber_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("56")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyDigit(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("56", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyDigit_whenHasALetter_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("56A")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyDigit(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("56", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyDigit_whenHasALetterBetween_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("5A6A")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyDigit(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("5", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyDigit_whenHasDigits_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("55.5")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyDigit(textFieldState = textFieldState)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("55", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyDigit_whenItsQuantity_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("55.5")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyDigit(textFieldState = textFieldState, UnitHabit.CALORIES)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("55.5", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyDigit_whenItsFrequency_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("55.5")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyDigit(textFieldState = textFieldState, UnitHabit.SESSIONS)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("55.5", textFieldState.text.toString())
    }

    @Test
    fun givenIsOnlyDigit_whenItsTime_ThenReturnsTheCorrectValue(){
        // --- GIVEN ---
        val textFieldState = TextFieldState("55.5")

        // --- WHEN ---
        composeTestRule.setContent {
            IsOnlyDigit(textFieldState = textFieldState, UnitHabit.MINUTES)
        }

        //Then
        composeTestRule.waitForIdle()
        assertEquals("55", textFieldState.text.toString())
    }
}