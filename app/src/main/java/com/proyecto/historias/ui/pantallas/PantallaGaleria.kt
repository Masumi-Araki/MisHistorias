package com.proyecto.historias.ui.pantallas

import com.proyecto.historias.ui.theme.Tema
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.historias.R
import com.proyecto.historias.modelo.Personaje
import com.proyecto.historias.ui.componentes.AvatarVacio
import com.proyecto.historias.ui.componentes.BarraNavegacion
import com.proyecto.historias.ui.componentes.BotonAtras
import com.proyecto.historias.ui.componentes.FotoCircular
import com.proyecto.historias.ui.componentes.IconosSistema
import com.proyecto.historias.ui.componentes.Seccion
import com.proyecto.historias.ui.componentes.rememberImagen
import com.proyecto.historias.ui.theme.BeigeEngrane
import com.proyecto.historias.ui.theme.CafePersonaje
import com.proyecto.historias.ui.theme.GrisClaro
import com.proyecto.historias.ui.theme.GrisFoto
import com.proyecto.historias.ui.theme.GrisImagen
import com.proyecto.historias.ui.theme.TextoClaro
import com.proyecto.historias.ui.theme.TextoOscuro

/** Galería de personajes con foto de perfil e imagen de la historia. */
@Composable
fun PantallaGaleria(
    nombreUsuario: String,
    personajes: List<Personaje>,
    fotoPerfil: Uri?,
    imagenHistoria: Uri?,
    onFotoPerfil: (Uri) -> Unit,
    onImagenHistoria: (Uri) -> Unit,
    onAtras: () -> Unit,
    onCrear: () -> Unit,
    onAbrir: (Int) -> Unit,
    onCambiarNombre: () -> Unit,
    onCerrarSesion: () -> Unit,
    onSeccion: (Seccion) -> Unit,
) {
    IconosSistema(arribaOscuro = Tema.oscuro, abajoOscuro = true)
    val colorTexto = Tema.texto
    val colorLinea = Tema.linea
    val selectorPerfil = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) onFotoPerfil(uri)
    }
    val selectorHistoria = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) onImagenHistoria(uri)
    }
    val soloImagenes = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    var menu by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize().background(Tema.fondo)) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 130.dp),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BotonAtras(onAtras)
                Spacer(Modifier.weight(1f))
                Text("@", color = colorTexto, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    nombreUsuario,
                    color = colorTexto,
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(140.dp)
                        .drawBehind {
                            drawLine(colorTexto, Offset(0f, size.height), Offset(size.width, size.height), 2.dp.toPx())
                        }
                        .padding(start = 4.dp, bottom = 2.dp),
                )
                Spacer(Modifier.weight(1f))
                Box {
                    Box(
                        Modifier
                            .size(40.dp)
                            .shadow(4.dp, RoundedCornerShape(8.dp))
                            .background(BeigeEngrane, RoundedCornerShape(8.dp))
                            .clickable { menu = true },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_engrane),
                            contentDescription = "Configuración",
                            tint = Color(0xFF1A1A1A),
                            modifier = Modifier.size(26.dp),
                        )
                    }
                    DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                        DropdownMenuItem(text = { Text("Cambiar nombre") }, onClick = { menu = false; onCambiarNombre() })
                        DropdownMenuItem(text = { Text("Cerrar sesión") }, onClick = { menu = false; onCerrarSesion() })
                    }
                }
            }

            Box(Modifier.fillMaxWidth().height(156.dp)) {
                Canvas(Modifier.fillMaxSize()) {
                    val grosor = 3.dp.toPx()
                    drawLine(colorLinea, Offset(size.width * 0.3f, 12.dp.toPx()), Offset(size.width, 12.dp.toPx()), grosor)
                    drawLine(colorLinea, Offset(0f, 104.dp.toPx()), Offset(30.dp.toPx(), 104.dp.toPx()), grosor)
                }
                FotoCircular(
                    uri = fotoPerfil,
                    res = null,
                    fondo = GrisFoto,
                    modifier = Modifier
                        .padding(start = 18.dp)
                        .size(124.dp)
                        .border(4.dp, colorLinea, CircleShape)
                        .clickable { selectorPerfil.launch(soloImagenes) },
                ) {
                    Text(
                        "Foto de\nperfil",
                        color = TextoOscuro,
                        fontSize = 23.sp,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Center,
                    )
                }
                Column(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 24.dp, end = 18.dp)
                        .width(184.dp)
                        .height(128.dp)
                        .clickable { selectorHistoria.launch(soloImagenes) },
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .background(GrisClaro),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Imagen Historia", color = TextoOscuro, fontSize = 12.sp)
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(GrisImagen),
                        contentAlignment = Alignment.Center,
                    ) {
                        val imagen = rememberImagen(imagenHistoria)
                        if (imagen != null) {
                            Image(imagen, "Imagen de la historia", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                        } else {
                            Icon(
                                painterResource(R.drawable.ic_imagen),
                                contentDescription = "Subir imagen",
                                tint = Color(0xFF111111),
                                modifier = Modifier.size(72.dp),
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(26.dp))
            val vacias = maxOf(1, 6 - personajes.size)
            val casillas: List<Personaje?> = personajes + List<Personaje?>(vacias) { null }
            casillas.chunked(3).forEach { fila ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    fila.forEach { p ->
                        TarjetaPersonaje(p, Modifier.weight(1f)) {
                            if (p == null) onCrear() else onAbrir(p.id)
                        }
                    }
                    repeat(3 - fila.size) { Spacer(Modifier.weight(1f)) }
                }
                Spacer(Modifier.height(22.dp))
            }
        }
        BarraNavegacion(Seccion.Galeria, onSeccion, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun TarjetaPersonaje(personaje: Personaje?, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier
            .height(142.dp)
            .shadow(3.dp)
            .background(CafePersonaje)
            .clickable(onClick = onClick)
            .padding(top = 16.dp, start = 6.dp, end = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (personaje != null && (personaje.fotoUri != null || personaje.fotoRes != null)) {
            FotoCircular(personaje.fotoUri, personaje.fotoRes, Modifier.size(50.dp)) {}
        } else {
            AvatarVacio(Modifier.size(50.dp))
        }
        Spacer(Modifier.height(10.dp))
        Text(
            personaje?.nombre ?: "******",
            color = TextoClaro,
            fontSize = if (personaje == null) 16.sp else 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
