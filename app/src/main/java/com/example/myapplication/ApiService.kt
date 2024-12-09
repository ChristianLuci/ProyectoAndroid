package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("getProductInfo")
    fun getProductInfo(@Query("barcode") barcode: String): Call<DatosCompartidos.ApiResponse>
}