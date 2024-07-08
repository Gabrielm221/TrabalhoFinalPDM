package com.example.myfinalproject.View

import com.example.myfinalproject.Model.Produtos
import com.example.myfinalproject.Control.ProdutosController

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.myfinalproject.R
import com.example.myfinalproject.View.ProdutosActivity.ActionType.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProdutosActivity : AppCompatActivity() {

    // Criação dos campos de requisição do produto
    lateinit var id_produto: EditText
    lateinit var tipo_grao: EditText
    lateinit var ponto_torra: EditText
    lateinit var valor: EditText
    lateinit var blend: ToggleButton

    // Criação dos botões para realização das ações
    lateinit var botao_adcProduto: ImageButton
    lateinit var botao_busProduto: ImageButton
    lateinit var botao_excProduto: ImageButton
    lateinit var botao_lisProduto: ImageButton
    lateinit var botao_altProduto: ImageButton
    lateinit var botao_voltar: ImageButton

    var listaProdutos: List<Produtos> = listOf()
    var adapProdutos: ArrayAdapter<Produtos>? = null

    lateinit var produtosListView: ListView

    val pdController = ProdutosController();



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produtos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Vinculando os campos de requisição da página
        id_produto = findViewById(R.id.jEditIdProduto)
        tipo_grao = findViewById(R.id.jEditTipoGrao)
        ponto_torra = findViewById(R.id.jEditPontoTorra)
        valor = findViewById(R.id.jEditValor)
        blend = findViewById(R.id.jToggleBlend)

        // Vinculando os botões de ações da página
        botao_adcProduto = findViewById(R.id.jButtonAdicionarProduto)
        botao_busProduto = findViewById(R.id.jButtonBuscarProduto)
        botao_excProduto = findViewById(R.id.jButtonExcluirProduto)
        botao_lisProduto = findViewById(R.id.jButtonListarProdutos)
        botao_altProduto = findViewById(R.id.jButtonAlterarProduto)
        botao_voltar = findViewById(R.id.jButtonVoltarProdutos)

        // Vinculando a lista da página para exibição dos produtos
        produtosListView = findViewById(R.id.produtosList)

        setupButton(botao_voltar, VOLTAR)
        setupButton(botao_adcProduto, ADICIONAR)
        setupButton(botao_excProduto, EXCLUIR)
        setupButton(botao_lisProduto, LISTAR)
        setupButton(botao_busProduto, BUSCAR)
        setupButton(botao_altProduto, ALTERAR)

    }

    private fun setupButton(button: ImageButton, actionType: ActionType) {

        button.setOnClickListener {
            val id_produtoText = id_produto.text.toString().trim()
            val tipo_graoText = tipo_grao.text.toString().trim()
            val ponto_torraText = ponto_torra.text.toString().trim()
            val valorText = valor.text.toString().toDoubleOrNull() ?: 0.0
            val blendStatus = blend.isChecked

            val instanciaProduto = Produtos(id_produtoText, tipo_graoText, ponto_torraText, valorText, blendStatus)

            when (actionType) {
                VOLTAR -> finish()

                ADICIONAR -> {
                    if (id_produtoText.isNotBlank() && tipo_graoText.isNotBlank() && ponto_torraText.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                pdController.addProduto(instanciaProduto)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Produto adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                                    limparCampos()
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Erro ao adicionar produto!", Toast.LENGTH_SHORT).show()
                                }
                                Log.e("Activity_Produtos", "Error adding produto", e)
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                    }
                }
                EXCLUIR -> {
                    if (id_produtoText.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                pdController.deleteProduto(id_produtoText)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Produto excluído com sucesso!", Toast.LENGTH_SHORT).show()
                                    limparCampos()
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Erro ao excluir produto!", Toast.LENGTH_SHORT).show()
                                }
                                Log.e("ProdutosActivity", "Error deleting produto", e)
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, "ID do produto não pode estar vazio!", Toast.LENGTH_SHORT).show()
                    }
                }
                LISTAR -> {

                    adapProdutos = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaProdutos)
                    produtosListView.adapter = adapProdutos

                    MostrarProdutos()

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            // Adicione o código para listar os produtos do banco de dados
                            listaProdutos = listOf() // Substitua pelo código para obter a lista de produtos do banco de dados
                            withContext(Dispatchers.Main) {
                                adapProdutos = ArrayAdapter(this@ProdutosActivity, android.R.layout.simple_list_item_1, listaProdutos)
                                produtosListView.adapter = adapProdutos
                                Toast.makeText(applicationContext, "Produtos listados com sucesso!", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Erro ao listar produtos!", Toast.LENGTH_SHORT).show()
                            }
                            Log.e("ProdutosActivity", "Error listing produtos", e)
                        }
                    }
                }
                BUSCAR -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val produto = pdController.getProdutoById(id_produtoText)
                            withContext(Dispatchers.Main) {
                                if (produto != null) {
                                    listaProdutos = listOf(produto) // Atualiza a lista apenas com o produto encontrado
                                    adapProdutos = ArrayAdapter(this@ProdutosActivity, android.R.layout.simple_list_item_1, listaProdutos)
                                    produtosListView.adapter = adapProdutos
                                    Toast.makeText(applicationContext, "Produto encontrado!", Toast.LENGTH_SHORT).show()
                                } else {
                                    listaProdutos = listOf() // Limpa a lista se o produto não foi encontrado
                                    adapProdutos = ArrayAdapter(this@ProdutosActivity, android.R.layout.simple_list_item_1, listaProdutos)
                                    produtosListView.adapter = adapProdutos
                                    Toast.makeText(applicationContext, "Produto não encontrado!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Erro ao buscar produto!", Toast.LENGTH_SHORT).show()
                            }
                            Log.e("ProdutosActivity", "Error fetching produto", e)
                        }
                    }
                }

                ALTERAR -> {
                    if (id_produtoText.isNotBlank() && tipo_graoText.isNotBlank() && ponto_torraText.isNotBlank()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                pdController.updateProduto(instanciaProduto)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Produto alterado com sucesso!", Toast.LENGTH_SHORT).show()
                                    limparCampos()
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(applicationContext, "Erro ao alterar produto!", Toast.LENGTH_SHORT).show()
                                }
                                Log.e("ProdutosActivity", "Error updating produto", e)
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun MostrarProdutos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                listaProdutos = pdController.getAllProdutos()
                withContext(Dispatchers.Main) {
                    adapProdutos = ArrayAdapter(this@ProdutosActivity, android.R.layout.simple_list_item_1, listaProdutos)
                    produtosListView.adapter = adapProdutos
                    Toast.makeText(applicationContext, "Produtos buscados com sucesso!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Não foi possível buscar os produtos!", Toast.LENGTH_SHORT).show()
                }
                Log.e("Activity_Produtos", "Error fetching produtos", e)
            }
        }
    }

    private fun limparCampos() {
        id_produto.text.clear()
        tipo_grao.text.clear()
        ponto_torra.text.clear()
        valor.text.clear()
        blend.isChecked = false
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
