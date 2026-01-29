package com.gsledger.app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val etDescricao = findViewById<EditText>(R.id.etDescricao)
        val etValor = findViewById<EditText>(R.id.etValor)
        val rbEntrada = findViewById<RadioButton>(R.id.rbEntrada)
        val rbSaida = findViewById<RadioButton>(R.id.rbSaida)
        val btnSalvar = findViewById<Button>(R.id.btnSalvarLancamento)
        val spCategoria = findViewById<Spinner>(R.id.spCategoria)

        // 📂 LISTA DE CATEGORIAS
        val categorias = listOf(
            "Alimentação",
            "Transporte",
            "Moradia",
            "Lazer",
            "Saúde",
            "Educação",
            "Compras",
            "Contas",
            "Salário",
            "Investimentos",
            "Outros"
        )

        val adapterCategorias = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categorias
        )
        spCategoria.adapter = adapterCategorias

        // 📥 DADOS VINDOS DO QR OU NOTIFICAÇÃO
        val qrValue = intent.getStringExtra("qrValue")
        val tipoAuto = intent.getStringExtra("tipoAuto")
        val descricaoAuto = intent.getStringExtra("descricaoAuto")
        val origemAuto = intent.getStringExtra("origemAuto")

        if (!qrValue.isNullOrEmpty()) {
            val valorLimpo = qrValue.filter { it.isDigit() || it == '.' || it == ',' }
            etValor.setText(valorLimpo)
        }

        if (!descricaoAuto.isNullOrEmpty()) {
            etDescricao.setText(descricaoAuto)
        }

        when (tipoAuto) {
            "entrada" -> rbEntrada.isChecked = true
            "saida" -> rbSaida.isChecked = true
        }

        if (!rbEntrada.isChecked && !rbSaida.isChecked) {
            rbSaida.isChecked = true
        }

        btnSalvar.setOnClickListener {
            val descricao = etDescricao.text.toString().trim()
            val valor = etValor.text.toString().trim()
            val categoria = spCategoria.selectedItem.toString()

            val tipo = when {
                rbEntrada.isChecked -> "entrada"
                rbSaida.isChecked -> "saida"
                else -> "saida"
            }

            if (descricao.isEmpty() || valor.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                Storage.saveTransaction(
                    context = this,
                    descricao = descricao,
                    valor = valor,
                    tipo = tipo,
                    origem = origemAuto ?: "Manual",
                    categoria = categoria   // 🆕 NOVO
                )

                Toast.makeText(this, "Lançamento salvo!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
