package com.example.nutricionapp

import android.app.Application

class NutricionApp : Application() {

    lateinit var connectivityObserver: ConnectivityObserver


    override fun onCreate() {
        super.onCreate()
        connectivityObserver = ConnectivityObserver(this)
        // Inicializa otros componentes globales aquí si es necesario
    }
}

