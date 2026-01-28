package com.gsledger.app

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object Storage {

    private const val PREF_NAME = "GS_LEDGER_PREFS"
    private const val KEY_TRANSACTIONS = "transactions"

    // 🔥 AGORA COM ORIGEM (mas compatível com versões antigas)
    fun saveTransaction(
        context: Context,
        descricao: String,
        valor: String,
        tipo: String,
        origem: String = "Manual"
    ) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonArray = JSONArray(prefs.getString(KEY_TRANSACTIONS, "[]"))

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dataAtual = dateFormat.format(Date())

        val transaction = JSONObject().apply {
            put("descricao", descricao)
            put("valor", valor)
            put("data", dataAtual)
            put("tipo", tipo)
            put("origem", origem) // 🆕 NOVO CAMPO
        }

        jsonArray.put(transaction)
        prefs.edit().putString(KEY_TRANSACTIONS, jsonArray.toString()).apply()
    }

    fun getTransactions(context: Context): JSONArray {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return JSONArray(prefs.getString(KEY_TRANSACTIONS, "[]"))
    }

    fun deleteTransaction(context: Context, index: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonArray = JSONArray(prefs.getString(KEY_TRANSACTIONS, "[]"))

        val newArray = JSONArray()
        for (i in 0 until jsonArray.length()) {
            if (i != index) {
                newArray.put(jsonArray.getJSONObject(i))
            }
        }

        prefs.edit().putString(KEY_TRANSACTIONS, newArray.toString()).apply()
    }
}
