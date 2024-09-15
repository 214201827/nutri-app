package com.example.nutriapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutriapp.ui.theme.NutriappTheme
import org.w3c.dom.Text



// Clase de actividad, aquí estará lo que se ejecutará.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriappTheme {

                    /*Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )*/
                    LoginComponent()


            }
        }
    }
}

// Texto de prueba
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

// Boton iniciar sesión
@Composable
fun BotonIniciarSesion(onClick: () -> Unit) {
    Button(onClick = { onClick() },  modifier = Modifier.fillMaxWidth()) {
        Text("Iniciar sesión")
    }
}


// Botón Iniciar con Google
@Composable
fun BotonIniciarGooglen(onClick: () -> Unit) {
    Button(onClick = { onClick() }, modifier = Modifier.fillMaxWidth()) {
        Text("Iniciar con Google")
    }
}

// Elemento de login, donde estan los fields y botones
@Composable
fun LoginComponent(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier

    ) {
        var email by remember { mutableStateOf(TextFieldValue("")) }
        var pass by remember { mutableStateOf(TextFieldValue("")) }
        OutlinedTextField(
            modifier = modifier,
            value = email,
            label = { Text(text = "Email") },
            onValueChange = {
                email = it
            }
        )
        OutlinedTextField(
            modifier = modifier,
            value = pass,
            label = { Text(text = "Contraseña") },
            onValueChange = {
                pass = it
            }
        )
        BotonIniciarSesion {  }

        BotonIniciarGooglen {  }

    }
}

@Preview(showBackground = true)
@Composable
fun FieldPreview() {
    NutriappTheme {

            /*Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )*/
            LoginComponent()

    }
}


/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NutriappTheme {
        Greeting("Android")
    }
}*/

