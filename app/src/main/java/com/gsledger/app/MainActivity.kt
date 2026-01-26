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
        val btnEscanearQR = findViewById<Button>(R.id.btnEscanearQR)
        val btnAtivarNotif = findViewById<Button>(R.id.btnAtivarNotif)

        btnAdicionar.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        btnVerResumo.setOnClickListener {
            startActivity(Intent(this, ResumoActivity::class.java))
        }

        btnEscanearQR.setOnClickListener {
            startActivity(Intent(this, QrScannerActivity::class.java))
        }

        // 🔔 Abre a tela do Android para permitir leitura de notificações
        btnAtivarNotif.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }
}
