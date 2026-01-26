package com.gsledger.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val etDescricao = findViewById<EditText>(R.id.etDescricao)
        val etValor = findViewById<EditText>(R.id.etValor)
        val btnSalvar = findViewById<Button>(R.id.btnSalvarLancamento)

        // 🔽 RECEBE VALOR VINDO DO QR CODE
        val qrValue = intent.getStringExtra("qrValue")
        if (!qrValue.isNullOrEmpty()) {
            // Mantém só números e separadores
            val valorLimpo = qrValue.filter { it.isDigit() || it == '.' || it == ',' }
            etValor.setText(valorLimpo)
        }

        btnSalvar.setOnClickListener {
            val descricao = etDescricao.text.toString()
            val valor = etValor.text.toString()

            if (descricao.isEmpty() || valor.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Lançamento salvo (ainda não gravado no app)", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
