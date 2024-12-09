package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.setPadding

class PantallaLista : AppCompatActivity() {

    lateinit var tvTituloLista:TextView
    lateinit var linearLayout:LinearLayout
    lateinit var btAddProducto:Button
    var listaCompra:ListaCompra? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pantalla_lista)

        tvTituloLista = findViewById(R.id.tvTituloLista)
        linearLayout = findViewById(R.id.linearLayout)
        btAddProducto = findViewById(R.id.btAddProducto)
        var indice = intent.extras?.get("ListaCompra") as Int
        if(indice != null){
            listaCompra = DatosCompartidos.listaListasCompra[indice]
        }

        tvTituloLista.text = listaCompra?.titulo

        listaCompra?.listaArticulos?.forEach{
            item -> addArticulo(item)
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btAddProducto.setOnClickListener {
            val intent = Intent(this, CrearProducto::class.java)
            startActivityForResult(intent,1)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK){
            return
        }

        var producto = data?.extras?.get("Producto") as Articulo

        if(listaCompra?.listaArticulos?.contains(producto) == true){
            listaCompra?.listaArticulos?.remove(producto)
        }

        listaCompra?.listaArticulos?.add(producto)
        addArticulo(producto)
    }

    fun addArticulo(articulo: Articulo){
        val constraintLayout = ConstraintLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                320.dpToPx(),
                114.dpToPx()
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                setMargins(0, 100, 0, 0)
                setPadding(10,0,0,0)
            }
            setBackgroundResource(R.drawable.fondo_lista)
        }

        constraintLayout.setOnClickListener {
            cambiarActivity(CrearProducto::class.java,articulo)
        }

        val titulo = TextView(this).apply {
            id = View.generateViewId()
            text = articulo.nombre
            textSize = 16f
            maxWidth = 600
            setTextColor(resources.getColor(android.R.color.black))
        }


        val btBorrar = Button(this).apply {
            id = View.generateViewId()
            text = "Borrar"
            textSize = 12f
            setTextColor(resources.getColor(android.R.color.black))
            setBackgroundResource(R.drawable.press_boton)
        }

        btBorrar.setOnClickListener {
            listaCompra?.listaArticulos?.remove(articulo)
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

        linearLayout.addView(constraintLayout)
    }

    //Funciones auxiliares
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }

    private fun <T : Activity> Activity.cambiarActivity(targetActivity: Class<T>, articulo: Articulo) {
        val intent = Intent(this, targetActivity)
        intent.putExtra("Producto",articulo)
        startActivity(intent)
    }
}