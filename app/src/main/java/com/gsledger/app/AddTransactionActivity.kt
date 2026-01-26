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

        val qrValue = intent.getStringExtra("qrValue")
        if (!qrValue.isNullOrEmpty()) {
            val valorLimpo = qrValue.filter { it.isDigit() || it == '.' || it == ',' }
            etValor.setText(valorLimpo)
        }

        btnSalvar.setOnClickListener {
            val descricao = etDescricao.text.toString()
            val valor = etValor.text.toString()
            val tipo = if (rbEntrada.isChecked) "entrada" else "saida"

            if (descricao.isEmpty() || valor.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                Storage.saveTransaction(this, descricao, valor, tipo)
                Toast.makeText(this, "Lançamento salvo!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
