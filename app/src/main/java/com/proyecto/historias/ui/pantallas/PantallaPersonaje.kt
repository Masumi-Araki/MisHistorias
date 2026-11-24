package com.proyecto.historias.ui.pantallas

import com.proyecto.historias.ui.theme.Tema
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.historias.R
import com.proyecto.historias.modelo.Personaje
import com.proyecto.historias.ui.componentes.BarraSuperior
import com.proyecto.historias.ui.componentes.BotonRojo
import com.proyecto.historias.ui.componentes.CampoOscuro
import com.proyecto.historias.ui.componentes.CirculosDecorativos
import com.proyecto.historias.ui.componentes.Etiqueta
import com.proyecto.historias.ui.componentes.FotoCircular
import com.proyecto.historias.ui.componentes.IconosSistema
import com.proyecto.historias.ui.theme.GrisClaro

/**
 * Crear personaje (personaje = null) o ver/editar uno ya registrado.
 * Al guardar devuelve el personaje con id = -1 si es nuevo.
 */
@Composable
fun PantallaPersonaje(
    personaje: Personaje?,
    onAtras: (() -> Unit)?,
    onGuardar: (Personaje) -> Unit,
) {
    IconosSistema(arribaOscuro = true, abajoOscuro = Tema.oscuro)
    var nombre by remember { mutableStateOf(personaje?.nombre.orEmpty()) }
    var rol by remember { mutableStateOf(personaje?.rol.orEmpty()) }
    var gustos by remember { mutableStateOf(personaje?.gustos.orEmpty()) }
    var origen by remember { mutableStateOf(personaje?.origen.orEmpty()) }
    var descripcion by remember { mutableStateOf(personaje?.descripcion.orEmpty()) }
    var foto by remember { mutableStateOf<Uri?>(personaje?.fotoUri) }
    var error by remember { mutableStateOf(false) }
    val selector = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) foto = uri
    }

    Box(Modifier.fillMaxSize().background(Tema.fondo)) {
        CirculosDecorativos()
        Column(Modifier.fillMaxSize()) {
            BarraSuperior(titulo = "Crear personaje", onAtras = onAtras) {
                if (onAtras == null) {
                    Image(painterResource(R.drawable.logo), contentDescription = null, modifier = Modifier.size(44.dp))
                }
            }
            Column(
                Modifier
                    .weight(1f)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .shadow(4.dp)
                        .background(Tema.panel)
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                ) {
                    FotoCircular(
                        uri = foto,
                        res = if (foto == null) personaje?.fotoRes else null,
                        fondo = GrisClaro,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(100.dp)
                            .clickable { selector.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_subir),
                            contentDescription = "Subir foto del personaje",
                            tint = Color.Black,
                            modifier = Modifier.size(62.dp),
                        )
                    }
                    Etiqueta("Nombre del Personaje")
                    CampoOscuro(nombre, { nombre = it; error = false }, placeholder = "********")
                    if (error) {
                        Text(
                            "Ponle nombre a tu personaje",
                            color = Color(0xFFFFB4A9),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 6.dp),
                        )
                    }
                    Etiqueta("Rol en la historia")
                    CampoOscuro(rol, { rol = it }, placeholder = "Protagonista")
                    Etiqueta("Gustos")
                    CampoOscuro(gustos, { gustos = it }, placeholder = "Gustos")
                    Etiqueta("Origen-Personalidad")
                    CampoOscuro(origen, { origen = it }, placeholder = "Enter location", unaLinea = false)
                    Etiqueta("Descripción")
                    CampoOscuro(
                        descripcion,
                        { descripcion = it },
                        placeholder = "Biografía o Descripción rápida",
                        unaLinea = false,
                        alto = 84.dp,
                    )
                    Spacer(Modifier.height(28.dp))
                    BotonRojo(
                        "Guardar personaje\nnuevo",
                        onClick = {
                            if (nombre.isBlank()) {
                                error = true
                            } else {
                                onGuardar(
                                    Personaje(
                                        id = personaje?.id ?: -1,
                                        nombre = nombre.trim(),
                                        rol = rol.trim(),
                                        gustos = gustos.trim(),
                                        origen = origen.trim(),
                                        descripcion = descripcion.trim(),
                                        fotoRes = if (foto == null) personaje?.fotoRes else null,
                                        fotoUri = foto,
                                    ),
                                )
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(196.dp),
                    )
                    Spacer(Modifier.height(36.dp))
                }
            }
        }
    }
}
