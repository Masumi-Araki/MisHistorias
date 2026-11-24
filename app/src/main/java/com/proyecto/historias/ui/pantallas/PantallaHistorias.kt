package com.proyecto.historias.ui.pantallas

import com.proyecto.historias.ui.theme.Tema
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.historias.R
import com.proyecto.historias.modelo.Historia
import com.proyecto.historias.ui.componentes.BarraNavegacion
import com.proyecto.historias.ui.componentes.BarraSuperior
import com.proyecto.historias.ui.componentes.BotonMas
import com.proyecto.historias.ui.componentes.IconosSistema
import com.proyecto.historias.ui.componentes.Seccion
import com.proyecto.historias.ui.theme.CafeCaja
import com.proyecto.historias.ui.theme.CafeTarjeta
import com.proyecto.historias.ui.theme.GrisBoton
import com.proyecto.historias.ui.theme.GrisClaro
import com.proyecto.historias.ui.theme.RojoVino
import com.proyecto.historias.ui.theme.TextoClaro
import com.proyecto.historias.ui.theme.TextoTenue

/** Menú de tus historias. */
@Composable
fun PantallaHistorias(
    historias: List<Historia>,
    onAtras: () -> Unit,
    onCrear: () -> Unit,
    onNuevaHistoria: () -> Unit,
    onBorrar: (Historia) -> Unit,
    onGaleria: () -> Unit,
    onSeccion: (Seccion) -> Unit,
) {
    IconosSistema(arribaOscuro = true, abajoOscuro = true)
    var abierta by remember { mutableStateOf(historias.firstOrNull()?.id) }
    Box(Modifier.fillMaxSize().background(Tema.fondo)) {
        Column(Modifier.fillMaxSize()) {
            BarraSuperior(onAtras = onAtras)
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 130.dp),
            ) {
                Spacer(Modifier.height(12.dp))
                Pestanas(onCrear = onCrear, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.height(12.dp))
                historias.forEach { h ->
                    TarjetaHistoria(
                        historia = h,
                        expandida = abierta == h.id,
                        onToggle = { abierta = if (abierta == h.id) null else h.id },
                        onBorrar = { onBorrar(h) },
                        onGaleria = onGaleria,
                    )
                    Spacer(Modifier.height(20.dp))
                }
                Spacer(Modifier.height(6.dp))
                BotonMas(onNuevaHistoria, Modifier.align(Alignment.CenterHorizontally))
            }
        }
        BarraNavegacion(Seccion.Historias, onSeccion, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun Pestanas(onCrear: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .width(258.dp)
            .height(46.dp)
            .shadow(4.dp)
            .background(GrisBoton)
            .padding(5.dp),
    ) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(RojoVino, RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text("Historia", color = TextoClaro, fontSize = 18.sp)
        }
        Box(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(onClick = onCrear),
            contentAlignment = Alignment.Center,
        ) {
            Text("Create", color = TextoClaro, fontSize = 18.sp)
        }
    }
}

@Composable
private fun TarjetaHistoria(
    historia: Historia,
    expandida: Boolean,
    onToggle: () -> Unit,
    onBorrar: () -> Unit,
    onGaleria: () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(4.dp, RoundedCornerShape(2.dp))
                .background(CafeTarjeta, RoundedCornerShape(2.dp))
                .clickable(onClick = onToggle)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .size(width = 54.dp, height = 42.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                if (historia.miniaturaRes != null) {
                    Image(
                        painterResource(historia.miniaturaRes),
                        contentDescription = historia.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Icon(
                        painterResource(R.drawable.ic_imagen),
                        contentDescription = null,
                        tint = Color(0xFF111111),
                        modifier = Modifier.size(34.dp),
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    historia.titulo,
                    color = TextoClaro,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Última Edición: ${historia.ultimaEdicion}",
                    color = TextoTenue,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End),
                )
            }
            Icon(
                painterResource(R.drawable.ic_basura),
                contentDescription = "Borrar historia",
                tint = RojoVino,
                modifier = Modifier
                    .align(Alignment.Top)
                    .size(24.dp)
                    .clickable(onClick = onBorrar),
            )
        }
        AnimatedVisibility(visible = expandida) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .background(GrisClaro)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    "Nombre de la Historia: ${historia.titulo}",
                    color = TextoClaro,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CafeCaja)
                        .padding(horizontal = 8.dp, vertical = 9.dp),
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(CafeCaja)
                        .padding(horizontal = 8.dp, vertical = 9.dp),
                ) {
                    Text("Descripción de la historia:", color = TextoClaro, fontSize = 12.sp)
                    Spacer(Modifier.height(3.dp))
                    Text(historia.descripcion, color = TextoClaro, fontSize = 9.5.sp, lineHeight = 13.sp)
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(CafeCaja)
                        .clickable(onClick = onGaleria)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Galería del Mundo", color = TextoClaro, fontSize = 11.sp)
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painterResource(R.drawable.ic_flecha_abajo),
                        contentDescription = null,
                        tint = TextoClaro,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}
