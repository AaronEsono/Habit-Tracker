package aeb.proyecto.habittracker.components.onboardScreen.components.constants

/**
 * Represents the possible navigation or completion actions for a result on the onboarding page.
 */
sealed class ResultOptions {
    /**
     * Skips all the pages
     */
    data object Skip: ResultOptions()

    /**
     * Moves to the next result.
     */
    data object Next: ResultOptions()

    /**
     * Moves to the previous result.
     */
    data object Previous: ResultOptions()

    /**
     * Finishes the current result flow.
     */
    data object Finish: ResultOptions()
}