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
        val tipo = detectarTipo(mensagemCompleta)

        if (valor != null) {
            Storage.saveTransaction(
                applicationContext,
                "Movimentação bancária",
                valor,
                tipo
            )
        }
    }

    private fun extrairValor(texto: String): String? {
        // Captura valores tipo: R$ 1.234,56 ou R$12,34
        val regex = Pattern.compile("""R\$\s?([0-9\.,]+)""")
        val matcher = regex.matcher(texto)
        return if (matcher.find()) matcher.group(1) else null
    }

    
     private fun detectarTipo(texto: String): String {
    val t = texto.lowercase()

    // 🔴 SAÍDAS (dinheiro saindo) — VERIFICAMOS PRIMEIRO
    val palavrasSaida = listOf(
        "pix enviado",
        "você enviou",
        "pagamento realizado",
        "pagamento de",
        "compra no valor",
        "débito realizado",
        "debito realizado",
        "transferência enviada",
        "ted enviada",
        "você pagou",
        "pagou um pix",
        "pix pago"
    )

    // 🟢 ENTRADAS (dinheiro entrando)
    val palavrasEntrada = listOf(
        "recebeu um pix",
        "pix recebido",
        "valor creditado",
        "creditado em sua conta",
        "transferência recebida",
        "ted recebida",
        "depósito recebido",
        "deposito recebido",
        "você recebeu"
    )

    if (palavrasSaida.any { t.contains(it) }) return "saida"
    if (palavrasEntrada.any { t.contains(it) }) return "entrada"

    // Regra extra: se mencionar PIX mas NÃO disser que pagou/enviou
    if (t.contains("pix")) return "entrada"

    return "saida"
  }
}
