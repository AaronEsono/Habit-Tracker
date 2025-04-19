package aeb.proyecto.language.provider

import android.content.Context
import android.telephony.TelephonyManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import javax.inject.Inject


class RegionFirstDayProvider @Inject constructor(
    @ApplicationContext val context: Context
){
    fun getFirstDayOfWeekByLocale(): DayOfWeek {
        return getCountryCode(resolveCountryCode(context))
    }
}

fun getFirstDayOfWeekByLocale(context: Context): DayOfWeek {
    return getCountryCode(resolveCountryCode(context))
}

private fun resolveCountryCode(context: Context): String {
    val telephonyCountry = getCountryFromNetwork(context)
    val localeCountry = context.resources.configuration.locales[0].country.uppercase()
    return telephonyCountry ?: localeCountry
}


fun getCountryFromNetwork(context: Context): String? {
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
    return tm?.networkCountryIso?.uppercase()?.takeIf { it.isNotBlank() }
}

fun getCountryCode(country:String):DayOfWeek{
    return when (country) {
        // DOMINGO
        "AG", "AR", "AS", "AU", "BD", "BH", "BM", "BN", "BR", "BS", "BT", "BW", "BZ",
        "CA", "CH", "CL", "CN", "CO", "CR", "DM", "DO", "EC", "EG", "ET", "FJ", "FM",
        "GD", "GT", "GU", "HK", "HN", "ID", "IL", "IN", "JM", "JO", "JP", "KE", "KH",
        "KR", "KW", "LA", "LB", "LK", "MH", "MM", "MO", "MP", "MT", "MU", "MX", "MY",
        "MZ", "NA", "NI", "NP", "NZ", "OM", "PA", "PE", "PH", "PK", "PR", "PT", "PY",
        "QA", "SA", "SG", "SV", "SY", "TH", "TT", "TW", "TZ", "UG", "US", "UY", "VE",
        "VI", "VN", "WS", "YE", "ZA", "ZW" -> DayOfWeek.SUNDAY

        // SÁBADO
        "AF", "DZ", "IR", "IQ", "LY", "SD", "SO" -> DayOfWeek.SATURDAY

        // VIERNES
        "MV" -> DayOfWeek.FRIDAY

        // Por defecto, LUNES
        else -> DayOfWeek.MONDAY
    }
}