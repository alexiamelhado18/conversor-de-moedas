package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.myapplication.api.Endpoint
import com.example.myapplication.util.NetworkUtils
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    private val retrofitClient = NetworkUtils.getRetrofitInstance("https://cdn.jsdelivr.net/");
    private val endpoint = retrofitClient.create(Endpoint::class.java);

    //
    private lateinit var spFrom: Spinner;
    private lateinit var spTo: Spinner;
    private lateinit var btConvert: Button;
    private lateinit var tvResult: TextView;
    private lateinit var etValueFrom: EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //
        spFrom = findViewById(R.id.spFrom);
        spTo = findViewById(R.id.spTo);
        btConvert = findViewById(R.id.btConvert);
        tvResult = findViewById(R.id.tvResult);
        etValueFrom = findViewById(R.id.etValueFrom);

        //invoca o método
        listarMoedas();

        //
        btConvert.setOnClickListener { conversorDeMoedas() };
    }

    fun conversorDeMoedas() {
        endpoint.listarTaxasDeMoedas(spFrom.selectedItem.toString(), spTo.selectedItem.toString())
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    var data = response.body()?.entrySet()
                        ?.find { it.key == spTo.selectedItem.toString() };

                    var taxa: Double = data?.value.toString().toDouble();

                    var conversao = etValueFrom.text.toString().toDouble() * taxa;

                    tvResult.setText(conversao.toString());
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun listarMoedas() {
        endpoint.listarMoedas()
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    var data = mutableListOf<String>();
                    response.body()?.keySet()?.iterator()?.forEach {
                        data.add(it);
                    }

                    //
                    val posBRL = data.indexOf("brl");
                    val posUSD = data.indexOf("usd");

                    //
                    val adapter = ArrayAdapter(
                        baseContext,
                        android.R.layout.simple_spinner_dropdown_item,
                        data
                    );

                    //
                    spFrom.adapter = adapter;
                    spTo.adapter = adapter;

                    //
                    spFrom.setSelection(posBRL);
                    spTo.setSelection(posUSD);
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    println("Não foi");
                }

            });
    }
}