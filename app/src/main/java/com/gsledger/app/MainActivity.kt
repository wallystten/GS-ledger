package com.gsledger.app

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdicionar = findViewById<Button>(R.id.btnAdicionar)
        val btnVerResumo = findViewById<Button>(R.id.btnVerResumo)
        val btnEscanearQR = findViewByById<Button>(R.id.btnEscanearQR)
        val btnAtivarNotif = findViewById<Button>(R.id.btnAtivarNotif)

        // ➕ Adicionar lançamento manual
        btnAdicionar.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        // 📊 Ver resumo financeiro
        btnVerResumo.setOnClickListener {
            startActivity(Intent(this, ResumoActivity::class.java))
        }

        // 📷 Abrir leitor de QR Code
        btnEscanearQR.setOnClickListener {
            startActivity(Intent(this, QrScannerActivity::class.java))
        }

        // 🔔 Ativar leitura de notificações (bancos, Pix etc)
        btnAtivarNotif.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }
}
