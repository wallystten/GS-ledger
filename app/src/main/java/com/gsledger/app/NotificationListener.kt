package com.gsledger.app

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import java.util.regex.Pattern

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        val mensagemCompleta = "$title $text"

        val valor = extrairValor(mensagemCompleta)

        if (valor != null) {
            Storage.saveTransaction(
                applicationContext,
                "Lançamento automático",
                valor
            )
        }
    }

    private fun extrairValor(texto: String): String? {
        val regex = Pattern.compile("""R\$\s?([0-9]+[.,][0-9]{2})""")
        val matcher = regex.matcher(texto)

        return if (matcher.find()) {
            matcher.group(1)
        } else {
            null
        }
    }
}
