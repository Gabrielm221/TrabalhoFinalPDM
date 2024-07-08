package com.example.myfinalproject.View

import com.example.myfinalproject.Model.Cliente
import com.example.myfinalproject.Control.ClientesController
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfinalproject.R
import com.example.myfinalproject.View.ClientesActivity.ActionType.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientesActivity : AppCompatActivity() {

    // Criação dos campos de requisição do cliente
    lateinit var nome: EditText
    lateinit var cpf: EditText
    lateinit var telefone: EditText
    lateinit var endereco: EditText
    lateinit var instagram: EditText

    // Criação dos botões para realização das ações
    lateinit var botaoAdc: ImageButton
    lateinit var botaoBus: ImageButton
    lateinit var botaoExc: ImageButton
    lateinit var botaoLis: ImageButton
    lateinit var botaoAlt: ImageButton
    lateinit var botaoVoltar: ImageButton

    var listaClientes: List<Cliente> = listOf()
    var adapClientes: ArrayAdapter<Cliente>? = null

    lateinit var clientesListView: ListView

    val clientesController = ClientesController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Vinculando os campos de requisição da página
        nome = findViewById(R.id.jEditNome)
        cpf = findViewById(R.id.jEditCpf)
        telefone = findViewById(R.id.jEditTel)
        endereco = findViewById(R.id.jEditEndereco)
        instagram = findViewById(R.id.jEditInsta)

        // Vinculando os botões de ações da página
        botaoAdc = findViewById(R.id.jButtonAdicionar)
        botaoBus = findViewById(R.id.jButtonBuscar)
        botaoExc = findViewById(R.id.jButtonExcluir)
        botaoLis = findViewById(R.id.jButtonListar)
        botaoAlt = findViewById(R.id.jButtonAlterar)
        botaoVoltar = findViewById(R.id.jButtonVoltar)

        // Vinculando a lista da página para exibição dos clientes
        clientesListView = findViewById(R.id.clientList)

        setupButton(botaoVoltar, VOLTAR)
        setupButton(botaoAdc, ADICIONAR)
        setupButton(botaoExc, EXCLUIR)
        setupButton(botaoLis, LISTAR)
        setupButton(botaoBus, BUSCAR)
        setupButton(botaoAlt, ALTERAR)
    }

    private fun setupButton(button: ImageButton, actionType: ActionType) {
        button.setOnClickListener {
            val nomeText = nome.text.toString().trim()
            val cpfText = cpf.text.toString().trim()
            val telefoneText = telefone.text.toString().trim()
            val enderecoText = endereco.text.toString().trim()
            val instagramText = instagram.text.toString().trim()

            val instanciaCliente = Cliente(nomeText, cpfText, telefoneText, enderecoText, instagramText)

            when (actionType) {
                VOLTAR -> finish()

                ADICIONAR -> {
                    if (cpfText.isNotBlank() && nomeText.isNotBlank() && telefoneText.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                clientesController.addCliente(instanciaCliente)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Cliente adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                                    limparCampos()
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Erro ao adicionar cliente!", Toast.LENGTH_SHORT).show()
                                }
                                Log.e("Activity_Clientes", "Error adding cliente", e)
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
                    }
                }
                EXCLUIR -> {
                    if (cpfText.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                clientesController.deleteCliente(cpfText)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Cliente excluído com sucesso!", Toast.LENGTH_SHORT).show()
                                    limparCampos()
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Erro ao excluir cliente!", Toast.LENGTH_SHORT).show()
                                }
                                Log.e("ClientesActivity", "Error deleting cliente", e)
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, "CPF do cliente não pode estar vazio!", Toast.LENGTH_SHORT).show()
                    }
                }
                LISTAR -> {
                    adapClientes = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaClientes)
                    clientesListView.adapter = adapClientes

                    mostrarClientes()

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            listaClientes = clientesController.getAllClientes()
                            withContext(Dispatchers.Main) {
                                adapClientes = ArrayAdapter(this@ClientesActivity, android.R.layout.simple_list_item_1, listaClientes)
                                clientesListView.adapter = adapClientes
                                Toast.makeText(applicationContext, "Clientes listados com sucesso!", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Erro ao listar clientes!", Toast.LENGTH_SHORT).show()
                            }
                            Log.e("ClientesActivity", "Error listing clientes", e)
                        }
                    }
                }
                BUSCAR -> {
                    if (cpfText.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val cliente = clientesController.getClienteByCPF(cpfText)
                                withContext(Dispatchers.Main) {
                                    if (cliente != null) {
                                        listaClientes = listOf(cliente)
                                        adapClientes = ArrayAdapter(this@ClientesActivity, android.R.layout.simple_list_item_1, listaClientes)
                                        clientesListView.adapter = adapClientes
                                        Toast.makeText(applicationContext, "Cliente encontrado!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        listaClientes = listOf()
                                        adapClientes = ArrayAdapter(this@ClientesActivity, android.R.layout.simple_list_item_1, listaClientes)
                                        clientesListView.adapter = adapClientes
                                        Toast.makeText(applicationContext, "Cliente não encontrado!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Erro ao buscar cliente!", Toast.LENGTH_SHORT).show()
                                }
                                Log.e("ClientesActivity", "Error fetching cliente", e)
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, "É necessário informar o CPF!", Toast.LENGTH_SHORT).show()
                    }
                }

                ALTERAR -> {
                    if (cpfText.isNotBlank() && nomeText.isNotBlank() && telefoneText.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                clientesController.updateCliente(instanciaCliente)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Cliente alterado com sucesso!", Toast.LENGTH_SHORT).show()
                                    limparCampos()
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Erro ao alterar cliente!", Toast.LENGTH_SHORT).show()
                                }
                                Log.e("ClientesActivity", "Error updating cliente", e)
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun mostrarClientes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                listaClientes = clientesController.getAllClientes()
                withContext(Dispatchers.Main) {
                    adapClientes = ArrayAdapter(this@ClientesActivity, android.R.layout.simple_list_item_1, listaClientes)
                    clientesListView.adapter = adapClientes
                    Toast.makeText(applicationContext, "Clientes buscados com sucesso!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Não foi possível buscar os clientes!", Toast.LENGTH_SHORT).show()
                }
                Log.e("Activity_Clientes", "Error fetching clientes", e)
            }
        }
    }

    private fun limparCampos() {
        nome.text.clear()
        cpf.text.clear()
        telefone.text.clear()
        endereco.text.clear()
        instagram.text.clear()
    }

    enum class ActionType {
        ADICIONAR,
        BUSCAR,
        EXCLUIR,
        LISTAR,
        ALTERAR,
        VOLTAR
    }
}
