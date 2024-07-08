package com.example.myfinalproject.Control

import com.example.myfinalproject.Model.ProdutosDAO;
import com.example.myfinalproject.Model.Cliente
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.myfinalproject.Model.Produtos
import com.example.myfinalproject.View.ClientesActivity.ActionType.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProdutosController {
    private val pDao = ProdutosDAO()

    //CRIAR PRODUTO

    suspend fun addProduto(produto: Produtos) {
        withContext(Dispatchers.IO) {
            pDao.addProduto(produto)
        }
    }

    //DELETAR PRODUTO
    suspend fun deleteProduto(id: String) {
        withContext(Dispatchers.IO) {
            pDao.deleteProduto(id)
        }
    }

    //PEGAR TODOS OS PRODUTOS
    suspend fun getAllProdutos(): List<Produtos> {
        return withContext(Dispatchers.IO) {
            pDao.getAllProdutos()
        }
    }

    //PEGAR PRODUTO POR ID
    suspend fun getProdutoById(id: String): Produtos? {
        return withContext(Dispatchers.IO) {
            pDao.getProdutoById(id)
        }
    }

    //ATUALIZAR PRODUTO
    suspend fun updateProduto(produto: Produtos) {
        withContext(Dispatchers.IO) {
            pDao.updateProduto(produto)
        }
    }
}
