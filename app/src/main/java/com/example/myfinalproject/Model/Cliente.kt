package com.example.myfinalproject.Model

class Cliente(val nome: String,val cpf: String, val telefone: String, val endereco: String, val instagram: String) {

    //CONSTRUTOR DE INICIALIZAÇÃO PADRAO (STACK OVERFLOW TUTORIAL, NÃO VOU COMENTAR AS OUTRAS PORQUE É BASICAMENTE CLTR C + CLTR V))

    constructor() : this("", "", "", "", "")

    //TO STRING PADRÃO

    override fun toString(): String {
        return  " Nome: $nome\n"+
                " CPF: $cpf\n" +
                " Telefone: $telefone\n" +
                " Endereço: $endereco\n" +
                " Instagram: $instagram"
    }
}