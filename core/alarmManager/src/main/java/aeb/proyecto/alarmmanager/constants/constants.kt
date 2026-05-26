package aeb.proyecto.alarmmanager.constants

/**
 * Global constant metadata registry governing the configuration boundaries of the Alarm Manager ecosystem.
 *
 * This file contains immutable primitive values utilized across internal intent broadcasting, notification
 * engine layouts, and operational scheduling intervals for background tasks.
 */
const val CHANNEL = "CHANNEL"

/**
 * System notification channel architecture constants.
 */
const val NAME = "NAME"

/**
 * Key identifier utilized as a localized Intent Extra payload key to transmit stringified
 * JSON alarm configurations toward background broadcast interceptors.
 */
const val REMINDER = "REMINDER"

/**
 * Core production scheduling intervals.
 * * Defines a structural 24-hour frequency window converted into raw milliseconds
 * ($24 \times 60 \times 60 \times 1000$). Used to anchor recurrent daily synchronization tasks.
 */
const val INTERVAL = 24L * 60L * 60L * 1000L

/**
 * Sandbox testing interval window.
 * * Provides an accelerated 5-minute fallback boundary ($5 \times 60 \times 1000$)
 * dedicated exclusively to local debugging workflows, bypassing standard production window ceilings.
 */
const val DEBUG = 1000L * 5L * 60L // 5 Minutos