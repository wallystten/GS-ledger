package com.gsledger.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
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

        // 📥 DADOS VINDOS DO QR OU NOTIFICAÇÃO
        val qrValue = intent.getStringExtra("qrValue")
        val tipoAuto = intent.getStringExtra("tipoAuto")
        val descricaoAuto = intent.getStringExtra("descricaoAuto")
        val origemAuto = intent.getStringExtra("origemAuto") // 🆕 NOVO

        // 💰 Preenche valor automaticamente
        if (!qrValue.isNullOrEmpty()) {
            val valorLimpo = qrValue.filter { it.isDigit() || it == '.' || it == ',' }
            etValor.setText(valorLimpo)
        }

        // 📝 Preenche descrição automática
        if (!descricaoAuto.isNullOrEmpty()) {
            etDescricao.setText(descricaoAuto)
        }

        // 🔄 Marca entrada ou saída automaticamente
        when (tipoAuto) {
            "entrada" -> rbEntrada.isChecked = true
            "saida" -> rbSaida.isChecked = true
        }

        // 🛡️ Segurança: se nada foi marcado, assume SAÍDA
        if (!rbEntrada.isChecked && !rbSaida.isChecked) {
            rbSaida.isChecked = true
        }

        btnSalvar.setOnClickListener {
            val descricao = etDescricao.text.toString().trim()
            val valor = etValor.text.toString().trim()

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
                    origem = origemAuto ?: "Manual" // 🆕 SALVANDO ORIGEM
                )
                Toast.makeText(this, "Lançamento salvo!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
