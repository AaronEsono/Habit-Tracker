package aeb.proyecto.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * An extension property on [Project] to easily access the version catalog named "libs"
 * from within custom Gradle convention plugins.
 *
 * By default, type-safe accessors for the version catalog are not directly available
 * in precompiled or class-based custom plugins. This utility bridges that gap, allowing
 * syntax like `libs.findLibrary("...")` inside the convention plugins.
 *
 * @return The [VersionCatalog] instance containing dependencies, versions, and plugins definitions.
 */
val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")