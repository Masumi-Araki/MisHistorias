package com.proyecto.historias.modelo

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.proyecto.historias.R
import java.util.Calendar

data class Personaje(
    val id: Int,
    val nombre: String,
    val rol: String,
    val gustos: String,
    val origen: String,
    val descripcion: String,
    @DrawableRes val fotoRes: Int? = null,
    val fotoUri: Uri? = null,
)

data class Historia(
    val id: Int,
    val titulo: String,
    val ultimaEdicion: String,
    val descripcion: String,
    @DrawableRes val miniaturaRes: Int? = null,
)

/** Estado de la app en memoria: usuario, historias y personajes. */
class EstadoApp {
    var nombreUsuario by mutableStateOf("")
    var fotoPerfil by mutableStateOf<Uri?>(null)
    var imagenHistoria by mutableStateOf<Uri?>(null)

    val historias = mutableStateListOf(
        Historia(
            id = 1,
            titulo = "Horizontes Eternos",
            ultimaEdicion = "18 Oct 2026",
            descripcion = "MI historia almenos la primera temporada ocurre en la dimension 1 la historia " +
                "habla sobre el protagonista el cual es una especie desconocida dentro de el planeta, " +
                "el cual sueña con descubrir su origen y el poder ayudar a los demás así como sus amigos " +
                "lo han hecho por el, de ahí nacen los demás sucesos dentro de la serie en el cual " +
                "enfrenta villanos de diferentes tipos, mientras usa su traje para buscar por algún lado " +
                "alguien que sepa que especie es o simplemente buscar información",
            miniaturaRes = R.drawable.miniatura_horizontes,
        ),
        Historia(
            id = 2,
            titulo = "Chipitin",
            ultimaEdicion = "15 Oct 2026",
            descripcion = "Toca aquí para escribir de qué trata tu historia.",
        ),
    )

    val personajes = mutableStateListOf(
        Personaje(
            id = 1,
            nombre = "Mr.P",
            rol = "Protagonista",
            gustos = "Le gustan los tacos de trompo",
            origen = "Introvertido, ansioso busca hacer lo correcto",
            descripcion = "MI historia almenos la primera temporada ocurre en la dimension 1 la historia habla sobre el.",
            fotoRes = R.drawable.avatar_mrp,
        ),
    )

    private var siguienteId = 100

    fun nuevoId(): Int = siguienteId++

    fun guardarPersonaje(p: Personaje) {
        val i = personajes.indexOfFirst { it.id == p.id }
        if (i >= 0) personajes[i] = p else personajes.add(p)
    }

    fun agregarHistoria() {
        val n = historias.size + 1
        historias.add(Historia(nuevoId(), "Nueva historia $n", fechaHoy(), "Toca aquí para escribir de qué trata tu historia."))
    }
}

private val MESES = arrayOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

fun fechaHoy(): String {
    val c = Calendar.getInstance()
    return "${c.get(Calendar.DAY_OF_MONTH)} ${MESES[c.get(Calendar.MONTH)]} ${c.get(Calendar.YEAR)}"
}
