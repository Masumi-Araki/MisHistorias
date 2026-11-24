package com.proyecto.historias

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.proyecto.historias.navegacion.AppHistorias
import com.proyecto.historias.ui.theme.HistoriasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) window.decorView.isForceDarkAllowed = false
        setContent {
            HistoriasTheme {
                AppHistorias()
            }
        }
    }
}
