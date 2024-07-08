package com.example.myfinalproject.Model

import com.google.firebase.Timestamp
//PEDIDO TENDO COMO PARAMETRO O ID, A DATA QUE FOI FEITA A COMPRA, UMA LISTA DO TIPO DA INSTANCIA QUE CITEI, PARA PASSARMOS COMO PARAMETRO LÁ NO PEDIDO
class Pedido(val id: String = "", val data: Timestamp = Timestamp.now(), val produtos: List<PedidoInstancia> = emptyList(), val cli: Cliente = Cliente()) {

    //CONSTRUTOR DE INICIALIZAÇÃO PADRAO
    constructor() : this("", Timestamp.now(), emptyList(), Cliente())

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("ID: $id\n")
        sb.append("Data: ${data.toDate().toString()}\n")
        sb.append("Cliente: ${cli.nome}\n")
        sb.append("Produtos:\n")
        produtos.forEach {
            sb.append("  - ${it.produto.id}: ${it.produto.tipoDoGrao}, ")
            sb.append("Quantidade: ${it.quantidade}, ")
            sb.append("Ponto da Torra: ${it.produto.pontoDaTorra}, ")
            sb.append("Valor: R$ ${String.format("%.2f", it.produto.valor)}, ")
            sb.append("Misturado: ${if (it.produto.blend) "Sim" else "Não"}\n")
        }
        sb.append("==================\n")
        return sb.toString()
    }
}