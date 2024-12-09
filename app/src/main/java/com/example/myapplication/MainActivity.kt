package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()
        SincronizadorDatos.subirListas(this)
        exitProcess(0)
    }

    lateinit var linearLayout: LinearLayout
    lateinit var btCrearLista: Button
    lateinit var ivImagenPerfil: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        inicializarElementos()

        btCrearLista.setOnClickListener {
            lateinit var titulo:String
            mostrarPopupConInput(this){i ->
                titulo = i
                var listaNueva = ListaCompra(titulo)
                addLista(listaNueva)
            }
        }

        if(DatosCompartidos.listaListasCompra.isNotEmpty()){
            var lista = DatosCompartidos.listaListasCompra.toList()
            runOnUiThread{
                lista.forEach { i->addLista(i) }
            }
            DatosCompartidos.listaListasCompra = ArrayList(lista)
        }

        findViewById<Button>(R.id.btVolverInicioSesion).setOnClickListener {
            val intent = Intent(this, InicioSesion::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.ivCerrarApp).setOnClickListener {
            finishAffinity()
        }

        ivImagenPerfil.setOnClickListener {
            val intent = Intent(this, PerfilUsuario::class.java)
            startActivity(intent)
        }

    }

    fun mostrarPopupConInput(context: Context, callback: (String) -> Unit) {
        val input = EditText(this)
        input.hint = "Título"

        val dialog = AlertDialog.Builder(this)
            .setTitle("Creacion de lista")
            .setMessage("Inserta el título de la lista")
            .setView(input)
            .setPositiveButton("Aceptar") { dialog, _ ->
                val titulo = input.text.toString()
                callback(titulo)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    fun inicializarElementos(){
        linearLayout = findViewById(R.id.linearLayout)
        btCrearLista = findViewById(R.id.btCrearLista)
        ivImagenPerfil = findViewById(R.id.imagenPerfil)
    }


    fun addLista(listaCompra:ListaCompra){

        if(listaCompra.usuario!=PerfilUsuario.nombreUsuario){
            return
        }

        val constraintLayout = ConstraintLayout(this).apply {
            id = View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(
                320.dpToPx(),
                114.dpToPx()
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                setMargins(0, 100, 0, 0)
            }
            setBackgroundResource(R.drawable.fondo_lista)
        }


        val titulo = TextView(this).apply {
            id = View.generateViewId()
            text = listaCompra.titulo
            textSize = 24f
            maxWidth = 550
             setTextColor(resources.getColor(android.R.color.black))
        }



        val btBorrar = Button(this).apply {
            id = View.generateViewId()
            text = "Borrar"
            textSize = 24f
            setTextColor(resources.getColor(android.R.color.black))
            setBackgroundResource(R.drawable.press_boton)
        }




        btBorrar.setOnClickListener{
            DatosCompartidos.listaListasCompra.remove(listaCompra)
            DatosCompartidos.listaListasCompraBorradas.add(listaCompra)
            linearLayout.removeView(constraintLayout)
        }



        constraintLayout.addView(titulo)
        constraintLayout.addView(btBorrar)

        val set = ConstraintSet()
        set.clone(constraintLayout)
        set.connect(titulo.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(titulo.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        set.connect(titulo.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        set.connect(titulo.id, ConstraintSet.END, btBorrar.id, ConstraintSet.START)

        set.connect(btBorrar.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(btBorrar.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        set.connect(btBorrar.id, ConstraintSet.START, titulo.id, ConstraintSet.END)
        set.connect(btBorrar.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        set.applyTo(constraintLayout)

        DatosCompartidos.listaListasCompra.add(listaCompra)



        constraintLayout.setOnClickListener {
            cambiarActivity(PantallaLista::class.java,DatosCompartidos.listaListasCompra.indexOf(listaCompra))
        }

        linearLayout.addView(constraintLayout)
    }

    //Funciones auxiliares
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }

    private fun <T : Activity> Activity.cambiarActivity(targetActivity: Class<T>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
    }

    private fun <T : Activity> Activity.cambiarActivity(targetActivity: Class<T>, listaCompra: Int) {
        val intent = Intent(this, targetActivity)
        intent.putExtra("ListaCompra",listaCompra)
        startActivity(intent)
    }
}