package com.abad.prueba3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class ChooseRoleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooseregister)  // Asegúrate de que el layout sea el correcto

        // Referencia al botón de registrarse como Paciente
        val registerAsPatientButton = findViewById<Button>(R.id.registerAsPatientButton)
        // Referencia al botón de registrarse como Nutriólogo
        val registerAsNutriologistButton = findViewById<Button>(R.id.registerAsNutriologistButton)

        // Evento para el botón de Paciente
        registerAsPatientButton.setOnClickListener {
            val intent = Intent(this, RegisterPatientActivity::class.java)
            startActivity(intent)
        }

        // Evento para el botón de Nutriólogo
        registerAsNutriologistButton.setOnClickListener {
            val intent = Intent(this, RegisterPatientActivity::class.java)
            startActivity(intent)
        }
    }
}

