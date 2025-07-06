package com.simpleappsinc.tip_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tip_calculator.ui.theme.TipCalculatorTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import kotlin.math.ceil
import kotlin.math.floor

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

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    var tipInput by remember { mutableStateOf("15") }
    var tipPercent by remember { mutableStateOf(15f) }

    var splitByInput by remember { mutableStateOf("1") }
    var splitBy by remember { mutableStateOf(1f) }

    val people = splitByInput.toIntOrNull()?.coerceAtLeast(1) ?: 1

    var tipAmount = amount * tipPercent / 100
    var total = amount + tipAmount
    var perPerson = total / people

    var roundedTotal by remember { mutableStateOf<Double?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Title
        Text(
            text = "Tip Calculator",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)) // Clip the corners
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)) // Rounded border
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Bill Amount:")
                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.width(80.dp),
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)) // Clip the corners
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)) // Rounded border
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                    modifier = Modifier.width(80.dp),
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
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)) // Clip the corners
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)) // Rounded border
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Split by (people):")
                OutlinedTextField(
                    value = splitByInput,
                    onValueChange = {
                        splitByInput = it.filter { ch -> ch.isDigit() }
                        splitByInput.toFloatOrNull()?.let { value ->
                            splitBy = value.coerceIn(1f, 100f)
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.width(80.dp)
                )
            }

            Slider(
                value = splitBy,
                onValueChange = {
                    splitBy = it
                    splitByInput = it.toInt().toString()
                },
                valueRange = 1f..20f,
                steps = 19,
                modifier = Modifier.fillMaxWidth()
            )
        }


        Divider()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)) // Clip the corners
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)) // Rounded border
                .padding(12.dp)
        ) {
            Text("Tip: $${"%.2f".format(tipAmount)}")
            Text("Total: $${"%.2f".format(total)}")
            Text("Per Person: $${"%.2f".format(perPerson)}")
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                total = floor(total)
                tipAmount = floor(tipAmount)
                perPerson = floor(perPerson)
            }) {
                Text("Round Down")
            }

            Button(onClick = {
                roundedTotal = ceil(total)
                total = ceil(total)
                tipAmount = ceil(tipAmount)
                perPerson = ceil(perPerson)
            }) {
                Text("Round Up")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTipCalculator() {
    TipCalculatorTheme {
        TipCalculatorApp()
    }
}
