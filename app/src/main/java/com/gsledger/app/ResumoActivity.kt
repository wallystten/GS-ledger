package com.gsledger.app

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import org.json.JSONArray
import java.text.NumberFormat
import java.util.*

class ResumoActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var tvTotal: TextView
    private lateinit var tvDicas: TextView
    private lateinit var pieChart: PieChart
    private lateinit var transacoes: JSONArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumo)

        tvTotal = findViewById(R.id.tvTotal)
        tvDicas = findViewById(R.id.tvDicas)
        listView = findViewById(R.id.listViewTransacoes)
        pieChart = findViewById(R.id.pieChart)

        carregarLista()

        listView.setOnItemLongClickListener { _, _, position, _ ->
            AlertDialog.Builder(this)
                .setTitle("Excluir lançamento")
                .setMessage("Deseja remover este lançamento?")
                .setPositiveButton("Excluir") { _, _ ->
                    Storage.deleteTransaction(this, position)
                    carregarLista()
                }
                .setNegativeButton("Cancelar", null)
                .show()
            true
        }
    }

    private fun carregarLista() {
        transacoes = Storage.getTransactions(this)

        val adapter = TransactionAdapter(this, transacoes)
        listView.adapter = adapter

        var totalEntradas = 0.0
        var totalSaidas = 0.0

        for (i in 0 until transacoes.length()) {
            val item = transacoes.getJSONObject(i)
            val valor = parseValorSeguro(item.optString("valor", "0"))
            val tipo = item.optString("tipo", "saida")

            if (tipo == "entrada") totalEntradas += valor
            else totalSaidas += valor
        }

        val saldo = totalEntradas - totalSaidas
        val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        tvTotal.text =
            "Entradas: ${formatador.format(totalEntradas)}\n" +
            "Saídas: ${formatador.format(totalSaidas)}\n" +
            "Saldo: ${formatador.format(saldo)}"

        val dicas = FinancialAdvisor.gerarDicas(transacoes)
        tvDicas.text = dicas.joinToString("\n\n")

        mostrarGrafico(totalEntradas, totalSaidas)
    }

    /**
     * 🔥 CONVERSÃO INTELIGENTE DE VALORES
     * Aceita:
     * 100,50
     * 100.50
     * 1.234,56
     * 1234.56
     */
    private fun parseValorSeguro(valorStr: String): Double {
        val valor = valorStr.trim()

        return try {
            when {
                valor.contains(",") && valor.contains(".") -> {
                    // Formato brasileiro 1.234,56
                    valor.replace(".", "").replace(",", ".").toDouble()
                }
                valor.contains(",") -> {
                    // Formato 100,50
                    valor.replace(",", ".").toDouble()
                }
                else -> {
                    // Formato 100.50 ou 100
                    valor.toDouble()
                }
            }
        } catch (e: Exception) {
            0.0
        }
    }

    private fun mostrarGrafico(entradas: Double, saidas: Double) {
        val entries = ArrayList<PieEntry>()

        if (entradas > 0) entries.add(PieEntry(entradas.toFloat(), "Entradas"))
        if (saidas > 0) entries.add(PieEntry(saidas.toFloat(), "Saídas"))

        val dataSet = PieDataSet(entries, "Resumo Financeiro")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 14f

        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.centerText = "Fluxo de Caixa"
        pieChart.animateY(1000)
        pieChart.invalidate()
    }
}
