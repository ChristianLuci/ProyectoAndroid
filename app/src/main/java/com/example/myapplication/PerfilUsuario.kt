package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class PerfilUsuario : AppCompatActivity() {
    override fun onDestroy() {
        super.onDestroy()
        SincronizadorDatos.subirListas(this)
    }

    companion object{
        lateinit var nombreUsuario:String
        lateinit var password:String
    }

    lateinit var tvNombreUsuario:TextView
    lateinit var tvPassword:TextView
    lateinit var btSalir:Button
    lateinit var btGuardar:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_usuario)

        tvNombreUsuario = findViewById(R.id.tvNombreUsuarioPerfil)
        tvPassword = findViewById(R.id.tvPasswordPerfil)
        btSalir = findViewById(R.id.btSalirPerfil)
        btGuardar = findViewById(R.id.btGuardarPerfil)

        tvNombreUsuario.text = nombreUsuario
        tvPassword.text = password

        btSalir.setOnClickListener {
            mostrarPopup{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        btGuardar.setOnClickListener {
            SincronizadorDatos.actualizarDatos(this,tvNombreUsuario.text.toString(),tvPassword.text.toString())
            actualizarUsuariosListas()
            nombreUsuario = tvNombreUsuario.text.toString()
            password = tvPassword.text.toString()
            Toast.makeText(this,"Datos guardados correctamente",Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarUsuariosListas(){
        for(lista:ListaCompra in DatosCompartidos.listaListasCompra){
            if(lista.usuario == nombreUsuario){
                lista.usuario = tvNombreUsuario.text.toString()
            }
        }
    }

    private fun mostrarPopup(callback: () -> Unit) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Seguro que desea salir")
            .setMessage("Los cambios no guardados se perderán")
            .setPositiveButton("Aceptar") { dialog, _ ->
                callback()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }
}