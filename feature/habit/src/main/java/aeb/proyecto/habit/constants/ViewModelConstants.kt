package aeb.proyecto.habit.constants

/**
 * Bounds the operational sliding window matrix utilized to compute dynamic calendar tracks.
 * Pre-calculates an historical tracking baseline spanning 150 days in retro-retrospective
 * and maps a buffer horizon extending 50 days into future planning boundaries.
 */
val rangeDays = (-150..50)

/**
 * Defensive cache retention threshold buffer designed for lifecycle-aware state flows.
 * Establishes a 5000ms delay window before halting reactive stream emissions when viewports
 * hit background tracks, completely mitigating runtime computation overhead during hardware
 * orientation configuration changes.
 */
const val stopTimeOutMillis:Long = 5_000