package com.example.appcaixa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.appcaixa.FormTelaPrincipal.TelaPrincipal

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            val intent= Intent(this, TelaPrincipal::class.java)
            startActivity(intent)
            finish()
        },1500)

    }
}