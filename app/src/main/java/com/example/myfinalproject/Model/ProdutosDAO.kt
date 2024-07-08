package com.example.myfinalproject.Model

import com.example.myfinalproject.Model.Produtos
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ProdutosDAO {

    private val db = Firebase.firestore
    private val produtosCollection = db.collection("produtos")


    // Método para buscar todos os produtos
    suspend fun getAllProdutos(): List<Produtos> {
        return try {
            val responseProdutos = produtosCollection.get().await()
            responseProdutos.documents.mapNotNull { it.toObject(Produtos::class.java) }
        } catch (e: Exception) {
            throw FirebaseException("Error fetching produtos", e)
        }
    }

    // Método para buscar um produto pelo ID
    suspend fun getProdutoById(id: String): Produtos? {
        return try {
            val doc = produtosCollection.document(id).get().await()
            doc.toObject(Produtos::class.java)
        } catch (e: Exception) {
            throw FirebaseException("Error fetching produto with ID: $id", e)
        }
    }

    // Método para adicionar um novo produto
    suspend fun addProduto(produto: Produtos) {
        try {
            produtosCollection.document(produto.id).set(produto).await()
        } catch (e: Exception) {
            throw FirebaseException("Error adding produto", e)
        }
    }

    // Método para atualizar um produto existente
    suspend fun updateProduto(produto: Produtos) {
        try {
            produtosCollection.document(produto.id).set(produto).await()
        } catch (e: Exception) {
            throw FirebaseException("Error updating produto", e)
        }
    }

    // Método para deletar um produto pelo ID
    suspend fun deleteProduto(id: String) {
        try {
            produtosCollection.document(id).delete().await()
        } catch (e: Exception) {
            throw FirebaseException("Error deleting produto with ID: $id", e)
        }
    }
}