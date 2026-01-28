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
    override fun getItem(position: Int): Any = transacoes.getJSONObject(position)
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_transacao, parent, false)

        val item = transacoes.getJSONObject(position)

        val descricao = item.optString("descricao", "Sem descrição")
        val valor = item.optString("valor", "0").replace(",", ".").toDoubleOrNull() ?: 0.0
        val data = item.optString("data", "")
        val tipo = item.optString("tipo", "saida")
        val origem = item.optString("origem", "Manual")

        val tvTitulo = view.findViewById<TextView>(R.id.tvTitulo)
        val tvOrigem = view.findViewById<TextView>(R.id.tvOrigem)
        val tvData = view.findViewById<TextView>(R.id.tvData)
        val tvValor = view.findViewById<TextView>(R.id.tvValor)

        val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        tvTitulo.text = descricao
        tvOrigem.text = "Origem: $origem"
        tvData.text = data
        tvValor.text = formatador.format(valor)

        if (tipo == "entrada") {
            tvValor.setTextColor(Color.parseColor("#2E7D32")) // verde
        } else {
            tvValor.setTextColor(Color.parseColor("#C62828")) // vermelho
        }

        return view
    }
}
