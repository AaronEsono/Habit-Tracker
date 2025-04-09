pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Habit_Tracker"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:datastore")
include(":core:room")
include(":core:firestore")
include(":core:authentication")
include(":core:analytics")
include(":core:alarmManager")
include(":core:ui")
include(":feature:settings")
include(":core:language")
include(":feature:save")
include(":feature:logIn")
include(":feature:addHabit")
include(":feature:habit")
include(":feature:statistics")
