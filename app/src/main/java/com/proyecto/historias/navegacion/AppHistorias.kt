package com.proyecto.historias.navegacion

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.proyecto.historias.modelo.EstadoApp
import com.proyecto.historias.ui.componentes.Seccion
import com.proyecto.historias.ui.pantallas.PantallaCarga
import com.proyecto.historias.ui.pantallas.PantallaGaleria
import com.proyecto.historias.ui.pantallas.PantallaHistorias
import com.proyecto.historias.ui.pantallas.PantallaInicio
import com.proyecto.historias.ui.pantallas.PantallaMapa
import com.proyecto.historias.ui.pantallas.PantallaNombre
import com.proyecto.historias.ui.pantallas.PantallaPersonaje

sealed interface Pantalla {
    data object Inicio : Pantalla
    data object Carga : Pantalla
    data object Nombre : Pantalla
    data object Historias : Pantalla
    data object Galeria : Pantalla
    data object Mapa : Pantalla
    data object CrearPersonaje : Pantalla
    data class DetallePersonaje(val id: Int) : Pantalla
}

/** Navegación con una pila simple de pantallas (el botón atrás del sistema regresa una). */
@Composable
fun AppHistorias() {
    val estado = remember { EstadoApp() }
    val pila = remember { mutableStateListOf<Pantalla>(Pantalla.Inicio) }
    val context = LocalContext.current

    fun ir(p: Pantalla) {
        if (pila.last() != p) pila.add(p)
    }

    fun reiniciarEn(p: Pantalla) {
        pila.clear()
        pila.add(p)
    }

    fun atras() {
        if (pila.size > 1) pila.removeAt(pila.lastIndex)
    }

    fun irSeccion(s: Seccion) {
        val destino = when (s) {
            Seccion.Mapa -> Pantalla.Mapa
            Seccion.Galeria -> Pantalla.Galeria
            Seccion.Historias -> Pantalla.Historias
        }
        val i = pila.lastIndexOf(destino)
        if (i >= 0) {
            while (pila.lastIndex > i) pila.removeAt(pila.lastIndex)
        } else {
            pila.add(destino)
        }
    }

    BackHandler(enabled = pila.size > 1) { atras() }

    Crossfade(targetState = pila.last(), label = "pantalla") { pantalla ->
        when (pantalla) {
            Pantalla.Inicio -> PantallaInicio(onListo = { if (pila.last() == Pantalla.Inicio) reiniciarEn(Pantalla.Carga) })

            Pantalla.Carga -> PantallaCarga(onBienvenida = { ir(Pantalla.Nombre) })

            Pantalla.Nombre -> PantallaNombre(
                nombreInicial = estado.nombreUsuario,
                onAtras = { atras() },
                onIniciar = { nombre ->
                    estado.nombreUsuario = nombre
                    ir(Pantalla.Historias)
                },
            )

            Pantalla.Historias -> PantallaHistorias(
                historias = estado.historias,
                onAtras = { atras() },
                onCrear = { ir(Pantalla.CrearPersonaje) },
                onNuevaHistoria = { estado.agregarHistoria() },
                onBorrar = { estado.historias.remove(it) },
                onGaleria = { irSeccion(Seccion.Galeria) },
                onSeccion = { irSeccion(it) },
            )

            Pantalla.Galeria -> PantallaGaleria(
                nombreUsuario = estado.nombreUsuario,
                personajes = estado.personajes,
                fotoPerfil = estado.fotoPerfil,
                imagenHistoria = estado.imagenHistoria,
                onFotoPerfil = { estado.fotoPerfil = it },
                onImagenHistoria = { estado.imagenHistoria = it },
                onAtras = { atras() },
                onCrear = { ir(Pantalla.CrearPersonaje) },
                onAbrir = { ir(Pantalla.DetallePersonaje(it)) },
                onCambiarNombre = { ir(Pantalla.Nombre) },
                onCerrarSesion = { reiniciarEn(Pantalla.Carga) },
                onSeccion = { irSeccion(it) },
            )

            Pantalla.Mapa -> PantallaMapa(
                personajes = estado.personajes,
                onAtras = { atras() },
                onCrear = { ir(Pantalla.CrearPersonaje) },
                onGaleria = { irSeccion(Seccion.Galeria) },
                onAbrir = { ir(Pantalla.DetallePersonaje(it)) },
                onSeccion = { irSeccion(it) },
            )

            Pantalla.CrearPersonaje -> PantallaPersonaje(
                personaje = null,
                onAtras = null,
                onGuardar = { nuevo ->
                    val guardado = nuevo.copy(id = estado.nuevoId())
                    estado.guardarPersonaje(guardado)
                    Toast.makeText(context, "Personaje guardado", Toast.LENGTH_SHORT).show()
                    pila.removeAt(pila.lastIndex)
                    pila.add(Pantalla.DetallePersonaje(guardado.id))
                },
            )

            is Pantalla.DetallePersonaje -> {
                val personaje = estado.personajes.firstOrNull { it.id == pantalla.id }
                if (personaje == null) {
                    LaunchedEffect(pantalla) { atras() }
                } else {
                    PantallaPersonaje(
                        personaje = personaje,
                        onAtras = { atras() },
                        onGuardar = {
                            estado.guardarPersonaje(it)
                            Toast.makeText(context, "Cambios guardados", Toast.LENGTH_SHORT).show()
                            atras()
                        },
                    )
                }
            }
        }
    }
}
