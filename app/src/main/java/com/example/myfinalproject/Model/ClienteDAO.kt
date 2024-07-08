package com.example.myfinalproject.Model
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ClienteDAO {

    //CRIAÇÃO DO BANCO DE DADOS E INSTANCIA DO FIREBASE (STACK OVERFLOW TUTORIAL, NÃO VOU COMENTAR  AS OUTRASPORQUE É BASICAMENTE CLTR C + CLTR V)
    private val db = FirebaseFirestore.getInstance()
    private val clientesCollection = db.collection("clientes")

    suspend fun getAllClientes(): List<Cliente> {
        return try {
            val responseClientes = clientesCollection.get().await()
            responseClientes.documents.mapNotNull { it.toObject(Cliente::class.java) }
        } catch (e: Exception) {
            throw Exception("Erro ao buscar clientes", e)
        }
    }

    suspend fun getClienteByCpf(cpf: String): Cliente? {
        return try {
            val doc = clientesCollection.document(cpf).get().await()
            doc.toObject(Cliente::class.java)
        } catch (e: Exception) {
            throw Exception("Erro ao buscar cliente com CPF: $cpf", e)
        }
    }

    suspend fun addCliente(cliente: Cliente) {
        try {
            clientesCollection.document(cliente.cpf).set(cliente).await()
        } catch (e: Exception) {
            throw Exception("Erro ao adicionar cliente", e)
        }
    }

    suspend fun updateCliente(cliente: Cliente) {
        try {
            clientesCollection.document(cliente.cpf).set(cliente).await()
        } catch (e: Exception) {
            throw Exception("Erro ao atualizar cliente", e)
        }
    }

    suspend fun deleteCliente(cpf: String) {
        try {
            clientesCollection.document(cpf).delete().await()
        } catch (e: Exception) {
            throw Exception("Erro ao deletar cliente com CPF: $cpf", e)
        }
    }
}