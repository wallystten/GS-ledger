package com.gsledger.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdicionar = findViewById<Button>(R.id.btnAdicionar)
        val btnVerResumo = findViewById<Button>(R.id.btnVerResumo)
        val btnEscanearQR = findViewById<Button>(R.id.btnEscanearQR)

        btnAdicionar.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        btnVerResumo.setOnClickListener {
            Toast.makeText(this, "Resumo financeiro (em breve)", Toast.LENGTH_SHORT).show()
        }

        btnEscanearQR.setOnClickListener {
            startActivity(Intent(this, QrScannerActivity::class.java))
        }
    }
}
