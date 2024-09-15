package com.example.nutriapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutriapp.ui.theme.NutriappTheme


// Clase de actividad, aquí estará lo que se ejecutará.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriappTheme(dynamicColor = false) {

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
/*@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}*/

// Boton iniciar sesión
@Composable
fun BotonIniciarSesion(onClick: () -> Unit) {
    Button(onClick = { onClick() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Iniciar sesión")
    }
}


// Botón Iniciar con Google
@Composable
fun BotonIniciarGooglen(onClick: () -> Unit) {
    Button(onClick = { onClick() },
        modifier = Modifier.fillMaxWidth()) {
            Text("Iniciar con Google")
    }
}




// Elemento de login, donde estan los fields y botones
@Composable
fun LoginComponent() {
    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top

    ) {
        Image(painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "app_logo",
            Modifier.size(256.dp)
            )
        Spacer(Modifier.height(10.dp))
        var email by remember { mutableStateOf(TextFieldValue("")) }
        var pass by remember { mutableStateOf(TextFieldValue("")) }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            singleLine = true,
            label = { Text(text = "Email") },
            onValueChange = {
                email = it
            }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = pass,
            label = { Text(text = "Contraseña") },
            singleLine = true,
            onValueChange = {
                pass = it
            }
        )
        Spacer(Modifier.height(20.dp))
        BotonIniciarSesion {

        }

        BotonIniciarGooglen {  }

    }
}

@Preview(showBackground = true)
@Composable
fun FieldPreview() {
    NutriappTheme(dynamicColor = false) {

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

