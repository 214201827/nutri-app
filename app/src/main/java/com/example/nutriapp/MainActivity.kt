package com.example.nutriapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutriapp.ui.theme.NutriappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun OutLineTextFieldSample() {
    Column {
        var text by remember { mutableStateOf(TextFieldValue("")) }
        OutlinedTextField(
            value = text,
            label = { Text(text = "Email") },
            onValueChange = {
                text = it
            }
        )
        OutlinedTextField(
            value = text,
            label = { Text(text = "Password") },
            onValueChange = {
                text = it
            }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NutriappTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun FieldPreview() {
    NutriappTheme {
        OutLineTextFieldSample()
    }
}