package com.example.myfinalproject.Model

//INSTANCIA PARA INSERIR UM ARRAY DE PRODUTOS DENTRO DO PEDIDO
data class PedidoInstancia(var produto: Produtos = Produtos(), var quantidade: Int = 0) {

    constructor() : this(Produtos(), 0)

    override fun toString(): String {
        return "${produto.tipoDoGrao} - ${produto.pontoDaTorra} - ${produto.valor} - Qtd: $quantidade"
    }
}