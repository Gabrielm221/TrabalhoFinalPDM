package com.example.myfinalproject.Model

class Produtos( val id: String = "", val tipoDoGrao: String = "", val pontoDaTorra: String = "", val valor: Double = 0.0, val blend: Boolean = false) {

    //CONSTRUTOR DE INICIALIZAÇÃO PADRAO
    constructor() : this("", "", "", 0.0, false)

    //RESULTADO PARA EXIBIR NA TELA
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("  ID: $id\n")
        sb.append("  Tipo do Grão: $tipoDoGrao\n")
        sb.append("  Ponto da Torra: $pontoDaTorra\n")
        sb.append("  Valor: R$ ${String.format("%.2f", valor)}\n")
        sb.append("  Misturado: ${if (blend) "Sim" else "Não"}")
        return sb.toString()
    }
}