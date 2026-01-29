package com.gsledger.app

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.json.JSONArray
import java.text.NumberFormat
import java.util.*

class TransactionAdapter(
    private val context: Context,
    private val transacoes: JSONArray
) : BaseAdapter() {

    override fun getCount(): Int = transacoes.length()

    override fun getItem(position: Int): Any =
        transacoes.getJSONObject(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_transacao, parent, false)

        val item = transacoes.getJSONObject(position)

        val descricao = item.optString("descricao", "Sem descrição")
        val data = item.optString("data", "")
        val tipo = item.optString("tipo", "saida")
        val origem = item.optString("origem", "Manual")

        val tvTitulo = view.findViewById<TextView>(R.id.tvTitulo)
        val tvOrigem = view.findViewById<TextView>(R.id.tvOrigem)
        val tvData = view.findViewById<TextView>(R.id.tvData)
        val tvValor = view.findViewById<TextView>(R.id.tvValor)

        val valor = converterValorSeguro(item.optString("valor", "0"))

        val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        tvTitulo.text = descricao
        tvOrigem.text = "Origem: $origem"
        tvData.text = data
        tvValor.text = formatador.format(valor)

        if (tipo == "entrada") {
            tvValor.setTextColor(Color.parseColor("#2E7D32")) // Verde
        } else {
            tvValor.setTextColor(Color.parseColor("#C62828")) // Vermelho
        }

        return view
    }

    /**
     * 🔒 Converte valor com segurança total
     * Aceita:
     *  "1100"
     *  "1100.50"
     *  "1.100,50"
     *  "1100,50"
     */
    private fun converterValorSeguro(valorString: String): Double {
        return try {
            val limpo = valorString.trim()

            when {
                limpo.contains(",") && limpo.contains(".") -> {
                    // Formato BR com milhar: 1.234,56
                    limpo.replace(".", "").replace(",", ".").toDouble()
                }
                limpo.contains(",") -> {
                    // Formato BR simples: 1234,56
                    limpo.replace(",", ".").toDouble()
                }
                else -> {
                    // Já padrão: 1234.56
                    limpo.toDouble()
                }
            }
        } catch (e: Exception) {
            0.0
        }
    }
}
  
