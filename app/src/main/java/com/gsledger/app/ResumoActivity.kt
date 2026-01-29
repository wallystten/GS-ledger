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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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

        // 🔥 MAPA PARA SOMAR GASTOS POR CATEGORIA
        val gastosPorCategoria = HashMap<String, Double>()

        for (i in 0 until transacoes.length()) {
            val item = transacoes.getJSONObject(i)

            val valor = parseValorSeguro(item.optString("valor", "0"))
            val tipo = item.optString("tipo", "saida")
            val categoria = item.optString("categoria", "Outros")

            if (tipo == "entrada") {
                totalEntradas += valor
            } else {
                totalSaidas += valor

                // Soma na categoria
                val atual = gastosPorCategoria[categoria] ?: 0.0
                gastosPorCategoria[categoria] = atual + valor
            }
        }

        val saldo = totalEntradas - totalSaidas
        val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        tvTotal.text =
            "Entradas: ${formatador.format(totalEntradas)}\n" +
            "Saídas: ${formatador.format(totalSaidas)}\n" +
            "Saldo: ${formatador.format(saldo)}"

        val dicas = FinancialAdvisor.gerarDicas(transacoes)
        tvDicas.text = dicas.joinToString("\n\n")

        mostrarGraficoCategorias(gastosPorCategoria)
    }

    private fun parseValorSeguro(valorStr: String): Double {
        val valor = valorStr.trim()
        return try {
            when {
                valor.contains(",") && valor.contains(".") ->
                    valor.replace(".", "").replace(",", ".").toDouble()
                valor.contains(",") ->
                    valor.replace(",", ".").toDouble()
                else ->
                    valor.toDouble()
            }
        } catch (e: Exception) {
            0.0
        }
    }

    // 📊 GRÁFICO DE GASTOS POR CATEGORIA
    private fun mostrarGraficoCategorias(gastos: Map<String, Double>) {
        val entries = ArrayList<PieEntry>()

        for ((categoria, valor) in gastos) {
            if (valor > 0) {
                entries.add(PieEntry(valor.toFloat(), categoria))
            }
        }

        val dataSet = PieDataSet(entries, "Gastos por Categoria")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 13f
        dataSet.sliceSpace = 3f

        val data = PieData(dataSet)

        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.centerText = "Onde você gasta"
        pieChart.setEntryLabelColor(android.graphics.Color.BLACK)
        pieChart.animateY(1200)
        pieChart.invalidate()
    }
}
