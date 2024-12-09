package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.CompoundBarcodeView

class DetectorCodigosBarra : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_producto)

        val intent = Intent(this, CaptureActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_SCAN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            // Obtener el resultado del escaneo
            val scanResult = data?.getStringExtra("SCAN_RESULT")
            DatosCompartidos.resultadoCodigoBarras = scanResult.toString()

            val intent = Intent(this, CrearProducto::class.java)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        private const val REQUEST_CODE_SCAN = 1  // Código de solicitud para la actividad de escaneo
    }
}
