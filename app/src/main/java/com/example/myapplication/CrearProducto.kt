package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearProducto : AppCompatActivity() {

    val c = this

    lateinit var etNombre:EditText
    lateinit var etMarca:EditText
    lateinit var etPrecio:EditText
    lateinit var btGuardarSalir:Button
    lateinit var btCancelar:Button
    lateinit var producto: Articulo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_producto)

        btGuardarSalir = findViewById(R.id.btGuardarSalir)
        btCancelar = findViewById(R.id.btCancelar)
        etNombre = findViewById(R.id.etNombreProducto)
        etMarca = findViewById(R.id.etMarca)
        etPrecio = findViewById(R.id.etPrecio)

        val productoCargar = intent.extras?.get("Producto") as? Articulo

        if(productoCargar != null){
            etNombre.setText(productoCargar.nombre)
            etMarca.setText(productoCargar.marca)
            etPrecio.setText(productoCargar.precio.toString())
        }

        producto = Articulo()

        findViewById<Button>(R.id.btEscanear).setOnClickListener {
            usarCodigoBarras()
        }

        btGuardarSalir.setOnClickListener {
            try{
                producto.nombre = etNombre.text.toString()
                producto.marca = etMarca.text.toString()
                producto.precio = etPrecio.text.toString().toDouble()
                val intent = Intent(this, PantallaLista::class.java)
                intent.putExtra("Producto", producto)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }catch (e:NumberFormatException){
                Toast.makeText(this,"Los datos ingresados no son validos o los campos están vaciosFT, intentelo de nuevo",Toast.LENGTH_SHORT).show()
            }
        }

        btCancelar.setOnClickListener {
            val intent = Intent(this, PantallaLista::class.java)
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val barcode = DatosCompartidos.resultadoCodigoBarras
        val api = DatosCompartidos.RetrofitClient.instance

        api.getProductInfo(barcode).enqueue(object : Callback<DatosCompartidos.ApiResponse> {
            override fun onResponse(call: Call<DatosCompartidos.ApiResponse>, response: Response<DatosCompartidos.ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.success == true) {
                        val product = apiResponse.product

                        etNombre.setText(product?.nombre.toString())
                        etMarca.setText(product?.marca.toString())

                        runOnUiThread {
                            Toast.makeText(c,"Producto cargado correctamente, inserte el precio actual del producto",Toast.LENGTH_LONG).show()
                        }

                    } else {
                        runOnUiThread {
                            Toast.makeText(c,"Error cargando el producto",Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(c,"Error en la respuesta de la api",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<DatosCompartidos.ApiResponse>, t: Throwable) {
                Log.e("Error", "Error en la conexión: ${t.message}")
            }
        })
    }

    fun usarCodigoBarras(){
        val intent = Intent(this, DetectorCodigosBarra::class.java)
        startActivityForResult(intent,1)
    }
}