package com.proyecto.historias.ui.componentes

import com.proyecto.historias.ui.theme.Tema
import android.app.Activity
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.proyecto.historias.R
import com.proyecto.historias.ui.theme.Beige
import com.proyecto.historias.ui.theme.BarraOscura
import com.proyecto.historias.ui.theme.GrisBoton
import com.proyecto.historias.ui.theme.NodoMapa
import com.proyecto.historias.ui.theme.RojoVino
import com.proyecto.historias.ui.theme.TextoClaro
import com.proyecto.historias.ui.theme.TextoTenue

enum class Seccion { Mapa, Galeria, Historias }

/** Ajusta el color de los íconos de las barras del sistema según el fondo de cada pantalla. */
@Composable
fun IconosSistema(arribaOscuro: Boolean, abajoOscuro: Boolean) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? Activity)?.window ?: return@SideEffect
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !arribaOscuro
            isAppearanceLightNavigationBars = !abajoOscuro
        }
    }
}

@Composable
fun BotonAtras(onClick: () -> Unit, modifier: Modifier = Modifier, tam: Dp = 38.dp) {
    Box(
        modifier
            .size(tam)
            .shadow(4.dp, RoundedCornerShape(2.dp))
            .background(GrisBoton, RoundedCornerShape(2.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painterResource(R.drawable.ic_atras),
            contentDescription = "Regresar",
            tint = Color.White,
            modifier = Modifier.size(tam * 0.6f),
        )
    }
}

@Composable
fun BotonRojo(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes icono: Int? = null,
    tamTexto: TextUnit = 18.sp,
    peso: FontWeight = FontWeight.Medium,
    fondo: Color = RojoVino,
    colorTexto: Color = TextoClaro,
) {
    Row(
        modifier
            .shadow(4.dp, RoundedCornerShape(2.dp))
            .background(fondo, RoundedCornerShape(2.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            texto,
            color = colorTexto,
            fontSize = tamTexto,
            fontWeight = peso,
            textAlign = TextAlign.Center,
            lineHeight = tamTexto * 1.3f,
        )
        if (icono != null) {
            Spacer(Modifier.width(20.dp))
            Icon(painterResource(icono), contentDescription = null, tint = colorTexto, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun CampoOscuro(
    valor: String,
    onCambio: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    alto: Dp = 44.dp,
    unaLinea: Boolean = true,
    fondo: Color = Tema.campo,
) {
    BasicTextField(
        value = valor,
        onValueChange = onCambio,
        singleLine = unaLinea,
        textStyle = TextStyle(color = TextoClaro, fontSize = 14.sp, lineHeight = 20.sp),
        cursorBrush = SolidColor(TextoClaro),
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(2.dp))
            .background(fondo, RoundedCornerShape(2.dp)),
        decorationBox = { campo ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = alto)
                    .padding(horizontal = 16.dp, vertical = 11.dp),
                contentAlignment = if (unaLinea) Alignment.CenterStart else Alignment.TopStart,
            ) {
                if (valor.isEmpty()) Text(placeholder, color = TextoTenue, fontSize = 14.sp)
                campo()
            }
        },
    )
}

@Composable
fun Etiqueta(texto: String, modifier: Modifier = Modifier) {
    Text(
        texto,
        color = TextoClaro,
        fontSize = 18.sp,
        modifier = modifier.padding(top = 18.dp, bottom = 8.dp),
    )
}

/** Barra oscura superior con botón de regresar opcional. */
@Composable
fun BarraSuperior(
    titulo: String? = null,
    onAtras: (() -> Unit)? = null,
    alto: Dp = 64.dp,
    acciones: @Composable RowScope.() -> Unit = {},
) {
    Row(
        Modifier
            .fillMaxWidth()
            .shadow(6.dp)
            .background(BarraOscura)
            .statusBarsPadding()
            .height(alto)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onAtras != null) {
            BotonAtras(onAtras)
            Spacer(Modifier.width(18.dp))
        } else {
            Spacer(Modifier.width(46.dp))
        }
        if (titulo != null) {
            Text(titulo, color = TextoClaro, fontSize = 19.sp, modifier = Modifier.weight(1f))
        } else {
            Spacer(Modifier.weight(1f))
        }
        acciones()
    }
}

/** Barra inferior: mapa, galería (botón central con el logo) e historias. */
@Composable
fun BarraNavegacion(seleccion: Seccion, onSeccion: (Seccion) -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth()) {
        Row(
            Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
                .background(BarraOscura)
                .navigationBarsPadding()
                .height(64.dp)
                .padding(horizontal = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconoNav(R.drawable.ic_red, "Mapa interactivo", seleccion == Seccion.Mapa) { onSeccion(Seccion.Mapa) }
            IconoNav(R.drawable.ic_historial, "Tus historias", seleccion == Seccion.Historias) { onSeccion(Seccion.Historias) }
        }
        val activo = seleccion == Seccion.Galeria
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .size(72.dp)
                .shadow(
                    if (activo) 16.dp else 6.dp,
                    RoundedCornerShape(10.dp),
                    ambientColor = if (activo) Color(0xFFFFC94D) else Color.Black,
                    spotColor = if (activo) Color(0xFFFFB300) else Color.Black,
                )
                .background(Beige, RoundedCornerShape(10.dp))
                .then(if (activo) Modifier.border(2.dp, Color(0xFFFFE08A), RoundedCornerShape(10.dp)) else Modifier)
                .clickable { onSeccion(Seccion.Galeria) },
            contentAlignment = Alignment.Center,
        ) {
            Image(painterResource(R.drawable.logo), contentDescription = "Galería de personajes", modifier = Modifier.size(46.dp))
        }
    }
}

@Composable
private fun IconoNav(@DrawableRes icono: Int, descripcion: String, activo: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (activo) Color.White.copy(alpha = 0.12f) else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(painterResource(icono), contentDescription = descripcion, tint = Color(0xFFF2F2F2), modifier = Modifier.size(36.dp))
    }
}

/** Círculos tenues de fondo como en el diseño. */
@Composable
fun BoxScope.CirculosDecorativos() {
    Box(
        Modifier
            .align(Alignment.BottomStart)
            .offset(x = (-120).dp, y = 50.dp)
            .size(240.dp)
            .background(Tema.circulo, CircleShape),
    )
    Box(
        Modifier
            .align(Alignment.CenterStart)
            .offset(x = (-22).dp, y = 20.dp)
            .size(44.dp)
            .background(Tema.circulo, CircleShape),
    )
}

/** Círculo negro con la silueta de una persona (avatar sin foto). */
@Composable
fun AvatarVacio(modifier: Modifier = Modifier) {
    Box(
        modifier
            .clip(CircleShape)
            .background(NodoMapa),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painterResource(R.drawable.ic_persona),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.fillMaxSize(0.66f),
        )
    }
}

/** Foto en círculo: primero la elegida de la galería, luego la del recurso; si no hay, muestra [vacio]. */
@Composable
fun FotoCircular(
    uri: Uri?,
    @DrawableRes res: Int?,
    modifier: Modifier = Modifier,
    fondo: Color = Color.Transparent,
    vacio: @Composable BoxScope.() -> Unit,
) {
    val bitmap = rememberImagen(uri)
    Box(modifier.clip(CircleShape).background(fondo), contentAlignment = Alignment.Center) {
        when {
            bitmap != null -> Image(bitmap, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            res != null -> Image(painterResource(res), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            else -> vacio()
        }
    }
}

/** Carga una imagen de la galería reducida (máx. ~1024 px) para no gastar memoria. */
@Composable
fun rememberImagen(uri: Uri?): ImageBitmap? {
    val context = LocalContext.current
    return remember(uri) {
        if (uri == null) return@remember null
        runCatching {
            val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, limites) }
            var muestra = 1
            while (limites.outWidth / (muestra * 2) >= 1024 && limites.outHeight / (muestra * 2) >= 1024) muestra *= 2
            val opciones = BitmapFactory.Options().apply { inSampleSize = muestra }
            context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opciones) }?.asImageBitmap()
        }.getOrNull()
    }
}

@Composable
fun BotonMas(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(46.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(Beige, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(painterResource(R.drawable.ic_mas), contentDescription = "Agregar", tint = Color(0xFF111111), modifier = Modifier.size(30.dp))
    }
}
