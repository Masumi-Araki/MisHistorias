package com.proyecto.historias.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = RojoVino,
    onPrimary = TextoClaro,
    secondary = Beige,
    onSecondary = TextoOscuro,
    background = Crema,
    onBackground = TextoOscuro,
    surface = Crema,
    onSurface = TextoOscuro,
)

private val EsquemaOscuro = darkColorScheme(
    primary = RojoVino,
    onPrimary = TextoClaro,
    secondary = Beige,
    onSecondary = TextoOscuro,
    background = FondoOscuro,
    onBackground = TextoCrema,
    surface = FondoOscuro,
    onSurface = TextoCrema,
)

val LocalModoOscuro = staticCompositionLocalOf { false }

/** Colores que cambian entre la versión clara y la oscura del diseño (sigue al modo del sistema). */
object Tema {
    val oscuro: Boolean @Composable get() = LocalModoOscuro.current
    val fondo: Color @Composable get() = if (oscuro) FondoOscuro else Crema
    val texto: Color @Composable get() = if (oscuro) TextoCrema else TextoOscuro
    val circulo: Color @Composable get() = if (oscuro) CirculoOscuro else CremaCirculo
    val linea: Color @Composable get() = if (oscuro) TextoCrema else RojoBorde
    val panel: Color @Composable get() = if (oscuro) PanelOscuro else GrisPanel
    val campo: Color @Composable get() = if (oscuro) CampoOscuroNoche else GrisCampo
    val tarjetaNombre: Color @Composable get() = if (oscuro) TarjetaNombreOscura else CafeNombre
}

@Composable
fun HistoriasTheme(oscuro: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalModoOscuro provides oscuro) {
        MaterialTheme(
            colorScheme = if (oscuro) EsquemaOscuro else EsquemaClaro,
            typography = Typography,
            content = content,
        )
    }
}
