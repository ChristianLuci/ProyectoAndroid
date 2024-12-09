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

class Registrarse : AppCompatActivity() {
    lateinit var tvUsuario: EditText
    lateinit var tvAlerta: TextView
    lateinit var tvPassword: EditText
    lateinit var bVolver: Button
    lateinit var bRegistrarse: Button
    lateinit var sqlHelp: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse)
        inicializarElementos()
        val db = sqlHelp.writableDatabase
        bRegistrarse.setOnClickListener{
            lateinit var usuario:String
            lateinit var password:String
            try{
                usuario = tvUsuario.text.toString()
                password = tvPassword.text.toString()

                db.execSQL("insert into usuarios values('${usuario}','${password}')")
                tvUsuario.setText("")
                tvPassword.setText("")

                Toast.makeText(this,"Usuario registrado correctamente", Toast.LENGTH_LONG).show()
                volver()
            }catch (e:Exception){
                tvAlerta.setText("Hay que rellenar todos los datos para poder registrarse")
            }
        }
        bVolver.setOnClickListener{
            volver()
        }
    }

    private fun volver(){
        val intento = Intent(this, InicioSesion::class.java)
        startActivity(intento)
    }

    private fun inicializarElementos(){
        tvUsuario = findViewById(R.id.tvUsuario)
        tvPassword = findViewById(R.id.tvPassword)
        tvAlerta = findViewById(R.id.tvAlerta)
        bVolver = findViewById(R.id.bVolver)
        bRegistrarse = findViewById(R.id.bRegistrarse)
        sqlHelp = SQLiteHelper(this,"usuariosdb",null,1)

    }
}