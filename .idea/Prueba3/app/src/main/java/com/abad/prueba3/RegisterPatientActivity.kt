package com.abad.prueba3

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterPatientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registerpatient)  // Asegúrate de que el archivo XML exista

        // Flecha de regreso
        val backArrow = findViewById<ImageView>(R.id.backArrowImageView)
        val loginback = findViewById<TextView>(R.id.loginTextView)

        // Listener para regresar al login (MainActivity)
        loginback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Listener para la flecha que simplemente finaliza la actividad actual
        backArrow.setOnClickListener {
            finish()  // Esto te regresa a la actividad anterior (que sería ChooseRoleActivity)
        }
    }
}