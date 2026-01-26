package com.gsledger.app

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class ResumoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumo)

        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val listView = findViewById<ListView>(R.id.listViewTransacoes)

        val transacoes: JSONArray = Storage.getTransactions(this)
        val lista = mutableListOf<String>()
        var total = 0.0

        for (i in 0 until transacoes.length()) {
            val item = transacoes.getJSONObject(i)
            val descricao = item.getString("descricao")
            val valor = item.getString("valor").replace(",", ".").toDoubleOrNull() ?: 0.0
            val data = item.getString("data")

            total += valor
            lista.add("R$ %.2f - %s\n%s".format(valor, descricao, data))
        }

        tvTotal.text = "Total: R$ %.2f".format(total)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista)
        listView.adapter = adapter
    }
}
