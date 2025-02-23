package aeb.proyecto.settings.utils

import aeb.proyecto.settings.R
import aeb.proyecto.settings.constants.SettingsConstants
import android.content.Context
import android.content.Intent
import android.net.Uri

fun openLink(context: Context, uri:String){
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    context.startActivity(intent)
}


fun sendEmail(context:Context){
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Especifica el esquema mailto
        putExtra(Intent.EXTRA_EMAIL, arrayOf(SettingsConstants.EMAIL)) // Correo del destinatario
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.settings_email_title)) // Asunto opcional
    }

    context.startActivity(intent)
}