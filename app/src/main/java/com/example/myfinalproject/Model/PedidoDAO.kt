import com.example.myfinalproject.Model.Pedido
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PedidoDAO {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("pedidos")

    // Função para criar um novo pedido
    suspend fun createPedido(pedido : Pedido): Boolean {
        return try {
            collection.document(pedido.id).set(pedido).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Função para ler um pedido pelo ID
    suspend fun getPedidoById(id: String): Pedido? {
        return try {
            val document = collection.document(id).get().await()
            document.toObject(Pedido::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Função para ler todos os pedidos
    suspend fun getAllPedidos(): List<Pedido> {
        return try {
            val result = collection.get().await()
            result.documents.mapNotNull { it.toObject(Pedido::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Função para atualizar um pedido existente
    suspend fun updatePedido(pedido: Pedido): Boolean {
        return try {
            collection.document(pedido.id).set(pedido).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    // Função para excluir um pedido pelo ID
    suspend fun deletePedidoById(id: String): Boolean {
        return try {
            collection.document(id).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}