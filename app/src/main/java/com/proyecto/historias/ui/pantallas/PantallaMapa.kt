package com.proyecto.historias.ui.pantallas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.proyecto.historias.R
import com.proyecto.historias.modelo.Personaje
import com.proyecto.historias.ui.componentes.AvatarVacio
import com.proyecto.historias.ui.componentes.BarraNavegacion
import com.proyecto.historias.ui.componentes.BarraSuperior
import com.proyecto.historias.ui.componentes.FotoCircular
import com.proyecto.historias.ui.componentes.IconosSistema
import com.proyecto.historias.ui.componentes.Seccion
import com.proyecto.historias.ui.theme.FondoMapa
import com.proyecto.historias.ui.theme.GrisBoton
import com.proyecto.historias.ui.theme.NodoMapa
import com.proyecto.historias.ui.theme.PuntoMapa
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/** Nodo del mapa: posición relativa (0..1), diámetro relativo al ancho y color del anillo. */
private data class Nodo(val x: Float, val y: Float, val diametro: Float, val anillo: Color)

private val NODOS = listOf(
    Nodo(0.25f, 0.12f, 0.27f, Color(0xFFC2233F)), // 0
    Nodo(0.70f, 0.14f, 0.20f, Color(0xFF7DE87D)), // 1
    Nodo(0.13f, 0.34f, 0.17f, Color(0xFF7C2AE8)), // 2
    Nodo(0.50f, 0.34f, 0.115f, Color(0xFFE8A317)), // 3
    Nodo(0.80f, 0.36f, 0.20f, Color(0xFF43B02A)), // 4
    Nodo(0.50f, 0.50f, 0.21f, Color(0xFF3FD9A0)), // 5
    Nodo(0.21f, 0.57f, 0.18f, Color(0xFFC21FD8)), // 6
    Nodo(0.80f, 0.64f, 0.115f, Color(0xFFD7E52A)), // 7
    Nodo(0.45f, 0.74f, 0.20f, Color(0xFFE53935)), // 8
    Nodo(0.14f, 0.80f, 0.17f, Color(0xFF2F7FE0)), // 9
)

private val VERDE = Color(0xFF2E9E1F)
private val ROJO = Color(0xFFD11A1A)
private val AZUL = Color(0xFF1E7FD9)
private val ARENA = Color(0xFFC9A04E)

/** Relaciones entre personajes: desde, hacia, color. */
private val RELACIONES = listOf(
    Triple(0, 1, Color(0xFF111111)),
    Triple(2, 0, VERDE), Triple(5, 0, VERDE), Triple(5, 1, VERDE), Triple(5, 2, VERDE), Triple(5, 3, VERDE),
    Triple(5, 4, ROJO), Triple(1, 4, ROJO),
    Triple(6, 2, AZUL), Triple(5, 6, AZUL), Triple(5, 9, AZUL), Triple(9, 6, AZUL),
    Triple(5, 8, ARENA), Triple(5, 7, ARENA), Triple(4, 7, ARENA), Triple(7, 8, ARENA),
)

/** Mapa interactivo de relaciones entre personajes. */
@Composable
fun PantallaMapa(
    personajes: List<Personaje>,
    onAtras: () -> Unit,
    onCrear: () -> Unit,
    onGaleria: () -> Unit,
    onAbrir: (Int) -> Unit,
    onSeccion: (Seccion) -> Unit,
) {
    IconosSistema(arribaOscuro = true, abajoOscuro = true)
    var menu by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize().background(FondoMapa)) {
        Column(Modifier.fillMaxSize()) {
            BarraSuperior(onAtras = onAtras, alto = 76.dp) {
                Box {
                    Box(
                        Modifier
                            .size(54.dp)
                            .shadow(4.dp, RoundedCornerShape(2.dp))
                            .background(GrisBoton, RoundedCornerShape(2.dp))
                            .clickable { menu = true },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_menu_abajo),
                            contentDescription = "Menú",
                            tint = Color.White,
                            modifier = Modifier.size(38.dp),
                        )
                    }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        DropdownMenuItem(text = { Text("Crear personaje") }, onClick = { menu = false; onCrear() })
                        DropdownMenuItem(text = { Text("Galería de personajes") }, onClick = { menu = false; onGaleria() })
                    }
                }
            }
            MapaPersonajes(
                personajes = personajes,
                onNodo = { i -> personajes.getOrNull(i)?.let { onAbrir(it.id) } ?: onCrear() },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(bottom = 96.dp),
            )
        }
        BarraNavegacion(Seccion.Mapa, onSeccion, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun MapaPersonajes(personajes: List<Personaje>, onNodo: (Int) -> Unit, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier) {
        val ancho = maxWidth
        val alto = maxHeight
        Canvas(Modifier.fillMaxSize()) {
            val paso = 14.dp.toPx()
            var y = paso / 2
            while (y < size.height) {
                var x = paso / 2
                while (x < size.width) {
                    drawCircle(PuntoMapa, radius = 1.2.dp.toPx(), center = Offset(x, y))
                    x += paso
                }
                y += paso
            }
            val grosor = 2.dp.toPx()
            fun centro(n: Nodo) = Offset(n.x * size.width, n.y * size.height)
            fun radio(n: Nodo) = n.diametro * size.width / 2
            RELACIONES.forEach { (a, b, color) ->
                flecha(centro(NODOS[a]), centro(NODOS[b]), radio(NODOS[a]), radio(NODOS[b]), color, grosor)
            }
            // Relación curva del nodo 8 al 4 por la derecha
            val desde = centro(NODOS[8]) + Offset(radio(NODOS[8]) + 4.dp.toPx(), 0f)
            val hasta = centro(NODOS[4]) + Offset(radio(NODOS[4]) * 0.75f, radio(NODOS[4]) * 0.8f)
            val control1 = Offset(size.width * 0.97f, desde.y)
            val control2 = Offset(size.width * 0.98f, hasta.y + radio(NODOS[4]))
            val curva = Path().apply {
                moveTo(desde.x, desde.y)
                cubicTo(control1.x, control1.y, control2.x, control2.y, hasta.x, hasta.y)
            }
            drawPath(curva, ROJO, style = Stroke(width = grosor, cap = StrokeCap.Round))
            puntaFlecha(hasta, atan2(hasta.y - control2.y, hasta.x - control2.x), ROJO, grosor)
        }
        NODOS.forEachIndexed { i, n ->
            val d = ancho * n.diametro
            val personaje = personajes.getOrNull(i)
            val grosorAnillo = if (n.diametro > 0.15f) 4.dp else 3.dp
            Box(
                Modifier
                    .offset(x = ancho * n.x - d / 2, y = alto * n.y - d / 2)
                    .size(d)
                    .border(grosorAnillo, n.anillo, CircleShape)
                    .padding(grosorAnillo - 1.dp)
                    .clickable { onNodo(i) },
            ) {
                if (personaje != null && (personaje.fotoUri != null || personaje.fotoRes != null)) {
                    FotoCircular(personaje.fotoUri, personaje.fotoRes, Modifier.fillMaxSize(), fondo = NodoMapa) {}
                } else {
                    AvatarVacio(Modifier.fillMaxSize())
                }
            }
        }
    }
}

private fun DrawScope.flecha(desde: Offset, hasta: Offset, rDesde: Float, rHasta: Float, color: Color, grosor: Float) {
    val dx = hasta.x - desde.x
    val dy = hasta.y - desde.y
    val largo = sqrt(dx * dx + dy * dy)
    if (largo < 1f) return
    val ux = dx / largo
    val uy = dy / largo
    val hueco = grosor * 3
    val inicio = Offset(desde.x + ux * (rDesde + hueco), desde.y + uy * (rDesde + hueco))
    val fin = Offset(hasta.x - ux * (rHasta + hueco), hasta.y - uy * (rHasta + hueco))
    drawLine(color, inicio, fin, strokeWidth = grosor, cap = StrokeCap.Round)
    puntaFlecha(fin, atan2(uy, ux), color, grosor)
}

private fun DrawScope.puntaFlecha(punta: Offset, angulo: Float, color: Color, grosor: Float) {
    val largo = grosor * 5
    for (lado in listOf(-1f, 1f)) {
        val a = angulo + PI.toFloat() + lado * 0.5f
        drawLine(color, punta, Offset(punta.x + cos(a) * largo, punta.y + sin(a) * largo), strokeWidth = grosor, cap = StrokeCap.Round)
    }
}
