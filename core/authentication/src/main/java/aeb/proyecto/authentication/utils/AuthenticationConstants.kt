package aeb.proyecto.authentication.utils

/**
 * OOTB Identity Framework Domain Constants.
 *
 * This file contains the top-level compile-time constant keys used as internal semantic discriminators
 * across the authentication execution pipelines. Unlike raw, generic infrastructure network exceptions,
 * these specific tokens isolate expected business-rule validation boundaries, allowing presentation modules
 * to intercept state outcomes and orchestrate defensive UI navigation routing parameters dynamically.
 */

/**
 * Signaling indicator stating that a registration lifecycle invocation cannot proceed because
 * the requested email address is already bound to an active user profile registry.
 */
const val ERROR_EMAIL_EXISTS = "ERROR_EMAIL_EXISTS"

/**
 * Signaling indicator stating that credential authentication passed security validation rules, but
 * the user profile session remains locked due to a pending verification email activation constraint.
 */
const val ERROR_UNVERIFIED_EMAIL = "ERROR_UNVERIFIED_EMAIL"

/**
 * Signaling indicator stating that an operation targeting the current user context failed
 * because the active local session token cache is empty, expired, or corrupted.
 */
const val ERROR_NO_USER_FOUND = "ERROR_NO_USER_FOUND"