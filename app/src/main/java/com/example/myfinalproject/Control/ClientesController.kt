package com.example.myfinalproject.Control
import com.example.myfinalproject.Model.ClienteDAO
import com.example.myfinalproject.Model.Cliente
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.myfinalproject.View.ClientesActivity.ActionType.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//CONTROLADORES PARA ACESSO DIRETO A FUNÇÃO (JEFFERSON PEDIU PARA USAR COROUTINES), REPRESENTADAS AQUI POR FUNÇÕES SUSPEND
class ClientesController {
    val cDAO = ClienteDAO()


    //ADD CLIENTE
    suspend fun addCliente(cliente: Cliente) {
        withContext(Dispatchers.IO) {
            cDAO.addCliente(cliente)
        }
    }

    //DELETAR CLIENTE
    suspend fun deleteCliente(cpf: String) {
        withContext(Dispatchers.IO) {
            cDAO.deleteCliente(cpf)
        }
    }

    //QATUALIZAR CLIENTE
    suspend fun updateCliente(cliente: Cliente) {
        withContext(Dispatchers.IO) {
            cDAO.updateCliente(cliente)
        }
    }

    //PEGAR DADOS POR CPF
    suspend fun getClienteByCPF(cpf:String):Cliente?{
        return withContext(Dispatchers.IO){
            cDAO.getClienteByCpf(cpf);
        }
    }

    //PEGAR TODOS OS CLIENTES
    suspend fun getAllClientes():List<Cliente>
    {
        return withContext(Dispatchers.IO) {
            cDAO.getAllClientes()
        }
    }

}