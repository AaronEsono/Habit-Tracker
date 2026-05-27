package aeb.proyecto.analytics

import aeb.proyecto.analytics.model.AnalyticsEvent

/**
 * A decoupled architectural contract defining the public boundaries for telemetry data propagation.
 *
 * This interface serves as the foundational abstraction layer for tracking operations throughout the
 * application ecosystem. By consuming this contract instead of concrete implementation drivers, feature
 * modules remain strictly agnostic of underlying third-party telemetry vendors (such as Google Firebase,
 * Mixpanel, or local diagnostic systems).
 *
 * This structural isolation facilitates seamless vendor migrations, simplifies behavior mocking during
 * isolated unit testing phases, and enforces strict architectural decoupling.
 */
interface AnalyticsManagerInterface {

    /**
     * Dispatches a structured telemetry event token toward the active analytics processing pipeline.
     *
     * Client components should construct strongly-typed events through designated factory registries
     * and pass them directly to this method, leaving downstream sanitization and transport mechanics
     * completely encapsulated by the concrete implementation.
     *
     * @param event The structured [AnalyticsEvent] payload containing classification tags and analytical metadata.
     */
    fun logEvent(event: AnalyticsEvent)
}