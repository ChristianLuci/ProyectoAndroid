package com.example.myapplication

import android.content.Context
import android.widget.Toast

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DatosCompartidos {

    companion object{
        var resultadoCodigoBarras:String = "";

        var listaListasCompra:ArrayList<ListaCompra> = ArrayList()
        var listaListasCompraBorradas:ArrayList<ListaCompra> = ArrayList()

    }

    data class ApiResponse(
        val success: Boolean,
        val product: Articulo?,
        val message: String?
    )


    object RetrofitClient {
        private const val IP_LOCAL = "192.168.1.133" //Cambiar aqui y en el archivo res/xml/network_security_config.xml
        private const val BASE_URL = "http://${IP_LOCAL}:5000/"

        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}