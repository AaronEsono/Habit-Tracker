package aeb.proyecto.habittracker.utils

import aeb.proyecto.habittracker.utils.Constans.APPTITLE
import aeb.proyecto.habittracker.utils.Constans.EMAIL
import android.content.Context
import android.content.Intent
import android.net.Uri

fun sendEmail(context:Context){
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Especifica el esquema mailto
        putExtra(Intent.EXTRA_EMAIL, arrayOf(EMAIL)) // Correo del destinatario
        putExtra(Intent.EXTRA_SUBJECT, APPTITLE) // Asunto opcional
    }

    context.startActivity(intent)
}