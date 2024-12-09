package com.example.myapplication

import java.io.Serializable

class Articulo : Serializable {
    lateinit var nombre:String
    lateinit var marca:String
    var precio:Double = 0.0
}