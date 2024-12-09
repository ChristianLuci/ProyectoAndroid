package com.example.myapplication

import android.content.Context
import android.database.sqlite.SQLiteException
import android.util.Log
import android.widget.Toast


//Nombre_Articulo;MarcaArticulo;PrecioArticulo

class SincronizadorDatos {

    companion object{
        fun actualizarDatos(context: Context, usuario:String, password:String){
            val sqLiteHelper = SQLiteHelper(context,"usuariosdb",null,1)
            val db = sqLiteHelper.writableDatabase
            db.execSQL("update usuarios set usuario = '${usuario}', password = '${password}' where usuario = '${PerfilUsuario.nombreUsuario}'")
            db.close()
        }

        fun subirListas(context: Context){
            val sqLiteHelper = SQLiteHelper(context,"usuariosdb",null,1)
            val db = sqLiteHelper.writableDatabase
            for(listaBorrada:ListaCompra in DatosCompartidos.listaListasCompraBorradas){
                db.execSQL("delete from nombreListas where nombreLista = '${listaBorrada.titulo}'")
            }
            val listaListas = DatosCompartidos.listaListasCompra.toTypedArray()
            var datosArticulos = ""
            for(lista:ListaCompra in listaListas){
                datosArticulos = ""
                for(articulo:Articulo in lista.listaArticulos){
                    datosArticulos += "${articulo.nombre};${articulo.marca};${articulo.precio};"
                }
                try{
                    db.execSQL("insert into nombreListas values(" +
                            "'${PerfilUsuario.nombreUsuario}'," +
                            "'${lista.titulo}'," +
                            "'${datosArticulos}'" +
                            ")")

                }catch (s:SQLiteException){
                    Log.DEBUG //no hace nada, pero es para los valores repetidos
                }
            }
            db.close()
            Toast.makeText(context,"Datos subidos correctamente", Toast.LENGTH_LONG).show()
        }

        fun cargarListas(context: Context){
            val sqLiteHelper = SQLiteHelper(context,"usuariosdb",null,1)
            val db = sqLiteHelper.writableDatabase
            val cursor = db.rawQuery("select * from nombreListas",null)
            var tempListasCompra = ArrayList<ListaCompra>()

            if(cursor.moveToFirst()){
                do{
                    val listaCompra = ListaCompra(cursor.getString(1))
                    listaCompra.usuario = cursor.getString(0)
                    val datos = cursor.getString(2).split(";")
                    for(i in 0 until datos.size-1 step 3){
                        val tempArticulo = Articulo()
                        tempArticulo.nombre = datos[i]
                        tempArticulo.marca = datos[i+1]
                        tempArticulo.precio = datos[i+2].toDouble()
                        listaCompra.listaArticulos.add(tempArticulo)
                    }
                    tempListasCompra.add(listaCompra)

                }while(cursor.moveToNext())
            }
            DatosCompartidos.listaListasCompra.clear()
            DatosCompartidos.listaListasCompra = tempListasCompra
            db.close()
        }
    }
}