package com.example.myfinalproject.View

import com.example.myfinalproject.View.PedidosActivity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfinalproject.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.FirebaseApp


class MainActivity : AppCompatActivity() {

    lateinit var botao_cliente: Button
    lateinit var botao_produto: Button
    lateinit var botao_pedido: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        botao_cliente = findViewById(R.id.jButtonCliente)
        botao_produto = findViewById(R.id.jButtonProduto)
        botao_pedido = findViewById(R.id.jButtonPedido)

        setupButton(botao_cliente, ClientesActivity::class.java)
        setupButton(botao_produto, ProdutosActivity::class.java)
        setupButton(botao_pedido, PedidosActivity::class.java)
    }

    private fun setupButton(button: Button, activityClass: Class<*>) {
        button.setOnClickListener {
            Intent(this, activityClass).let { intent ->
                startActivity(intent)
            }
        }
    }
}