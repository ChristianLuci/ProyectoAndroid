package com.example.myapplication

import java.io.Serializable




class ListaCompra:Serializable {
    lateinit var titulo:String
    var usuario:String
    var listaArticulos:ArrayList<Articulo>
    constructor(){
        usuario = PerfilUsuario.nombreUsuario
        listaArticulos = ArrayList()
    }
    constructor(titulo: String) {
        usuario = PerfilUsuario.nombreUsuario
        this.titulo = titulo
        listaArticulos = ArrayList()
    }

}