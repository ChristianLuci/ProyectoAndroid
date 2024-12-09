package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class InicioSesion : AppCompatActivity() {
    lateinit var tvUsuario:EditText
    lateinit var tvAlerta: TextView
    lateinit var tvPassword:EditText
    lateinit var bRegistrarse:Button
    lateinit var bInicioSesion:Button
    lateinit var sqlHelp: SQLiteHelper
    lateinit var usuario:String
    lateinit var password:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_sesion)
        inicializarElementos(this)
        val db = sqlHelp.writableDatabase
        bRegistrarse.setOnClickListener{
            val intento = Intent(this, Registrarse::class.java)
            startActivity(intento)
        }
        bInicioSesion.setOnClickListener{
            try{
                usuario = tvUsuario.text.toString()
                password = tvPassword.text.toString()
                if (usuario == "" || password == ""){
                    throw Exception()
                }
                val fila = db.rawQuery("select usuario,password from usuarios where usuario = '${usuario}' and password = '${password}'",
                    null)
                if(fila.moveToFirst()){
                    if(usuario == fila.getString(0) && password == fila.getString(1)){
                        ingresarApp(usuario)
                        Toast.makeText(this,"Inicio de sesión realizado correctamente",Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this,"Error al iniciar sesión",Toast.LENGTH_LONG).show()
                }
            }catch (e:Exception){
                tvAlerta.setText("Hay que rellenar todos los datos para poder iniciar sesión")
            }

        }
    }

    private fun ingresarApp(usuario:String){
        val intento = Intent(this, MainActivity::class.java)
        PerfilUsuario.nombreUsuario = usuario
        PerfilUsuario.password = password
        SincronizadorDatos.cargarListas(this)
        startActivity(intento)
    }

    private fun inicializarElementos(context: Context){
        tvUsuario = findViewById(R.id.tvUsuario)
        tvPassword = findViewById(R.id.tvPassword)
        tvAlerta = findViewById(R.id.tvAlerta)
        bRegistrarse = findViewById(R.id.bRegistrarse)
        bInicioSesion = findViewById(R.id.bIniciarSesion)
        sqlHelp = SQLiteHelper(this,"usuariosdb",null,1)


    }
}