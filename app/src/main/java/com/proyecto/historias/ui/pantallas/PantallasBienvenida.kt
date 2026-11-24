package com.proyecto.historias.ui.pantallas

import com.proyecto.historias.ui.theme.RojoVino
import com.proyecto.historias.ui.theme.TextoCrema
import com.proyecto.historias.ui.theme.Tema
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.historias.R
import com.proyecto.historias.ui.componentes.BotonAtras
import com.proyecto.historias.ui.componentes.BotonRojo
import com.proyecto.historias.ui.componentes.CampoOscuro
import com.proyecto.historias.ui.componentes.CirculosDecorativos
import com.proyecto.historias.ui.componentes.IconosSistema
import com.proyecto.historias.ui.theme.Crema
import com.proyecto.historias.ui.theme.GrisCampoNombre
import com.proyecto.historias.ui.theme.TextoClaro
import com.proyecto.historias.ui.theme.TextoOscuro
import kotlinx.coroutines.delay

/** Pantalla de inicio: logo + pluma "Iniciando"; avanza sola. */
@Composable
fun PantallaInicio(onListo: () -> Unit) {
    IconosSistema(arribaOscuro = Tema.oscuro, abajoOscuro = Tema.oscuro)
    LaunchedEffect(Unit) {
        delay(2200)
        onListo()
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Tema.fondo)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onListo() }
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(0.3f))
        Image(painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.width(180.dp))
        Image(
            painterResource(if (Tema.oscuro) R.drawable.pluma_iniciando_oscuro else R.drawable.pluma_iniciando),
            contentDescription = "Iniciando",
            modifier = Modifier
                .width(272.dp)
                .offset(x = (-8).dp, y = (-10).dp),
        )
        Spacer(Modifier.weight(0.7f))
    }
}

/** Pantalla de carga: invita a iniciar la cuenta. */
@Composable
fun PantallaCarga(onBienvenida: () -> Unit) {
    IconosSistema(arribaOscuro = Tema.oscuro, abajoOscuro = Tema.oscuro)
    Column(
        Modifier
            .fillMaxSize()
            .background(Tema.fondo)
            .statusBarsPadding()
            .padding(horizontal = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(64.dp))
        Image(painterResource(R.drawable.logo), contentDescription = "Logo", modifier = Modifier.width(176.dp))
        Spacer(Modifier.height(34.dp))
        Text(
            "Prepárate para\niniciar tu cuenta",
            color = Tema.texto,
            fontSize = 23.sp,
            lineHeight = 32.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(64.dp))
        BotonRojo(
            "Bienvenid@",
            onClick = onBienvenida,
            icono = R.drawable.ic_flecha_derecha,
            peso = FontWeight.Bold,
            fondo = if (Tema.oscuro) Crema else RojoVino,
            colorTexto = if (Tema.oscuro) TextoOscuro else TextoClaro,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        )
    }
}

/** Ingresar el nombre con el que aparecerás en la plataforma. */
@Composable
fun PantallaNombre(nombreInicial: String, onAtras: () -> Unit, onIniciar: (String) -> Unit) {
    IconosSistema(arribaOscuro = Tema.oscuro, abajoOscuro = Tema.oscuro)
    var nombre by remember { mutableStateOf(nombreInicial) }
    var error by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize().background(Tema.fondo)) {
        CirculosDecorativos()
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(Modifier.fillMaxWidth().padding(start = 18.dp, top = 18.dp)) {
                BotonAtras(onAtras, tam = 54.dp)
            }
            Image(
                painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.width(160.dp).offset(x = 6.dp),
            )
            Spacer(Modifier.height(18.dp))
            Column(
                Modifier
                    .width(272.dp)
                    .shadow(6.dp)
                    .background(Tema.tarjetaNombre)
                    .border(1.dp, if (Tema.oscuro) TextoCrema else Color(0xFFE9E3CF))
                    .padding(4.dp),
            ) {
                Text(
                    "Ingresa tu nombre para la plataforma:",
                    color = TextoClaro,
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 40.dp, bottom = 32.dp),
                )
                CampoOscuro(
                    nombre,
                    { nombre = it; error = false },
                    placeholder = "********",
                    fondo = if (Tema.oscuro) Color(0xFF3A3633) else GrisCampoNombre,
                    alto = 40.dp,
                )
                if (error) {
                    Text(
                        "Escribe tu nombre para continuar",
                        color = Color(0xFFFFB4A9),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 14.dp, top = 6.dp),
                    )
                }
                Spacer(Modifier.height(34.dp))
                BotonRojo(
                    "Iniciar",
                    onClick = { if (nombre.isBlank()) error = true else onIniciar(nombre.trim()) },
                    tamTexto = 20.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(174.dp)
                        .height(52.dp),
                )
                Spacer(Modifier.height(40.dp))
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}
