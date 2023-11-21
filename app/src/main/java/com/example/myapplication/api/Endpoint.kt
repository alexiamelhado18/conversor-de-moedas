package com.example.myapplication.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoint {
    //interface Endpoint => Métodos que o Retrofit irá chamar

    @GET("/gh/fawazahmed0/currency-api@1/latest/currencies.json")
    fun listarMoedas(): Call<JsonObject>

    @GET("/gh/fawazahmed0/currency-api@1/latest/currencies/{de}/{para}.json")
    fun listarTaxasDeMoedas(
        @Path(value = "de", encoded = true) de: String,
        @Path(value = "para", encoded = true) para: String
    ): Call<JsonObject>
}