package aeb.proyecto.language.provider

import android.content.Context
import android.telephony.TelephonyManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import javax.inject.Inject


/**
 * Injectable provider that resolves regional calendar configurations based on client device locales.
 * * This class is intended for architectural injection (e.g., in ViewModels or Data Repositories)
 * to align time-tracking metrics with local regional standards.
 *
 * @property context The global [ApplicationContext] used to query system locale configuration baselines.
 */
open class RegionFirstDayProvider @Inject constructor(
    @ApplicationContext val context: Context
){
    /**
     * Resolves the proper [DayOfWeek] starting boundary matching the device's determined location profile.
     *
     * @return The specific [DayOfWeek] representing the localized start of a calendar week (e.g., Monday or Sunday).
     */
    open fun getFirstDayOfWeekByLocale(): DayOfWeek {
        return getCountryCode(resolveCountryCode(context))
    }
}

/**
 * Static utility shorthand to resolve the proper starting [DayOfWeek] directly inside the presentation layer.
 * * This alternative method allows Jetpack Compose UI structures to fetch calendar boundaries
 * using a local [Context] instance without explicit class injection pipelines.
 *
 * @param context The active layout or application context reference token.
 * @return The specific [DayOfWeek] representing the localized start of a calendar week.
 */
fun getFirstDayOfWeekByLocale(context: Context): DayOfWeek {
    return getCountryCode(resolveCountryCode(context))
}

/**
 * Evaluates the current runtime environment to extract the most accurate ISO country code.
 *
 * This multi-tier resolution system prioritizes the live SIM/network provider country code metadata,
 * falling back gracefully to the primary OS hardware locale configurations if network streams are unavailable.
 *
 * @param context The infrastructure environment context target.
 * @return A unified, uppercase two-letter ISO country code string token.
 */
private fun resolveCountryCode(context: Context): String {
    val telephonyCountry = getCountryFromNetwork(context)
    val localeCountry = context.resources.configuration.locales[0].country.uppercase()
    return telephonyCountry ?: localeCountry
}

/**
 * Queries the hardware telephony subsystem to extract the current active network operator's country region.
 * * This provides a high-fidelity geographical location snapshot based on the cell tower or network
 * registration context, operating independently of the user's selected system language interface.
 *
 * @param context The infrastructure environment context target.
 * @return A uppercase two-letter ISO country code string token (e.g., "PE", "US", "ES"),
 * or null if the device lacks telephony capabilities, has no active network signal, or is in airplane mode.
 */
fun getCountryFromNetwork(context: Context): String? {
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
    return tm?.networkCountryIso?.uppercase()?.takeIf { it.isNotBlank() }
}

/**
 * Maps an ISO-3166-1 alpha-2 country code token to its official calendar week-start boundary.
 * * This lookup engine relies on the global standard regional distribution configurations
 * to align dynamic calendar metrics with domestic standards.
 *
 * @param country A unified, uppercase two-letter country code string token (e.g., "US", "ES").
 * @return The specific [DayOfWeek] initialization token targeting that country.
 */
fun getCountryCode(country:String):DayOfWeek{
    return when (country) {
        // --- WEEK STARTS ON SUNDAY ---
        "AG", "AR", "AS", "BD", "BH", "BM", "BN", "BR", "BS", "BT", "BW", "BZ",
        "CA", "CH", "CL", "CO", "CR", "DM", "DO", "EC", "EG", "ET", "FJ", "FM",
        "GD", "GT", "GU", "HK", "HN", "ID", "IL", "IN", "JM", "JO", "JP", "KE", "KH",
        "KR", "KW", "LA", "LB", "LK", "MH", "MM", "MO", "MP", "MT", "MU", "MX", "MY",
        "MZ", "NA", "NI", "NP", "OM", "PA", "PE", "PH", "PK", "PR", "PY",
        "QA", "SA", "SG", "SV", "SY", "TH", "TT", "TW", "TZ", "UG", "US", "UY", "VE",
        "VI", "VN", "WS", "YE", "ZA", "ZW" -> DayOfWeek.SUNDAY

        // --- WEEK STARTS ON SATURDAY ---
        "AF", "DZ", "IR", "IQ", "LY", "SD", "SO" -> DayOfWeek.SATURDAY

        // --- WEEK STARTS ON FRIDAY ---
        "MV" -> DayOfWeek.FRIDAY

        // --- DEFAULT BACKGROUND BOUNDARY: WEEK STARTS ON MONDAY ---
        // Covers Europe (ES, FR, DE, GB, PT...), China (CN), Australia (AU), New Zealand (NZ), etc.
        else -> DayOfWeek.MONDAY
    }
}