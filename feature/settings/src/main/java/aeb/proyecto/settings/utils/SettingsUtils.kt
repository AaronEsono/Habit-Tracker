package aeb.proyecto.settings.utils

import aeb.proyecto.settings.BuildConfig
import aeb.proyecto.settings.R
import aeb.proyecto.settings.constants.SettingsConstants
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun openLink(context: Context, uri:String){
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    context.startActivity(intent)
}

fun sendEmail(context:Context){
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Especifica el esquema mailto
        putExtra(Intent.EXTRA_EMAIL, arrayOf(SettingsConstants.EMAIL)) // Correo del destinatario
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.settings_email_title)) // Asunto opcional
        putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.settings_message,
                getLocalDataDevice(),
                getGetLocalDateTime(),
                BuildConfig.APP_VERSION)
        )
    }

    context.startActivity(intent)
}

private fun getGetLocalDateTime():String{
    val localDateTime = LocalDateTime.now()
    val zoneId = ZoneId.systemDefault() // Obtienes la zona horaria por defecto del sistema
    val zonedDateTime = ZonedDateTime.of(localDateTime, zoneId)
    return zonedDateTime.toString()
}

private fun getLocalDataDevice():String{
    val deviceName = Build.MODEL // Modelo del dispositivo
    val deviceBrand = Build.BRAND // Marca del dispositivo

    return "$deviceName $deviceBrand"
}