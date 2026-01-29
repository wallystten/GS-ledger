package com.gsledger.app

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import java.util.regex.Pattern

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {

        val pacote = sbn.packageName.lowercase()

        // 🏦 Detecta o banco pela notificação
        val origemBanco = detectarBanco(pacote) ?: return

        val extras = sbn.notification.extras

        val title = extras.getString("android.title") ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""
        val bigText = extras.getCharSequence("android.bigText")?.toString() ?: ""

        val mensagemCompleta = "$title $text $bigText"

        Log.d("GS_LEDGER_NOTIF", "BANCO: $origemBanco | MSG: $mensagemCompleta")

        val valor = extrairValor(mensagemCompleta)
        val tipo = detectarTipo(mensagemCompleta)

        if (valor != null) {
            Storage.saveTransaction(
                applicationContext,
                descricao = "Movimentação bancária",
                valor = valor,
                tipo = tipo,
                origem = origemBanco // 🔥 AQUI VAI O NOME DO BANCO
            )

            Log.d("GS_LEDGER_NOTIF", "SALVO: $origemBanco | R$ $valor | $tipo")
        }
    }

    // 🏦 Mapeia pacote → nome do banco
    private fun detectarBanco(pacote: String): String? {
        return when {
            pacote.contains("santander") -> "Santander"
            pacote.contains("nubank") -> "Nubank"
            pacote.contains("itau") -> "Itaú"
            pacote.contains("bradesco") -> "Bradesco"
            pacote.contains("bb") -> "Banco do Brasil"
            pacote.contains("caixa") -> "Caixa"
            pacote.contains("inter") -> "Banco Inter"
            pacote.contains("sicredi") -> "Sicredi"
            else -> null // ignora apps que não são bancos
        }
    }

    private fun extrairValor(texto: String): String? {
        val regex = Pattern.compile("""R\$\s?([0-9\.,]+)""")
        val matcher = regex.matcher(texto)
        return if (matcher.find()) matcher.group(1) else null
    }

    private fun detectarTipo(texto: String): String {
        val t = texto.lowercase()

        val palavrasSaida = listOf(
            "pix enviado",
            "seu pix foi enviado",
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

        if (t.contains("pix") && !t.contains("enviado") && !t.contains("pagou"))
            return "entrada"

        return "saida"
    }
}
