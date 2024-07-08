package com.example.myfinalproject.Control

import PedidoDAO
import com.example.myfinalproject.Model.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PedidosController {
    private val pedidoDao = PedidoDAO()

    //CRIAR UM PEDIDO
    suspend fun createPedido(pedido: Pedido): Boolean {
        return withContext(Dispatchers.IO) {
            pedidoDao.createPedido(pedido)
        }
    }

    //ATUALIZAR UM PEDIDO (USADO PARA NA VERDADE EXCLUIR UM PEDIDO COM VINCULO)
    suspend fun updatePedido(pedido: Pedido): Boolean {
        return withContext(Dispatchers.IO) {
            pedidoDao.updatePedido(pedido)
        }
    }

    //DELETAR PEDIDO
    suspend fun deletePedidoById(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            pedidoDao.deletePedidoById(id)
        }
    }

    //PEGAR TODOS OS PEDIDOS
    suspend fun getAllPedidos(): List<Pedido> {
        return withContext(Dispatchers.IO) {
            pedidoDao.getAllPedidos()
        }
    }

    //PEGAR PEDIDO POR ID (USADO PARA A LISTA DE EXIBIÇÃO EM PEDIDOS)
    suspend fun getPedidoById(id: String): Pedido? {
        return withContext(Dispatchers.IO) {
            pedidoDao.getPedidoById(id)
        }
    }

}