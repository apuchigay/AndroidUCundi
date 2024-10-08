package com.example.clasekotlin.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.pow
import java.text.DecimalFormat

val colores = listOf("Negro", "Marrón", "Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Violeta", "Gris", "Blanco")
val tolerancias = listOf("±5% Dorado", "±10% Plateado", "±20% Ninguno")

// Función para mapear los nombres de colores a los valores de Color en Compose
fun obtenerColor(nombre: String): Color {
    return when (nombre) {
        "Negro" -> Color.Black
        "Marrón" -> Color(0xFF8B4513) // Marrón oscuro
        "Rojo" -> Color.Red
        "Naranja" -> Color(0xFFFFA500)
        "Amarillo" -> Color.Yellow
        "Verde" -> Color.Green
        "Azul" -> Color.Blue
        "Violeta" -> Color(0xFF8A2BE2)
        "Gris" -> Color.Gray
        "Blanco" -> Color.White
        "Dorado" -> Color(0xFFD4AF37) // Dorado
        "Plateado" -> Color(0xFFC0C0C0) // Plateado
        else -> Color.Transparent
    }
}

@Preview(showBackground = true)
@Composable
fun Interfaz() {
    var banda1 by remember { mutableStateOf(colores[0]) }
    var banda2 by remember { mutableStateOf(colores[0]) }
    var multiplicador by remember { mutableStateOf(colores[0]) }
    var tolerancia by remember { mutableStateOf(tolerancias[0]) }
    var resistencia by remember { mutableStateOf("0 Ω") }

    // Función para formatear la resistencia con separador de miles
    fun formatearResistencia(valor: Double): String {
        val formatter = DecimalFormat("#,###") // Formato con separador de miles
        return if (valor % 1.0 == 0.0) {
            "${formatter.format(valor.toInt())} Ω" // Mostrar como entero si no tiene decimales
        } else {
            "${formatter.format(valor)} Ω" // Mostrar con decimales si es necesario
        }
    }

    // Función para calcular la resistencia
    fun calcularResistencia() {
        val valorBanda1 = colores.indexOf(banda1) * 10
        val valorBanda2 = colores.indexOf(banda2)
        val valorMultiplicador = 10.0.pow(colores.indexOf(multiplicador).toDouble())
        val resultado = (valorBanda1 + valorBanda2) * valorMultiplicador

        resistencia = formatearResistencia(resultado)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center) // Centramos todo el contenido
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DropdownSelector("Selecciona Banda 1", banda1) { nuevoValor ->
            banda1 = nuevoValor
            calcularResistencia()
        }

        DropdownSelector("Selecciona Banda 2", banda2) { nuevoValor ->
            banda2 = nuevoValor
            calcularResistencia()
        }

        DropdownSelector("Selecciona Multiplicador", multiplicador) { nuevoValor ->
            multiplicador = nuevoValor
            calcularResistencia()
        }

        DropdownSelector("Selecciona Tolerancia", tolerancia, opciones = tolerancias) { nuevoValor ->
            tolerancia = nuevoValor
            calcularResistencia()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Resistencia: $resistencia $tolerancia", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar los colores seleccionados como cuadros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ColorBox(obtenerColor(banda1), "Banda 1")
            ColorBox(obtenerColor(banda2), "Banda 2")
            ColorBox(obtenerColor(multiplicador), "Multiplicador")
            ColorBox(
                obtenerColor(if (tolerancia == "±5% Dorado") "Dorado" else if (tolerancia == "±10% Plateado") "Plateado" else "Transparente"),
                "Tolerancia"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    selectedValue: String,
    opciones: List<String> = colores,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedValue,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor(),
                label = { Text(label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onValueChange(opcion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// Función para mostrar un cuadro de color
@Composable
fun ColorBox(color: Color, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}
