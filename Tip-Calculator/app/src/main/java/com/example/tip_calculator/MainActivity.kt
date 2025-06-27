package com.example.tip_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tip_calculator.ui.theme.TipCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                TipCalculatorApp()
            }
        }
    }
}

@Composable
fun TipCalculatorApp() {
    var amountInput by remember { mutableStateOf("") }
    //var splitByInput by remember { mutableStateOf("1") }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    var tipInput by remember { mutableStateOf("15") }
    var tipPercent by remember { mutableStateOf(15f) }

    var splitByInput by remember { mutableStateOf("1") }
    var splitBy by remember { mutableStateOf(1f) }

    val people = splitByInput.toIntOrNull()?.coerceAtLeast(1) ?: 1

    val tipAmount = amount * tipPercent / 100
    val total = amount + tipAmount
    val perPerson = total / people

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(60.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Tip Calculator", style = MaterialTheme.typography.headlineMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Bill Amount:")
            OutlinedTextField(
                value = amountInput,
                onValueChange = { amountInput = it },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.width(60.dp),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Tip %:")
            OutlinedTextField(
                value = tipInput,
                onValueChange = {
                    tipInput = it.filter { ch -> ch.isDigit() } // optionally allow only digits
                    tipInput.toFloatOrNull()?.let {
                        tipPercent = it.coerceIn(0f, 100f)
                    }
                },
                singleLine = true,
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }

        Slider(
            value = tipPercent,
            onValueChange = {
                tipPercent = it
                tipInput = it.toInt().toString()
            },
            valueRange = 0f..30f,
            steps = 30,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Split by (people):")
            OutlinedTextField(
                value = splitByInput,
                onValueChange = {
                    splitByInput = it.filter { ch -> ch.isDigit() } // optionally filter out non-digits
                    splitByInput.toFloatOrNull()?.let { value ->
                        splitBy = value.coerceIn(1f, 100f)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.width(60.dp)
            )
        }

        Slider(
            value = splitBy,
            onValueChange = {
                splitBy = it
                splitByInput = it.toInt().toString()
            },
            valueRange = 1f..20f, // adjust as needed
            steps = 19,
            modifier = Modifier.fillMaxWidth()
        )

        Divider()

        Text("Tip: $${"%.2f".format(tipAmount)}")
        Text("Total: $${"%.2f".format(total)}")
        Text("Per Person: $${"%.2f".format(perPerson)}")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTipCalculator() {
    TipCalculatorTheme {
        TipCalculatorApp()
    }
}
