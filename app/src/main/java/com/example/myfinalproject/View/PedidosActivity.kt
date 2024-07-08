package com.example.myfinalproject.View

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfinalproject.Control.ClientesController
import com.example.myfinalproject.Control.PedidosController
import com.example.myfinalproject.Control.ProdutosController
import com.example.myfinalproject.Model.Cliente
import com.example.myfinalproject.Model.Pedido
import com.example.myfinalproject.Model.PedidoInstancia
import com.example.myfinalproject.Model.Produtos
import com.example.myfinalproject.R
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PedidosActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var addPedidoButton: Button
    private lateinit var removePedidoButton: Button
    private lateinit var filterButton: Button
    private lateinit var listAllButton: Button
    private lateinit var updateButton: Button
    private lateinit var addProdutoButton: Button
    private lateinit var removeProdutoButton: Button
    private lateinit var quantityEditText: EditText

    private lateinit var pedidosListView: ListView
    private lateinit var pedidoIdEditText: EditText
    private lateinit var clienteSpinner: Spinner
    private lateinit var produtosSpinner: Spinner

    private var listaClientes: List<Cliente> = emptyList()
    private var listaPedidos: List<Pedido> = emptyList()
    private var listaProdutos: List<Produtos> = emptyList()
    private var listaCarrinho: MutableList<PedidoInstancia> = mutableListOf()

    private val clientesController = ClientesController()
    private val pedidosController = PedidosController()
    private val produtosController = ProdutosController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos)

        initializeViews()

        backButton.setOnClickListener {
            finish()
        }

        fetchClientes()
        fetchPedidos()
        fetchProdutos()

        addPedidoButton.setOnClickListener {
            val pedidoId = pedidoIdEditText.text.toString()
            if (pedidoId.isEmpty()) {
                Toast.makeText(applicationContext, "É necessário fornecer um ID!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val cliente = listaClientes.getOrNull(clienteSpinner.selectedItemPosition)
            if (cliente == null) {
                Toast.makeText(applicationContext, "Selecione um Cliente válido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                val pedido = Pedido(pedidoId, Timestamp.now(), listaCarrinho, cliente)
                val success = pedidosController.createPedido(pedido)
                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(applicationContext, "Pedido adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                        fetchPedidos()
                        limparCampos()
                    } else {
                        Toast.makeText(applicationContext, "Falha ao adicionar pedido.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        addProdutoButton.setOnClickListener {
            val qtProd = quantityEditText.text.toString().toIntOrNull()
            val produtoSelecionado = listaProdutos.getOrNull(produtosSpinner.selectedItemPosition)
            if (qtProd != null && qtProd > 0 && produtoSelecionado != null) {
                val itemExistente = listaCarrinho.find { it.produto.id == produtoSelecionado.id }
                if (itemExistente != null) {
                    itemExistente.quantidade = qtProd
                } else {
                    listaCarrinho.add(PedidoInstancia(produtoSelecionado, qtProd))
                }
                val adapterCarrinho = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaCarrinho)
                pedidosListView.adapter = adapterCarrinho
            } else {
                Toast.makeText(applicationContext, "Deve haver pelo menos 1 de Produto!", Toast.LENGTH_SHORT).show()
            }
        }

        removeProdutoButton.setOnClickListener {
            val produtoSelecionado = listaProdutos.getOrNull(produtosSpinner.selectedItemPosition)
            val itemExistente = listaCarrinho.find { it.produto.id == produtoSelecionado?.id }

            if (itemExistente != null) {
                listaCarrinho.remove(itemExistente)
                val adapterCarrinho = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaCarrinho)
                pedidosListView.adapter = adapterCarrinho
            } else {
                Toast.makeText(applicationContext, "Esse Produto ainda não está no carrinho!", Toast.LENGTH_SHORT).show()
            }
        }

        removePedidoButton.setOnClickListener {
            val pedidoId = pedidoIdEditText.text.toString()
            if (pedidoId.isEmpty()) {
                Toast.makeText(applicationContext, "É necessário fornecer um ID de Pedido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                val success = pedidosController.deletePedidoById(pedidoId)
                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(applicationContext, "Pedido removido com sucesso!", Toast.LENGTH_SHORT).show()
                        fetchPedidos()
                        limparCampos()
                    } else {
                        Toast.makeText(applicationContext, "Falha ao remover pedido.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        filterButton.setOnClickListener {
            val cliente = listaClientes.getOrNull(clienteSpinner.selectedItemPosition)
            if (cliente == null) {
                Toast.makeText(applicationContext, "Selecione um Cliente válido para aplicar o filtro.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val pedidosFiltrados = listaPedidos.filter { it.cli.cpf == cliente.cpf }
            val adapterPedidos = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, pedidosFiltrados)
            pedidosListView.adapter = adapterPedidos
            Toast.makeText(applicationContext, "Exibindo ${pedidosFiltrados.size} pedidos para o cliente ${cliente.nome}", Toast.LENGTH_SHORT).show()
        }

        listAllButton.setOnClickListener {
            val adapterPedidos = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, listaPedidos)
            pedidosListView.adapter = adapterPedidos
        }

        updateButton.setOnClickListener {
            val pedidoId = pedidoIdEditText.text.toString()
            if (pedidoId.isEmpty()) {
                Toast.makeText(applicationContext, "É necessário fornecer um ID de Pedido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val cliente = listaClientes.getOrNull(clienteSpinner.selectedItemPosition)
            if (cliente == null) {
                Toast.makeText(applicationContext, "Selecione um Cliente válido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                val pedido = Pedido(pedidoId, Timestamp.now(), emptyList(), cliente)
                val success = pedidosController.updatePedido(pedido)
                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(applicationContext, "Pedido atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                        fetchPedidos()
                        limparCampos()
                    } else {
                        Toast.makeText(applicationContext, "Falha ao atualizar pedido.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initializeViews() {
        backButton = findViewById(R.id.bt_back_pd)
        addPedidoButton = findViewById(R.id.bt_add_pd)
        removePedidoButton = findViewById(R.id.bt_rm_pd)
        filterButton = findViewById(R.id.bt_f)
        listAllButton = findViewById(R.id.bt_all_pd)
        updateButton = findViewById(R.id.bt_att_pd)
        addProdutoButton = findViewById(R.id.bt_add_car)
        removeProdutoButton = findViewById(R.id.bt_rm_car)

        pedidosListView = findViewById(R.id.lt_pd)
        pedidoIdEditText = findViewById(R.id.text_id_pd)
        clienteSpinner = findViewById(R.id.dd_cli)
        produtosSpinner = findViewById(R.id.dd_prod)
        quantityEditText = findViewById(R.id.text_qt_prod)
    }

    private fun fetchClientes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                listaClientes = clientesController.getAllClientes()
                val nomesCpf = listaClientes.map { "${it.nome} - ${it.cpf}" }
                withContext(Dispatchers.Main) {
                    val adapterClientes = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, nomesCpf)
                    adapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    clienteSpinner.adapter = adapterClientes
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Não foi possível buscar os clientes!", Toast.LENGTH_SHORT).show()
                }
                Log.e("Activity_Pedidos", "Error fetching clientes", e)
            }
        }
    }

    private fun fetchPedidos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                listaPedidos = pedidosController.getAllPedidos()
                withContext(Dispatchers.Main) {
                    val adapterPedidos = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, listaPedidos)
                    pedidosListView.adapter = adapterPedidos
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Não foi possível buscar os pedidos!", Toast.LENGTH_SHORT).show()
                }
                Log.e("Activity_Pedidos", "Error fetching pedidos", e)
            }
        }
    }

    private fun fetchProdutos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                listaProdutos = produtosController.getAllProdutos()
                withContext(Dispatchers.Main) {
                    val adapterProdutos = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, listaProdutos)
                    adapterProdutos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    produtosSpinner.adapter = adapterProdutos
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Não foi possível buscar os produtos!", Toast.LENGTH_SHORT).show()
                }
                Log.e("Activity_Pedidos", "Error fetching produtos", e)
            }
        }
    }

    private fun limparCampos() {
        pedidoIdEditText.text.clear()
        quantityEditText.text.clear()

        listaCarrinho.clear()
        val adapterCarrinho = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaCarrinho)
        pedidosListView.adapter = adapterCarrinho
    }
}
