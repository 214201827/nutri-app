package com.example.nutricionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutricionapp.R
import com.example.nutricionapp.ui.theme.NutricionAppTheme


@Composable
fun ProcesoVerificacionScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(painter = painterResource(id = R.drawable.denied_logo),
            contentDescription = "app_logo",
            Modifier.size(200.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text("Su cuenta está en proceso de verificación.",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text("Su cuenta está en proceso de verificación aún, tenga paciencia. " +
                "En un máximo de 3 días aproximadamente se le notificará el estatus " +
                "de su peticiíon.",
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    NutricionAppTheme {
        ProcesoVerificacionScreen(navController = rememberNavController())
    }
}