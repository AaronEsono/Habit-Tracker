package aeb.proyecto.language.model

import java.time.DayOfWeek
import java.util.Locale

fun getFirstDayOfWeekByLocale(): DayOfWeek {
    return when (Locale.getDefault().country.uppercase()) {

        // 🌞 Países donde la semana comienza el DOMINGO
        // América, partes de Asia y África
        "AG", "AR", "AS", "AU", "BD", "BH", "BM", "BN", "BR", "BS", "BT", "BW", "BZ",
        "CA", "CH", "CL", "CN", "CO", "CR", "DM", "DO", "EC", "EG", "ET", "FJ", "FM",
        "GD", "GT", "GU", "HK", "HN", "ID", "IL", "IN", "JM", "JO", "JP", "KE", "KH",
        "KR", "KW", "LA", "LB", "LK", "MH", "MM", "MO", "MP", "MT", "MU", "MX", "MY",
        "MZ", "NA", "NI", "NP", "NZ", "OM", "PA", "PE", "PH", "PK", "PR", "PT", "PY",
        "QA", "SA", "SG", "SV", "SY", "TH", "TT", "TW", "TZ", "UG", "US", "UY", "VE",
        "VI", "VN", "WS", "YE", "ZA", "ZW" -> DayOfWeek.SUNDAY

        // 📿 Países donde la semana comienza el SÁBADO
        // Principalmente países musulmanes
        "AF", "DZ", "IR", "IQ", "LY", "SD", "SO" -> DayOfWeek.SATURDAY

        // 🕌 Países donde la semana comienza el VIERNES
        // Ejemplo: Maldivas
        "MV" -> DayOfWeek.FRIDAY

        // 🗓️ Resto del mundo: semana comienza el LUNES
        else -> DayOfWeek.MONDAY
    }
}