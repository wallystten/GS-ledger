package com.gsledger.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 🔥 Inicializa o Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnResumo = findViewById<Button>(R.id.btnResumo)
        val btnScanner = findViewById<Button>(R.id.btnScanner)

        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        btnResumo.setOnClickListener {
            startActivity(Intent(this, ResumoActivity::class.java))
        }

        btnScanner.setOnClickListener {
            startActivity(Intent(this, QrScannerActivity::class.java))
        }
    }
}
