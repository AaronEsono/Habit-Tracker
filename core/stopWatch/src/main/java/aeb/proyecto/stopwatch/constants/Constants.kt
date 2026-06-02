package aeb.proyecto.stopwatch.constants

/**
 * Global constants orchestrating Intent routing, notification signaling,
 * and operational state boundaries for the background time tracking infrastructure.
 */

// ============================================================================
// Service Lifecycle Actions
// ============================================================================
/** Action intent payload to resume an active, paused tracking session loop. */
const val ACTION_SERVICE_RESUME = "ACTION_SERVICE_RESUME"

/** Action intent payload to temporarily pause a running tracking loop. */
const val ACTION_SERVICE_STOP = "ACTION_SERVICE_STOP"

/** Action intent payload to completely abort and reset the current time tracking matrix. */
const val ACTION_SERVICE_CANCEL = "ACTION_SERVICE_CANCEL"

/** Action intent payload dispatched when a countdown or interval cycle reaches completion. */
const val ACTION_SERVICE_FINISH = "ACTION_SERVICE_FINISH"

// ============================================================================
// Service Modality Initializers
// ============================================================================
/** Action intent payload utilized to initialize the service under the continuous Stopwatch modality. */
const val ACTION_SERVICE_START_STOPWATCH = "ACTION_SERVICE_STOP_STOPWATCH"

/** Action intent payload utilized to initialize the service under the fixed-duration Countdown Timer modality. */
const val ACTION_SERVICE_START_TIMER = "ACTION_SERVICE_START_TIMER"

/** Action intent payload utilized to initialize the service under the compound dynamic Interval training modality. */
const val ACTION_SERVICE_START_INTERVAL = "ACTION_SERVICE_START_INTERVAL"

// ============================================================================
// Notification Subsystem Configuration
// ============================================================================
/** Unique system-level Channel Identifier required to dispatch background task notification tokens. */
const val NOTIFICATION_CHANNEL_ID = "STOPWATCH_NOTIFICATION_ID"

/** User-facing display name assigned to the persistence notification channel sub-category. */
const val NOTIFICATION_CHANNEL_NAME = "STOPWATCH_NOTIFICATION"

/** Unique absolute integer handle allocated to identify the Foreground Service's persistent notification asset. */
const val NOTIFICATION_ID = -900

// ============================================================================
// PendingIntent Request Codes
// ============================================================================
/** Request code allocated to the tracking layout content interaction click handler. */
const val CLICK_REQUEST_CODE = 100

/** Request code mapped to the destructive 'Cancel' background action token. */
const val CANCEL_REQUEST_CODE = 101

/** Request code mapped to the functional 'Pause/Stop' background action token. */
const val STOP_REQUEST_CODE = 102

/** Request code mapped to the functional 'Resume' background action token. */
const val RESUME_REQUEST_CODE = 103

// ============================================================================
// Time Instrument Domain Classifications
// ============================================================================
/** Domain classification index mapping strictly to the progressive Stopwatch tracking mode. */
const val STOPWATCH = 0

/** Domain classification index mapping strictly to the regressive Countdown Timer tracking mode. */
const val TIMER = 1

/** Domain classification index mapping strictly to the iterative multi-segment Interval tracking mode. */
const val INTERVAL = 2